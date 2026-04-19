package cruds.Pets.V2.infrastructure.persistence.jpa.mapper;

import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusEntity;

public class PetStatusMapper {

    public static PetStatusEntity toEntity(PetStatus status) {
        if (status == null) return null;
        
        return new PetStatusEntity(
                status.getId(),
                status.getPetId(),
                status.getUserId(),
                status.getStatus(),
                status.getAlteradoParaPending(),
                status.getDataCriacao()
        );
    }

    public static PetStatus toDomain(PetStatusEntity entity) {
        if (entity == null) return null;
        
        PetStatus status = new PetStatus(
                entity.getId(),
                entity.getPetId(),
                entity.getUserId(),
                entity.getStatus(),
                entity.getAlteradoParaPending()
        );
        
        status.setDataCriacao(entity.getDataCriacao());
        return status;
    }
}