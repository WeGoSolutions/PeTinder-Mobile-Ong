package cruds.Pets.V2.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurtirPetWebDTO {

    @Schema(description = "ID do usuário que está curtindo", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(description = "Ação de curtir (true) ou descurtir (false)", example = "true")
    private Boolean curtir;
}