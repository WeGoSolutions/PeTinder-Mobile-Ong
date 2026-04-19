package cruds.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class MockEmailService implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(MockEmailService.class);

    @Async
    public void enviarEmail(String destinatario, String assunto, String mensagem) {
        logger.info("=== MOCK EMAIL SERVICE ===");
        logger.info("Destinatário: {}", destinatario);
        logger.info("Assunto: {}", assunto);
        logger.info("Mensagem: {}", mensagem);
        logger.info("Email simulado enviado com sucesso!");
        logger.info("=========================");
    }
}
