package cruds.notificacoes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificacaoDTO {
    private UUID notifyId;
    private UUID userId;
    private String notifyType;
    private String title;
    private String description;
    private boolean viewed;
    private LocalDateTime createdAt;

    public NotificacaoDTO(UUID userId, String notifyType, String title, String description) {
        this.notifyId = UUID.randomUUID();
        this.userId = userId;
        this.notifyType = notifyType;
        this.title = title;
        this.description = description;
        this.viewed = false;
        this.createdAt = LocalDateTime.now();
    }
}