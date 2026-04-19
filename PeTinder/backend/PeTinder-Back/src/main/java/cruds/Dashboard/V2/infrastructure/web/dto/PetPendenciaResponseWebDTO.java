package cruds.Dashboard.V2.infrastructure.web.dto;

import cruds.Dashboard.V2.core.domain.PetDashboard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetPendenciaResponseWebDTO {
    private String nome;
    private List<String> faltas;
    private UUID id;
    private String imageUrl;

    public static PetPendenciaResponseWebDTO fromDomain(PetDashboard pet, String imagemBase64) {
        return new PetPendenciaResponseWebDTO(
            pet.getNome(),
            pet.obterPendencias(),
            pet.getId(),
                imagemBase64
        );
    }
}
