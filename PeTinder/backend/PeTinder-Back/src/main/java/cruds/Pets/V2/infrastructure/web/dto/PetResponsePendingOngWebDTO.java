package cruds.Pets.V2.infrastructure.web.dto;

import cruds.Ong.V2.infrastructure.web.dto.OngResponseWebDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetResponsePendingOngWebDTO {
    private UUID userId;
    private UUID petId;
    private String petNome;
    private Double idade;
    private String porte;
    private String descricao;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private List<String> imageUrl;
    private String sexo;
    private UUID ongId;
    private OngResponseWebDTO ongInfo;
}
