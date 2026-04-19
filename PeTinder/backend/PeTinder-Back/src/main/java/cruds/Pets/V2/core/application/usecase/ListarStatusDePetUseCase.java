package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListarStatusDePetUseCase {

    private final PetStatusGateway petStatusGateway;

    public ListarStatusDePetUseCase(PetStatusGateway petStatusGateway) {
        this.petStatusGateway = petStatusGateway;
    }

    public List<PetStatusEnum> executar(UUID petId) {
        List<PetStatus> statuses = petStatusGateway.buscarPorPet(petId);
        List<PetStatusEnum> statusList = statuses.stream()
                .map(PetStatus::getStatus)
                .collect(Collectors.toList());

        if (statusList.isEmpty()) {
            statusList.add(PetStatusEnum.ADOPTED);
        }

        return statusList;
    }
}
