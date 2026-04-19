package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Users.V2.core.adapter.UsuarioGateway;

import java.util.Optional;
import java.util.UUID;

public class AdotarPetStatusUseCase {

    private final PetStatusGateway petStatusGateway;
    private final PetGateway petGateway;
    private final UsuarioGateway usuarioGateway;

    public AdotarPetStatusUseCase(PetStatusGateway petStatusGateway, 
                                  PetGateway petGateway,
                                  UsuarioGateway usuarioGateway) {
        this.petStatusGateway = petStatusGateway;
        this.petGateway = petGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public PetStatus adotar(UUID petId, UUID userId) {
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

        // Verificar se pet já foi adotado
        if (pet.getIsAdotado() != null && pet.getIsAdotado()) {
            throw new PetException.PetJaAdotadoException(
                    "Pet com ID " + petId + " já foi adotado"
            );
        }

        // Remover todos os outros status para este pet (outros interessados)
        petStatusGateway.removerPorPet(petId);

        // Verificar se já existe um status para este pet e usuário
        Optional<PetStatus> statusExistente = petStatusGateway.buscarPorPetEUsuario(petId, userId);
        
        PetStatus status;
        if (statusExistente.isPresent()) {
            status = statusExistente.get();
            status.alterarStatus(PetStatusEnum.ADOPTED);
            status = petStatusGateway.atualizar(status);
        } else {
            // Criar novo status
            status = new PetStatus(petId, userId, PetStatusEnum.ADOPTED);
            status = petStatusGateway.salvar(status);
        }

        // Marcar pet como adotado
        pet.adotar();
        petGateway.atualizar(pet);

        return status;
    }

    public void cancelarAdocao(UUID petId) {
        Pet pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + petId + " não encontrado"
                ));

        // Remover todos os status de adoção para este pet
        petStatusGateway.removerPorPet(petId);

        // Marcar pet como disponível
        pet.cancelarAdocao();
        petGateway.atualizar(pet);
    }
}