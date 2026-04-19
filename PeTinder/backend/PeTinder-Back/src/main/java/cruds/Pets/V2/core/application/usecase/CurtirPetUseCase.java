package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;

import java.util.UUID;

public class CurtirPetUseCase {

    private final PetGateway petGateway;

    public CurtirPetUseCase(PetGateway petGateway) {
        this.petGateway = petGateway;
    }

    public Pet curtir(UUID petId) {
        Pet pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + petId + " não encontrado"
                ));

        pet.curtir();
        return petGateway.atualizar(pet);
    }

    public Pet descurtir(UUID petId) {
        Pet pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + petId + " não encontrado"
                ));

        pet.descurtir();
        return petGateway.atualizar(pet);
    }
}