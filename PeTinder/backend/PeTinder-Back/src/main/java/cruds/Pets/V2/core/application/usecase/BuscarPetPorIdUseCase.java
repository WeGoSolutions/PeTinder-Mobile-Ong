package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public class BuscarPetPorIdUseCase {

    private final PetGateway petGateway;

    public BuscarPetPorIdUseCase(PetGateway petGateway) {
        this.petGateway = petGateway;
    }

    public Pet buscar(UUID id) {
        return petGateway.buscarPorId(id)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + id + " não encontrado"
                ));
    }
}