package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;

import java.util.UUID;

public class AdotarPetUseCase {

    private final PetGateway petGateway;

    public AdotarPetUseCase(PetGateway petGateway) {
        this.petGateway = petGateway;
    }

    public Pet adotar(UUID petId) {
        Pet pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + petId + " não encontrado"
                ));

        if (pet.getIsAdotado()) {
            throw new PetException.PetJaAdotadoException(
                    "Pet já foi adotado"
            );
        }

        pet.adotar();
        return petGateway.atualizar(pet);
    }

    public Pet cancelarAdocao(UUID petId) {
        Pet pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + petId + " não encontrado"
                ));

        pet.cancelarAdocao();
        return petGateway.atualizar(pet);
    }
}