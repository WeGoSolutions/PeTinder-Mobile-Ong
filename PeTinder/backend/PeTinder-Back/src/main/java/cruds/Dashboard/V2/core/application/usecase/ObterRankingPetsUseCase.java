package cruds.Dashboard.V2.core.application.usecase;

import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;
import cruds.Dashboard.V2.core.application.exception.DashboardException;
import cruds.Dashboard.V2.core.domain.PetDashboard;

import java.util.List;
import java.util.UUID;

public class ObterRankingPetsUseCase {

    private final PetDashboardGateway petDashboardGateway;

    public ObterRankingPetsUseCase(PetDashboardGateway petDashboardGateway) {
        this.petDashboardGateway = petDashboardGateway;
    }

    public List<PetDashboard> obterRanking(UUID ongId) {
        List<PetDashboard> pets = petDashboardGateway.listarPetsPorOngIdOrderByCurtidas(ongId);

        if (pets.isEmpty()) {
            throw new DashboardException.NenhumPetEncontradoException(
                "Nenhum pet encontrado para a ONG com ID: " + ongId
            );
        }

        return pets;
    }
}

