package cruds.Pets.V2.infrastructure.web.dto;

import cruds.Pets.V2.core.domain.PetStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PetStatusRequestWebDTO {
    @NotNull
    private UUID petId;

    @NotNull
    private UUID userId;

    @NotNull
    private PetStatusEnum status;

    private Integer curtidas;

    private LocalDateTime alteradoParaPending = LocalDateTime.now();

    public LocalDateTime getAlteradoParaPending() {
        return alteradoParaPending;
    }
}
