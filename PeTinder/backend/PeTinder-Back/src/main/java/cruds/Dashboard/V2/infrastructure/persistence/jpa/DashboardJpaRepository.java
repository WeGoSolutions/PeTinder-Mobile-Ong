package cruds.Dashboard.V2.infrastructure.persistence.jpa;

import cruds.Dashboard.V2.infrastructure.persistence.jpa.DashboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DashboardJpaRepository extends JpaRepository<DashboardEntity, UUID> {
    void deleteByOngId(UUID id);

    Optional<DashboardEntity> findByOngId(UUID ongId);

    boolean existsByOngId(UUID ongId);
}
