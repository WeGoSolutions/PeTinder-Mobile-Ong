package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Pets.V2.core.application.exception.PetException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AdotarPetPorUsuarioUseCase {

    private final PetStatusGateway petStatusGateway;
    private final PetGateway petGateway;

    public AdotarPetPorUsuarioUseCase(PetStatusGateway petStatusGateway, PetGateway petGateway) {
        this.petStatusGateway = petStatusGateway;
        this.petGateway = petGateway;
    }

    @Transactional
    public PetStatus executar(UUID petId, UUID userId) {
        // Verificar se pet existe
        var pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException("Pet não encontrado"));

        // Remover todos os outros status deste pet (exceto o do usuário adotante)
        var todosStatus = petStatusGateway.buscarPorPet(petId);
        for (PetStatus status : todosStatus) {
            if (!status.getUserId().equals(userId)) {
                petStatusGateway.remover(status.getId());
            }
        }

        // Buscar ou criar o status para o usuário adotante
        var petStatusOpt = petStatusGateway.buscarPorPetEUsuario(petId, userId);
        PetStatus petStatus;

        if (petStatusOpt.isPresent()) {
            petStatus = petStatusOpt.get();
            petStatus.setStatus(PetStatusEnum.ADOPTED);
            petStatus = petStatusGateway.atualizar(petStatus);
        } else {
            petStatus = new PetStatus(null, petId, userId, PetStatusEnum.ADOPTED, null, LocalDateTime.now());
            petStatus = petStatusGateway.salvar(petStatus);
        }

        // Marcar o pet como adotado
        pet.adotar();
        petGateway.atualizar(pet);

        return petStatus;
    }
}
