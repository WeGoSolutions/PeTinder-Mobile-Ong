package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.domain.PetStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RemoverPorUsuarioUseCase {

    private final PetStatusGateway petStatusGateway;
    private final BuscarStatusPorUsuarioIdUseCase buscarStatusPorUsuarioIdUseCase;

    public RemoverPorUsuarioUseCase(
            PetStatusGateway petStatusGateway,
            BuscarStatusPorUsuarioIdUseCase buscarStatusPorUsuarioIdUseCase
    ) {
        this.petStatusGateway = petStatusGateway;
        this.buscarStatusPorUsuarioIdUseCase = buscarStatusPorUsuarioIdUseCase;
    }

    public void removerPorId(UUID userId) {

        List<PetStatus> statusList = buscarStatusPorUsuarioIdUseCase.buscar(userId);

        if (statusList.isEmpty()) {
            return;
        }

        petStatusGateway.removerPorUsuario(userId);
    }
}
