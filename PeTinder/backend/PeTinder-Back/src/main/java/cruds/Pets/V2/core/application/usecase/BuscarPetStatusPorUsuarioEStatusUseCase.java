package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarPetStatusPorUsuarioEStatusUseCase {

    private final PetStatusGateway petStatusGateway;

    public BuscarPetStatusPorUsuarioEStatusUseCase(PetStatusGateway petStatusGateway) {
        this.petStatusGateway = petStatusGateway;
    }

    public List<PetStatus> executar(UUID userId, PetStatusEnum status) {
        return petStatusGateway.buscarPorUsuarioEStatus(userId, status);
    }
}
