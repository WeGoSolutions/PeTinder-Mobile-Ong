package cruds.Ong.V2.infrastructure.gateway;

import cruds.Ong.V2.core.adapter.PetOngGateway;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PetOngGatewayImpl implements PetOngGateway {

    private final ImagemPetJpaRepository imagemPetRepository;
    private final PetJpaRepository petRepository;
    private final PetStatusJpaRepository petStatusRepository;

    public PetOngGatewayImpl(PetJpaRepository petRepository, PetStatusJpaRepository petStatusRepository, ImagemPetJpaRepository imagemPetRepository) {
        this.petRepository = petRepository;
        this.petStatusRepository = petStatusRepository;
        this.imagemPetRepository = imagemPetRepository;
    }

    @Override
    public Page<PetOngInfo> listarPetsPorOng(UUID ongId, Pageable pageable) {
        Page<PetEntity> petsPage = petRepository.findByOngIdWithTags(ongId, pageable);

        return petsPage.map(pet -> {
            List<String> statusList = petStatusRepository.findByPetId(pet.getId())
                    .stream()
                    .map(status -> status.getStatus().name())
                    .collect(Collectors.toList());

            // Gerar URLs das imagens
            String baseUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();

            // Buscar as keys S3 das imagens do pet e gerar URLs
            List<String> imageKeys = imagemPetRepository.findKeysByPetId(pet.getId());
            List<String> imageUrls = IntStream.range(0, imageKeys.size())
                    .mapToObj(i -> baseUri + "/api/pets/" + pet.getId() + "/imagens/" + i)
                    .collect(Collectors.toList());

            return new PetOngInfo(
                    ongId,
                    pet.getId(),
                    pet.getNome(),
                    pet.getIdade(),
                    pet.getPorte(),
                    pet.getCurtidas(),
                    pet.getTags(),
                    pet.getDescricao(),
                    pet.getIsCastrado(),
                    pet.getIsVermifugo(),
                    pet.getIsVacinado(),
                    imageUrls,
                    pet.getSexo(),
                    statusList
            );
        });
    }

    @Override
    public void removerPetsPorOng(UUID ongId) {
        petRepository.deleteByOngId(ongId);
    }
}