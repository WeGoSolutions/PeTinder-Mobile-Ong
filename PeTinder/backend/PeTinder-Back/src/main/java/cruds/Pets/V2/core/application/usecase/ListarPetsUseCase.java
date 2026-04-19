package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.domain.Pet;

import java.util.List;
import java.util.UUID;

public class ListarPetsUseCase {

    private final PetGateway petGateway;

    public ListarPetsUseCase(PetGateway petGateway) {
        this.petGateway = petGateway;
    }

    public List<Pet> listarTodos() {
        return petGateway.listarTodos();
    }

    public List<Pet> listarPorOng(UUID ongId) {
        return petGateway.listarPorOng(ongId);
    }

    public List<Pet> listarDisponiveis() {
        return petGateway.listarDisponiveis();
    }
}