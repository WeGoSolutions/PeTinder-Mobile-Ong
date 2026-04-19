package cruds.Dashboard.V2.infrastructure.web.dto;

import cruds.Dashboard.V2.core.domain.PetDashboard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetRankingResponseWebDTO {
    private UUID id;
    private String nome;
    private String descricao;
    private Double idade;
    private String porte;
    private String sexo;
    private Integer curtidas;

    public static PetRankingResponseWebDTO fromDomain(PetDashboard pet) {
        return new PetRankingResponseWebDTO(
            pet.getId(),
            pet.getNome(),
            pet.getDescricao(),
            pet.getIdade(),
            pet.getPorte(),
            pet.getSexo(),
            pet.getCurtidas()
        );
    }
}
