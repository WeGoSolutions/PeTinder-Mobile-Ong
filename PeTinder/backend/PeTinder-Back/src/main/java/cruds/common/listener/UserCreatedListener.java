package cruds.common.listener;

import cruds.common.event.UserCreatedEvent;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class UserCreatedListener {

    private final JavaMailSender mailSender;

    @Autowired
    public UserCreatedListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        String emailDestinatario = event.getUser().getEmail();

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailDestinatario);
            helper.setSubject("🐾 Bem-vindo ao PeTinder, %s!".formatted(event.getUser().getNome()));

            String htmlMsg = """
                        <div style="font-family: Arial, sans-serif; background-color: #fefefe; padding: 20px; border-radius: 10px; border: 1px solid #ddd;">
                            <h1 style="color: #ff6f61;">🐾 Bem-vindo ao PeTinder, %s!</h1>
                    
                            <p style="font-size: 16px; color: #333;">
                                Estamos super felizes por ter você com a gente! <br>
                                Aqui no <strong>PeTinder</strong>, acreditamos que todo pet merece um lar cheio de amor, e toda pessoa merece um pet que mude sua vida. 💕
                            </p>
                    
                            <p style="font-size: 16px; color: #333;">
                                Prepare-se para conhecer novos amigos peludos, descobrir histórias emocionantes e, quem sabe, encontrar seu novo companheiro de quatro patas.
                            </p>
                    
                            <p style="font-size: 14px; color: #666;">Com carinho,<br>Equipe PeTinder 🐶🐱</p>
                        </div>
                    """.formatted(event.getUser().getNome());

            helper.setText(htmlMsg, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}