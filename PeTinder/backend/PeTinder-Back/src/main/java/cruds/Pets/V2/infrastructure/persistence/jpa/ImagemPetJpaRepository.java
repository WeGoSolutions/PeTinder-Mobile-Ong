package cruds.Pets.V2.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ImagemPetJpaRepository extends JpaRepository<ImagemPetEntity, UUID> {

    List<ImagemPetEntity> findByPetIdOrderById(UUID petId);

    void deleteByPetId(UUID petId);

    @Query("SELECT i.keyS3 FROM ImagemPetEntity i WHERE i.petId = :petId")
    List<String> findKeysByPetId(@Param("petId") UUID petId);

    void deleteAllByPetId(UUID petId);

    void deleteByKeyS3(String keyS3);

    List<ImagemPetEntity> findByPetId(UUID petId);

    void deleteByPetIdAndKeyS3In(UUID petId, List<String> keys);
}
