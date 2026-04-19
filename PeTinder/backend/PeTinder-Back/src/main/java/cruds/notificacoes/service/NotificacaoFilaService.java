package cruds.notificacoes.service;

import cruds.notificacoes.dto.NotificacaoDTO;
import cruds.notificacoes.storage.NotificacaoMemoriaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoFilaService {

    private final NotificacaoMemoriaStorage notificacaoStorage;

    public boolean deletarNotificacao(UUID userId, UUID notifyId) {
        try {
            boolean removida = notificacaoStorage.removerNotificacao(userId, notifyId);

            if (removida) {
                return true;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }
}