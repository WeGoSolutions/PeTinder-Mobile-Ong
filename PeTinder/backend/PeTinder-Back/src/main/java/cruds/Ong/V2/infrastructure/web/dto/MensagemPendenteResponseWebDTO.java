package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.adapter.MensagemPendenteGateway;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensagemPendenteResponseWebDTO {

    private UUID petId;
    private String petNome;
    private UUID userId;
    private String userName;
    private String userEmail;
    private String imageUrl;
    private LocalDateTime dataStatus;

    public static MensagemPendenteResponseWebDTO fromMensagem(
            MensagemPendenteGateway.MensagemPendente mensagem) {

        String auxiliar = mensagem.getUserImage() != null
                ? "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(mensagem.getUserImage())
                : null;

        return MensagemPendenteResponseWebDTO.builder()
            .petId(mensagem.getPetId())
            .petNome(mensagem.getPetNome())
            .userId(mensagem.getUserId())
            .userName(mensagem.getUserName())
            .userEmail(mensagem.getUserEmail())
                .imageUrl(auxiliar)
            .dataStatus(mensagem.getDataStatus())
            .build();
    }
}

