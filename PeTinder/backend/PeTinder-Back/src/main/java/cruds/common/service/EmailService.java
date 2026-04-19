package cruds.common.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

@Service
@Profile("!dev")
public class EmailService implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from.address}")
    private String fromAddress;

    @Value("${app.email.from.name}")
    private String fromName;

    @Async
    public void enviarEmail(String destinatario, String assunto, String mensagem) {
        try {
            logger.info("Tentando enviar email para: {} de: {}", destinatario, fromAddress);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagem, true);
            helper.setFrom(fromAddress, fromName);

            mailSender.send(mimeMessage);
            logger.info("Email enviado com sucesso para: {} de: {}", destinatario, fromAddress);

        } catch (jakarta.mail.MessagingException e) {
            logger.error("Erro ao enviar email para {}: {}", destinatario, e.getMessage());
            logger.warn("Email não foi enviado, mas continuando execução...");
        } catch (Exception e) {
            logger.error("Erro inesperado ao enviar email para {}: {}", destinatario, e.getMessage());
            logger.warn("Email não foi enviado, mas continuando execução...");
        }
    }
}
