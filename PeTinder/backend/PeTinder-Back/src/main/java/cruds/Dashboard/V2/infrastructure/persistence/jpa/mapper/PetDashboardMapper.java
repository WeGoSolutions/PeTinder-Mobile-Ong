package cruds.Dashboard.V2.infrastructure.persistence.jpa.mapper;

import cruds.Dashboard.V2.core.domain.PetDashboard;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetEntity;

public class PetDashboardMapper {

    public static PetDashboard toDomain(PetEntity entity) {
        if (entity == null) return null;



        return new PetDashboard(
            entity.getId(),
            entity.getNome(),
            entity.getDescricao(),
            entity.getIdade(),
            entity.getPorte(),
            entity.getSexo(),
            entity.getIsCastrado(),
            entity.getIsVermifugo(),
            entity.getIsVacinado(),
            entity.getIsAdotado(),
            entity.getCurtidas()
        );
    }
}
