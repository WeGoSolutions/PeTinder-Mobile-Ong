package cruds.notificacoes.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final RabbitProperties rabbitProperties;
    @Bean
    public ConnectionFactory connectionFactory() {
        String host = rabbitProperties.getHost();
        int port = rabbitProperties.getPort();
        CachingConnectionFactory cf = new CachingConnectionFactory(host, port);
        cf.setUsername(rabbitProperties.getUsername());
        cf.setPassword(rabbitProperties.getPassword());
        if (rabbitProperties.getVirtualHost() != null) {
            cf.setVirtualHost(rabbitProperties.getVirtualHost());
        }
        return cf;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public Queue filaNotificacoesGeral() {
        return new Queue("fila.notificacoes.geral", true);
    }
}