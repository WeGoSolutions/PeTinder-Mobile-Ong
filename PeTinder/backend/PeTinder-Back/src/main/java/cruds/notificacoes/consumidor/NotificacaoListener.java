package cruds.notificacoes.consumidor;

import cruds.notificacoes.dto.NotificacaoDTO;
import cruds.notificacoes.storage.NotificacaoMemoriaStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoListener {

    private final NotificacaoMemoriaStorage notificacaoStorage;
    private final ConnectionFactory connectionFactory;
    private final Jackson2JsonMessageConverter messageConverter;

    private final Map<UUID, SimpleMessageListenerContainer> listenersAtivos = new ConcurrentHashMap<>();

    public void criarListenerParaUsuario(UUID userId) {
        if (listenersAtivos.containsKey(userId)) {
            return;
        }

        String nomeFila = gerarNomeFila(userId);

        try {
            SimpleMessageListenerContainer container = criarContainerMensagens(nomeFila, userId);

            container.afterPropertiesSet();
            container.start();

            listenersAtivos.put(userId, container);

        } catch (Exception e) {
            log.error("❌ Erro ao criar listener para usuário {}: {}", userId, e.getMessage(), e);
        }
    }

    private SimpleMessageListenerContainer criarContainerMensagens(String nomeFila, UUID userId) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(nomeFila);
        container.setConcurrentConsumers(1);
        container.setPrefetchCount(10);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);

        container.setMessageListener((Message message) -> processarMensagem(message, userId));

        return container;
    }

    private void processarMensagem(Message message, UUID userId) {
        try {
            NotificacaoDTO notificacao = (NotificacaoDTO) messageConverter.fromMessage(message);

            notificacao.setUserId(userId);
            notificacaoStorage.adicionarNotificacao(notificacao);

        } catch (Exception e) {
            log.error("❌ Erro ao processar mensagem: {}", e.getMessage(), e);
        }
    }

    private String gerarNomeFila(UUID userId) {
        return "fila.usuario." + userId;
    }
}