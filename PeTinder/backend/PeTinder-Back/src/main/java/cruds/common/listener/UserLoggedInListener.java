package cruds.common.listener;

import cruds.common.event.UserLoggedInEvent;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class UserLoggedInListener {

    private final JavaMailSender mailSender;

    @Autowired
    public UserLoggedInListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void handleUserLogin(UserLoggedInEvent event) {
        String emailDestinatario = event.getUser().getEmail();
        String nome = event.getUser().getNome();
        String momentoLogin = event.getLoginTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"));

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailDestinatario);
            helper.setSubject("🔒 Novo login no PeTinder, " + nome + "!");

            String htmlMsg = """
                <div style="font-family: Arial, sans-serif; background-color: #ffffff; padding: 20px; border-radius: 8px; border:1px solid #e0e0e0;">
                  <h2 style="color: #4a90e2;">🔒 Olá, %s!</h2>
                  <p style="font-size: 16px; color: #333;">
                    Detectamos um <strong>novo acesso</strong> à sua conta em <em>%s</em>.
                  </p>
                  <p style="font-size: 15px; color: #333;">
                    Se foi você, continue aproveitando o PeTinder. 😊<br>
                    Caso não reconheça este acesso, <strong>recomendamos</strong> trocar sua senha imediatamente.
                  </p>
                  <p style="font-size: 14px; color: #777;">
                    Abraços,<br>
                    Equipe PeTinder 🐶🐱
                  </p>
                </div>
                """.formatted(
                    nome,
                    momentoLogin
            );

            helper.setText(htmlMsg, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
