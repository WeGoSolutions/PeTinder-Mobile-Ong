package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.PetOngGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public class ListarPetsOngUseCase {

    private final PetOngGateway petOngGateway;

    public ListarPetsOngUseCase(PetOngGateway petOngGateway) {
        this.petOngGateway = petOngGateway;
    }

    public Page<PetOngGateway.PetOngInfo> listarPets(UUID ongId, Pageable pageable) {
        return petOngGateway.listarPetsPorOng(ongId, pageable);
    }
}

