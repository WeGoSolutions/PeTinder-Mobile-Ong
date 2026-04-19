package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.adapter.PetOngGateway;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetOngResponseWebDTO {

    private UUID ongId;
    private UUID petId;
    private String petNome;
    private Double idade;
    private String porte;
    private Integer curtidas;
    private List<String> tags;
    private String descricao;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private List<String> imageUrl;
    private String sexo;
    private List<String> status;

    public static PetOngResponseWebDTO fromPetInfo(PetOngGateway.PetOngInfo petInfo) {
        return PetOngResponseWebDTO.builder()
            .ongId(petInfo.getOngId())
            .petId(petInfo.getPetId())
            .petNome(petInfo.getPetNome())
            .idade(petInfo.getIdade())
            .porte(petInfo.getPorte())
            .curtidas(petInfo.getCurtidas())
            .tags(petInfo.getTags())
            .descricao(petInfo.getDescricao())
            .isCastrado(petInfo.getIsCastrado())
            .isVermifugo(petInfo.getIsVermifugo())
            .isVacinado(petInfo.getIsVacinado())
            .imageUrl(petInfo.getImageUrl())
            .sexo(petInfo.getSexo())
            .status(petInfo.getStatus())
            .build();
    }
}
