package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;

import java.util.UUID;

public class RemoverPetUseCase {

    private final PetGateway petGateway;

    public RemoverPetUseCase(PetGateway petGateway) {
        this.petGateway = petGateway;
    }

    public void remover(UUID id) {
        if (!petGateway.existePorId(id)) {
            throw new PetException.PetNaoEncontradoException(
                    "Pet com ID " + id + " não encontrado"
            );
        }

        petGateway.remover(id);
    }
}