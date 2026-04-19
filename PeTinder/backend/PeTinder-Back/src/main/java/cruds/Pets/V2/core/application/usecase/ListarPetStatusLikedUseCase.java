package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.domain.PetStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ListarPetStatusLikedUseCase {

    private final PetStatusGateway petStatusGateway;

    public ListarPetStatusLikedUseCase(PetStatusGateway petStatusGateway) {
        this.petStatusGateway = petStatusGateway;
    }

    public List<PetStatus> listarTodos() {
        return petStatusGateway.buscarTodosLiked();
    }

    public List<PetStatus> listarPorUsuario(UUID userId) {
        return petStatusGateway.buscarLikedPorUsuario(userId);
    }
}
