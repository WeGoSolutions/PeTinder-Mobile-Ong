package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public class CurtirPetStatusUseCase {

    private final PetStatusGateway petStatusGateway;
    private final PetGateway petGateway;
    private final UsuarioGateway usuarioGateway;

    public CurtirPetStatusUseCase(PetStatusGateway petStatusGateway, 
                                  PetGateway petGateway,
                                  UsuarioGateway usuarioGateway) {
        this.petStatusGateway = petStatusGateway;
        this.petGateway = petGateway;
        this.usuarioGateway = usuarioGateway;
    }

    @Transactional
    public PetStatus curtir(UUID petId, UUID userId) {
        // Validar se pet existe
        Pet pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + petId + " não encontrado"
                ));

        // Validar se usuário existe
        if (!usuarioGateway.existePorId(userId)) {
            throw new PetException.PetNaoEncontradoException(
                    "Usuário com ID " + userId + " não encontrado"
            );
        }

        // Verificar se já existe um status para este pet e usuário
        Optional<PetStatus> statusExistente = petStatusGateway.buscarPorPetEUsuario(petId, userId);
        
        if (statusExistente.isPresent()) {
            PetStatus status = statusExistente.get();
            if (!status.isLiked()) {
                // Se não estava curtido, curtir
                status.alterarStatus(PetStatusEnum.LIKED);
                pet.curtir();
                petGateway.atualizar(pet);
                return petStatusGateway.atualizar(status);
            } else {
                // Se já estava curtido, descurtir (toggle behavior)
                pet.descurtir();
                petGateway.atualizar(pet);
                petStatusGateway.remover(status.getId());
                return null; // ou retorne um status indicando que foi removido
            }
        } else {
            // Criar novo status
            PetStatus novoStatus = new PetStatus(petId, userId, PetStatusEnum.LIKED);
            pet.curtir();
            petGateway.atualizar(pet);
            return petStatusGateway.salvar(novoStatus);
        }
    }

    @Transactional
    public void descurtir(UUID petId, UUID userId) {
        Optional<PetStatus> status = petStatusGateway.buscarPorPetEUsuario(petId, userId);
        
        if (status.isPresent() && status.get().isLiked()) {
            // Buscar o pet e decrementar curtidas
            Pet pet = petGateway.buscarPorId(petId)
                    .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                            "Pet com ID " + petId + " não encontrado"
                    ));

            pet.descurtir();
            petGateway.atualizar(pet);
            petStatusGateway.remover(status.get().getId());
        }
    }
}