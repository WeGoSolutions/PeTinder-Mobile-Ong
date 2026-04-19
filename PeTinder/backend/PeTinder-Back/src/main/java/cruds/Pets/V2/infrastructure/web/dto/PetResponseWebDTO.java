package cruds.Pets.V2.infrastructure.web.dto;

import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable(value = "pets", key = "#id")
public class PetResponseWebDTO {

    private UUID id;
    private String nome;
    private Double idade;
    private String porte;
    private Integer curtidas;
    private List<String> tags;
    private String descricao;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private Boolean isAdotado;
    private String sexo;
    private UUID ongId;
    private LocalDateTime dataCriacao;
    private List<String> imagensUrls;
    private PetStatusEnum status;
    private Integer totalImagens;

    public static PetResponseWebDTO fromDomain(Pet pet) {
        List<String> imagensUrls = null;
        Integer totalImagens = 0;

        if (pet.getImagens() != null && !pet.getImagens().isEmpty()) {
            totalImagens = pet.getImagens().size();

            imagensUrls = pet.getImagens().stream()
                    .filter(imagem -> imagem != null && imagem.temDados())
                    .map(imagem -> {
                        String base64Image = Base64.getEncoder().encodeToString(imagem.getDados());
                        return "data:image/jpeg;base64," + base64Image;
                    })
                    .toList();
        }
        
        return PetResponseWebDTO.builder()
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
                .dataCriacao(pet.getDataCriacao())
                .imagensUrls(imagensUrls)
                .totalImagens(totalImagens)
                .build();
    }

    public static PetResponseWebDTO fromDomain(Pet pet, PetStatusEnum status) {
        PetResponseWebDTO dto = fromDomain(pet);
        dto.setStatus(status);
        return dto;
    }
}