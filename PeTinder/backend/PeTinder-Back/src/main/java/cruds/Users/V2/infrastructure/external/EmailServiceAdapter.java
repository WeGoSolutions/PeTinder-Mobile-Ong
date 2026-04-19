package cruds.Users.V2.infrastructure.external;

import cruds.Users.V2.core.adapter.EmailGateway;
import cruds.common.service.IEmailService;
import cruds.common.config.EmailTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceAdapter implements EmailGateway {

    private final IEmailService emailService;

    @Autowired
    private EmailTemplateConfig emailTemplateConfig;

    public EmailServiceAdapter(IEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void enviarEmailBoasVindas(String email, String nome) {
        emailService.enviarEmail(
            email,
            emailTemplateConfig.getWelcomeEmailSubject(nome),
            emailTemplateConfig.getWelcomeEmailTemplate(nome)
        );
    }

    @Override
    public void enviarEmail(String destinatario, String assunto, String conteudo) {
        emailService.enviarEmail(destinatario, assunto, conteudo);
    }
}
