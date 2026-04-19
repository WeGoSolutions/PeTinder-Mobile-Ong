package cruds.Pets.V2.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PetResponseUserPendenteWebDTO {
    private UUID idPet;
    private UUID idUser;
    private String nomeUser;
    private String imageUrl;
}
