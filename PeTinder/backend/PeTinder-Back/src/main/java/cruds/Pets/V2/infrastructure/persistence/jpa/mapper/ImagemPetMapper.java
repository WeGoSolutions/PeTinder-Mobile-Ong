package cruds.Pets.V2.infrastructure.persistence.jpa.mapper;

import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetEntity;

import java.util.UUID;

public class ImagemPetMapper {

    public static ImagemPetEntity toEntity(ImagemPet imagem, UUID petId) {
        if (imagem == null) return null;

        return new ImagemPetEntity(
                imagem.getId(),
                imagem.getNomeArquivo(),
                petId,
                imagem.getKeyS3()
        );
    }

    public static ImagemPet toDomain(ImagemPetEntity entity) {
        if (entity == null) return null;

        return new ImagemPet(
                entity.getId(),
                entity.getNomeArquivo(),
                null,
                null
        );
    }
}
