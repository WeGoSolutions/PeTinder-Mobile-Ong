package cruds.Pets.V2.infrastructure.web.dto;

import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseGeralWebDTO {
    private UUID id;
    private String nome;
    private Double idade;
    private String porte;
    private Integer curtidas;
    private String descricao;
    private List<String> tags;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private List<String> imagens;
    private String sexo;
    private UUID ongId;
    private String nomeOng;
    private String linkOng;
    private EnderecoResponseWebDTO endereco;
    private PetStatusEnum status;

    public static PetResponseGeralWebDTO fromDomain(Pet pet, UUID ongId, String nomeOng, String linkOng, EnderecoResponseWebDTO endereco) {
        String baseUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        List<String> imagemUrls = null;
        if (pet.getImagens() != null && !pet.getImagens().isEmpty()) {
            imagemUrls = IntStream.range(0, pet.getImagens().size())
                    .mapToObj(i -> baseUri + "/api/pets/" + pet.getId() + "/imagens/" + i)
                    .toList();
        }

        return PetResponseGeralWebDTO.builder()
                .id(pet.getId())
                .nome(pet.getNome())
                .idade(pet.getIdade())
                .porte(pet.getPorte())
                .curtidas(pet.getCurtidas())
                .descricao(pet.getDescricao())
                .tags(pet.getTags())
                .isCastrado(pet.getIsCastrado())
                .isVermifugo(pet.getIsVermifugo())
                .isVacinado(pet.getIsVacinado())
                .imagens(imagemUrls)
                .sexo(pet.getSexo())
                .ongId(ongId)
                .nomeOng(nomeOng)
                .linkOng(linkOng)
                .endereco(endereco)
                .build();
    }

    public static PetResponseGeralWebDTO fromDomain(Pet pet, UUID ongId, String nomeOng, String linkOng, EnderecoResponseWebDTO endereco, PetStatusEnum status) {
        PetResponseGeralWebDTO dto = fromDomain(pet, ongId, nomeOng, linkOng, endereco);
        dto.setStatus(status);
        return dto;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnderecoResponseWebDTO {
        private String cep;
        private String rua;
        private String numero;
        private String cidade;
        private String uf;
        private String complemento;
    }
}
