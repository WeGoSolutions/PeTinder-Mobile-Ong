package cruds.Dashboard.V2.core.adapter;

import cruds.Dashboard.V2.core.domain.PetDashboard;

import java.util.List;
import java.util.UUID;

public interface PetDashboardGateway {

    List<PetDashboard> listarPetsPorOngIdOrderByCurtidas(UUID ongId);

    List<PetDashboard> listarPetsPorOngId(UUID ongId);

    long contarPetsAdotadosPorOngId(UUID ongId);

    long contarPetsNaoAdotadosPorOngId(UUID ongId);
}
