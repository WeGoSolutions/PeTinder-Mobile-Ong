package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Pets.V2.core.application.exception.PetException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CriarOuAtualizarPetStatusUseCase {

    private final PetStatusGateway petStatusGateway;
    private final PetGateway petGateway;

    public CriarOuAtualizarPetStatusUseCase(PetStatusGateway petStatusGateway, PetGateway petGateway) {
        this.petStatusGateway = petStatusGateway;
        this.petGateway = petGateway;
    }

    public PetStatus executar(UUID petId, UUID userId, PetStatusEnum status) {
        // Verificar se pet existe
        var pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException("Pet não encontrado"));

        // Buscar status existente ou criar novo
        var petStatusOpt = petStatusGateway.buscarPorPetEUsuario(petId, userId);

        PetStatus petStatus;
        if (petStatusOpt.isPresent()) {
            // Atualizar status existente
            petStatus = petStatusOpt.get();
            petStatus.setStatus(status);
            if (status == PetStatusEnum.PENDING) {
                petStatus.setAlteradoParaPending(LocalDateTime.now());
            }
            petStatus = petStatusGateway.atualizar(petStatus);
        } else {
            // Criar novo status
            LocalDateTime alteradoParaPending = status == PetStatusEnum.PENDING ? LocalDateTime.now() : null;
            petStatus = new PetStatus(null, petId, userId, status, alteradoParaPending, LocalDateTime.now());
            petStatus = petStatusGateway.salvar(petStatus);
        }

        return petStatus;
    }
}
