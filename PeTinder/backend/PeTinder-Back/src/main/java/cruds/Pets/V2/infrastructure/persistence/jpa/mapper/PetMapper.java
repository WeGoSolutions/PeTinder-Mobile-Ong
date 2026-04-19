package cruds.Pets.V2.infrastructure.persistence.jpa.mapper;

import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetEntity;

public class PetMapper {

    public static PetEntity toEntity(Pet pet) {
        if (pet == null) return null;

        return PetEntity.builder()
                .id(pet.getId())
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .porte(pet.getPorte())
                .curtidas(pet.getCurtidas())
                .tags(pet.getTags())
                .descricao(pet.getDescricao())
                .isCastrado(pet.getIsCastrado())
                .isVermifugo(pet.getIsVermifugo())
                .isVacinado(pet.getIsVacinado())
                .isAdotado(pet.getIsAdotado())
                .sexo(pet.getSexo())
                .ongId(pet.getOngId())
                .build();
    }

    public static Pet toDomain(PetEntity entity) {
        if (entity == null) return null;

        Pet pet = new Pet(
                entity.getId(),
                entity.getNome(),
                entity.getIdade(),
                entity.getPorte(),
                entity.getTags(),
                entity.getDescricao(),
                entity.getIsCastrado(),
                entity.getIsVermifugo(),
                entity.getIsVacinado(),
                entity.getSexo(),
                entity.getOngId()
        );

        pet.setCurtidas(entity.getCurtidas());
        pet.setIsAdotado(entity.getIsAdotado());

        return pet;
    }

    // Conversão da entidade V1 para V2
//    public static Pet toDomain(cruds.Pets.entity.Pet v1Pet) {
//        if (v1Pet == null) return null;
//
//        Pet pet = new Pet(
//                v1Pet.getId(),
//                v1Pet.getNome(),
//                v1Pet.getIdade(),
//                v1Pet.getPorte(),
//                v1Pet.getTags(),
//                v1Pet.getDescricao(),
//                v1Pet.getIsCastrado(),
//                v1Pet.getIsVermifugo(),
//                v1Pet.getIsVacinado(),
//                v1Pet.getSexo(),
//                v1Pet.getOng() != null ? v1Pet.getOng().getId() : null
//        );
//
//        pet.setCurtidas(v1Pet.getCurtidas());
//        pet.setIsAdotado(v1Pet.getIsAdopted());
//
//        return pet;
//    }
//
//    // Conversão de V2 para V1 (se necessário para compatibilidade)
//    public static PetEntity toV2Entity(cruds.Pets.entity.Pet v1Pet) {
//        if (v1Pet == null) return null;
//
//        return PetEntity.builder()
//                .id(v1Pet.getId())
//                .nome(v1Pet.getNome())
//                .idade(v1Pet.getIdade())
//                .porte(v1Pet.getPorte())
//                .curtidas(v1Pet.getCurtidas())
//                .tags(v1Pet.getTags())
//                .descricao(v1Pet.getDescricao())
//                .isCastrado(v1Pet.getIsCastrado())
//                .isVermifugo(v1Pet.getIsVermifugo())
//                .isVacinado(v1Pet.getIsVacinado())
//                .isAdotado(v1Pet.getIsAdopted())
//                .sexo(v1Pet.getSexo())
//                .ongId(v1Pet.getOng() != null ? v1Pet.getOng().getId() : null)
//                .build();
//    }
}