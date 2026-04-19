package cruds.Dashboard.V2.core.application.usecase;

import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;
import cruds.Dashboard.V2.core.application.exception.DashboardException;
import cruds.Dashboard.V2.core.domain.PetDashboard;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListarPendenciasPetsUseCase {

    private final PetDashboardGateway petDashboardGateway;

    public ListarPendenciasPetsUseCase(PetDashboardGateway petDashboardGateway) {
        this.petDashboardGateway = petDashboardGateway;
    }

    public List<PetDashboard> listarPendencias(UUID ongId) {
        List<PetDashboard> pets = petDashboardGateway.listarPetsPorOngId(ongId);

        if (pets.isEmpty()) {
            throw new DashboardException.NenhumPetEncontradoException(
                "Nenhum pet encontrado para a ONG com ID: " + ongId
            );
        }

        return pets.stream()
                .filter(PetDashboard::temPendencias)
                .collect(Collectors.toList());
    }
}

