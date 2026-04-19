package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.adapter.PetOngGateway;
import cruds.Ong.V2.core.application.exception.OngException;

import java.util.UUID;

public class RemoverOngUseCase {

    private final OngGateway ongGateway;
    private final PetOngGateway petOngGateway;

    public RemoverOngUseCase(OngGateway ongGateway, PetOngGateway petOngGateway) {
        this.ongGateway = ongGateway;
        this.petOngGateway = petOngGateway;
    }

    public void remover(UUID id) {
        if (!ongGateway.existePorId(id)) {
            throw new OngException.OngNaoEncontradaException(
                "ONG não encontrada com id: " + id
            );
        }

        // Remove pets associados antes de remover a ONG
        petOngGateway.removerPetsPorOng(id);

        ongGateway.remover(id);
    }
}

