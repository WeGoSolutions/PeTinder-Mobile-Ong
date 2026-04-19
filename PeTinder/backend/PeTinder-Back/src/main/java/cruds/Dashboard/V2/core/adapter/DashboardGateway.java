package cruds.Dashboard.V2.core.adapter;

import cruds.Dashboard.V2.core.domain.Dashboard;

import java.util.Optional;
import java.util.UUID;

public interface DashboardGateway {

    Dashboard salvar(Dashboard dashboard);

    Optional<Dashboard> buscarPorId(UUID id);

    Optional<Dashboard> buscarPorOngId(UUID ongId);

    void removerPorOngId(UUID ongId);

    boolean existePorOngId(UUID ongId);
}

