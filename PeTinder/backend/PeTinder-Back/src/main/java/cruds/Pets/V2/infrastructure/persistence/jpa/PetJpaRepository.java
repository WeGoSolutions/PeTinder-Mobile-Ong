package cruds.Pets.V2.infrastructure.persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetJpaRepository extends JpaRepository<PetEntity, UUID> {

    List<PetEntity> findByOngIdOrderByCurtidasDesc(UUID ongId);

    List<PetEntity> findByOngId(UUID ongId);
    
    Page<PetEntity> findByOngId(UUID ongId, Pageable pageable);

    @Query("SELECT DISTINCT p FROM PetEntity p LEFT JOIN FETCH p.tags WHERE p.ongId = :ongId")
    Page<PetEntity> findByOngIdWithTags(@Param("ongId") UUID ongId, Pageable pageable);

    @Query("SELECT p FROM PetEntity p WHERE p.isAdotado = false OR p.isAdotado IS NULL")
    List<PetEntity> findByIsAdotadoFalseOrIsAdotadoIsNull();

    void deleteByOngId(UUID ongId);
}