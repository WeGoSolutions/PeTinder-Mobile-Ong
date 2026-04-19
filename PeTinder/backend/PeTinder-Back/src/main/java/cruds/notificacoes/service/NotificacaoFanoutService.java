package cruds.notificacoes.service;

import cruds.notificacoes.consumidor.NotificacaoListener;
import cruds.notificacoes.dto.NotificacaoDTO;
import cruds.Pets.V2.infrastructure.web.service.PetStatusQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoFanoutService {

    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private final PetStatusQueryService petStatusQueryService;
    private final NotificacaoListener notificacaoListener;

    private final Set<String> exchangesCriados = ConcurrentHashMap.newKeySet();
    private final Set<String> bindingsCriados = ConcurrentHashMap.newKeySet();

    public void inscreverUsuarioNoPet(UUID petId, UUID userId) {
        validarParametros(petId, userId);

        String nomeFila = gerarNomeFila(userId);
        String nomeExchange = gerarNomeExchange(petId);
        String chaveBinding = gerarChaveBinding(nomeFila, nomeExchange);

        try {
            criarExchangeSeNaoExistir(nomeExchange);
            criarFilaSeNaoExistir(nomeFila);
            criarBindingSeNaoExistir(nomeFila, nomeExchange, chaveBinding);

            notificacaoListener.criarListenerParaUsuario(userId);

        } catch (Exception e) {
            log.error("Erro ao configurar notificações do usuário (RabbitMQ indisponível?) - petId={}, userId={}. Error: {}",
                    petId, userId, e.getMessage());
        }
    }

    public void notificarUsuarioSelecionado(UUID petId, UUID userId) {
        String nomePet = petStatusQueryService.getPetNomeById(petId);
        String nomeFila = gerarNomeFila(userId);

        NotificacaoDTO notificacao = new NotificacaoDTO(
                userId,
                "ADOPTED",
                "Parabéns!",
                "Adoção confirmada! " + nomePet + " ganhou um novo lar."
        );

        try {
            rabbitTemplate.convertAndSend(nomeFila, notificacao);
        } catch (Exception e) {
            log.warn("Falha ao enviar notificação de usuário selecionado para fila {}: {}", nomeFila, e.getMessage());
        }

        desconectarUsuarioDoPet(petId, userId);
    }

    public void notificarDemaisInteressados(UUID petId) {
        String nomePet = petStatusQueryService.getPetNomeById(petId);
        String nomeExchange = gerarNomeExchange(petId);

        NotificacaoDTO notificacao = new NotificacaoDTO(
                UUID.randomUUID(), // ID temporário, será substituído pelo listener
                "NOTADOPTED",
                "Ah, que pena!",
                "O pet " + nomePet + " já foi adotado por outra pessoa. Continue procurando, o seu amigo perfeito está te esperando!"
        );

        try {
            rabbitTemplate.convertAndSend(nomeExchange, "", notificacao);
        } catch (Exception e) {
            log.warn("Falha ao enviar notificação para exchange {}: {}", nomeExchange, e.getMessage());
        }
    }

    public void desconectarUsuarioDoPet(UUID petId, UUID userId) {
        String nomeFila = gerarNomeFila(userId);
        String nomeExchange = gerarNomeExchange(petId);
        String chaveBinding = gerarChaveBinding(nomeFila, nomeExchange);

        try {
            Queue fila = new Queue(nomeFila);
            FanoutExchange exchange = new FanoutExchange(nomeExchange);
            Binding binding = BindingBuilder.bind(fila).to(exchange);

            amqpAdmin.removeBinding(binding);
            bindingsCriados.remove(chaveBinding);

        } catch (Exception e) {
            log.warn("⚠️ Erro ao remover binding para usuário {} e pet {}: {}",
                    userId, petId, e.getMessage());
        }
    }

    private void criarExchangeSeNaoExistir(String nomeExchange) {
        if (!exchangesCriados.contains(nomeExchange)) {
            FanoutExchange exchange = new FanoutExchange(nomeExchange, true, false);
            amqpAdmin.declareExchange(exchange);
            exchangesCriados.add(nomeExchange);
            log.info("Exchange criada: {}", nomeExchange);
        }
    }

    private void criarFilaSeNaoExistir(String nomeFila) {
        Queue fila = new Queue(nomeFila, true, false, false);
        amqpAdmin.declareQueue(fila);
        log.info("Fila criada: {}", nomeFila);
    }

    private void criarBindingSeNaoExistir(String nomeFila, String nomeExchange, String chaveBinding) {
        if (!bindingsCriados.contains(chaveBinding)) {
            Queue fila = new Queue(nomeFila);
            FanoutExchange exchange = new FanoutExchange(nomeExchange);
            Binding binding = BindingBuilder.bind(fila).to(exchange);
            amqpAdmin.declareBinding(binding);
            bindingsCriados.add(chaveBinding);
            log.info("Binding criado entre fila {} e exchange {}", nomeFila, nomeExchange);
        }
    }

    private void validarParametros(UUID petId, UUID userId) {
        if (petId == null || userId == null) {
            throw new IllegalArgumentException("PetId e UserId não podem ser nulos");
        }
    }

    private String gerarNomeFila(UUID userId) {
        return "fila.usuario." + userId;
    }

    private String gerarNomeExchange(UUID petId) {
        return "fanoutExchange.pet." + petId;
    }

    private String gerarChaveBinding(String nomeFila, String nomeExchange) {
        return nomeFila + ":" + nomeExchange;
    }
}