package cruds.Dashboard.V2.infrastructure.persistence.jpa.mapper;

import cruds.Dashboard.V2.core.domain.Dashboard;
import cruds.Dashboard.V2.infrastructure.persistence.jpa.DashboardEntity;

import java.util.UUID;

public class DashboardMapper {

    public static DashboardEntity toEntity(Dashboard domain) {
        if (domain == null) return null;

        return DashboardEntity.builder()
                .id(domain.getId())
                .ongId(domain.getOngId())
                .build();
    }

    public static Dashboard toDomain(DashboardEntity entity) {
        if (entity == null) return null;

        UUID ongId = entity.getOngId();

        Dashboard dashboard = new Dashboard(entity.getId(), ongId);
        return dashboard;
    }
}
