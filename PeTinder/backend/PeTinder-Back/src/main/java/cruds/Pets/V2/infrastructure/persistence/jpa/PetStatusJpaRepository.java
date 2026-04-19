package cruds.Pets.V2.infrastructure.persistence.jpa;

import cruds.Pets.V2.core.domain.PetStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PetStatusJpaRepository extends JpaRepository<PetStatusEntity, UUID> {
    
    Optional<PetStatusEntity> findByPetIdAndUserId(UUID petId, UUID userId);
    
    List<PetStatusEntity> findByUserId(UUID userId);
    
    List<PetStatusEntity> findByPetId(UUID petId);
    
    List<PetStatusEntity> findByUserIdAndStatus(UUID userId, PetStatusEnum status);
    
    List<PetStatusEntity> findByStatus(PetStatusEnum status);
    
    @Query("SELECT ps FROM PetStatusEntity ps WHERE ps.status = 'LIKED'")
    List<PetStatusEntity> findAllLikedStatusPets();
    
    @Query("SELECT ps FROM PetStatusEntity ps WHERE ps.userId = :userId AND ps.status = 'LIKED'")
    List<PetStatusEntity> findLikedStatusPetsByUserId(@Param("userId") UUID userId);
    
    List<PetStatusEntity> findByPetIdAndStatus(UUID petId, PetStatusEnum status);
    
    void deleteByPetIdAndUserId(UUID petId, UUID userId);
    
    @Modifying
    @Transactional
    void deleteByPetId(UUID petId);
    
    @Modifying
    @Transactional
    void deleteByUserId(UUID userId);
    
    @Modifying
    @Transactional
    void deleteByPetIdAndUserIdNot(UUID petId, UUID userId);
    
    boolean existsByPetIdAndUserId(UUID petId, UUID userId);
    
    @Query("SELECT DISTINCT p.id FROM PetEntity p WHERE p.id NOT IN " +
           "(SELECT ps.petId FROM PetStatusEntity ps WHERE ps.userId = :userId)")
    List<UUID> findPetsNotInteractedByUser(@Param("userId") UUID userId);
    
    @Query("SELECT p FROM PetEntity p WHERE p.id NOT IN " +
           "(SELECT ps.petId FROM PetStatusEntity ps WHERE ps.userId = :userId)")
    Page<PetEntity> findPetsNotInteractedByUserPage(@Param("userId") UUID userId, Pageable pageable);
}