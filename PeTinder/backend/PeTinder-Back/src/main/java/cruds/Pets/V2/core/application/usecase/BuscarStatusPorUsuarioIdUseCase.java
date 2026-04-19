package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.domain.PetStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarStatusPorUsuarioIdUseCase {

    private final PetStatusGateway petStatusGateway;

    public BuscarStatusPorUsuarioIdUseCase(PetStatusGateway petStatusGateway) {this.petStatusGateway = petStatusGateway; }

    public List<PetStatus> buscar(UUID userId){
        return petStatusGateway.buscarPorUsuario(userId);
    }

}
