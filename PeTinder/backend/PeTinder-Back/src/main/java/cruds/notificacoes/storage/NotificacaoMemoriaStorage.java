package cruds.notificacoes.storage;

import cruds.notificacoes.dto.NotificacaoDTO;
import org.springframework.stereotype.Component;

import java.util.Deque;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

@Component
public class NotificacaoMemoriaStorage {
    private final Deque<NotificacaoDTO> notificacoes = new ConcurrentLinkedDeque<>();

    public void adicionarNotificacao(NotificacaoDTO notificacao) {
        notificacoes.push(notificacao);
    }

    public List<NotificacaoDTO> listarNotificacoesNaoLidas(UUID userId) {
        return notificacoes.stream()
                .filter(n -> n.getUserId() != null && n.getUserId().equals(userId))
                .filter(n -> !n.isViewed())
                .collect(Collectors.toList());
    }

    public boolean removerNotificacao(UUID userId, UUID notifyId) {
        return notificacoes.removeIf(n ->
                n.getUserId() != null &&
                        n.getUserId().equals(userId) &&
                        n.getNotifyId().equals(notifyId)
        );
    }

    public boolean marcarComoLida(UUID userId, UUID notifyId) {
        return notificacoes.stream()
                .filter(n -> n.getUserId() != null && n.getUserId().equals(userId))
                .filter(n -> n.getNotifyId().equals(notifyId))
                .peek(n -> n.setViewed(true))
                .findFirst()
                .isPresent();
    }

    public long contarNotificacoesNaoLidas(UUID userId) {
        return notificacoes.stream()
                .filter(n -> n.getUserId() != null && n.getUserId().equals(userId))
                .filter(n -> !n.isViewed())
                .count();
    }
}