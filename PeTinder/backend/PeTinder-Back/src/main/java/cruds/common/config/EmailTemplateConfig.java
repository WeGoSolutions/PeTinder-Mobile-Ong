package cruds.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailTemplateConfig {

    @Value("${app.email.from.name}")
    private String appName;

    public String getWelcomeEmailTemplate(String nomeUsuario) {
        return """
                <div style="font-family: Arial, sans-serif; background-color: #fefefe; padding: 20px; border-radius: 10px; border: 1px solid #ddd;">
                    <h1 style="color: #ff6f61;">🐾 Bem-vindo ao PeTinder, %s!</h1>
            
                    <p style="font-size: 16px; color: #333;">
                        Estamos super felizes por ter você com a gente! <br>
                        Aqui no <strong>PeTinder</strong>, acreditamos que todo pet merece um lar cheio de amor, e toda pessoa merece um pet que mude sua vida. 💕
                    </p>
            
                    <p style="font-size: 16px; color: #333;">
                        Prepare-se para conhecer novos amigos peludos, descobrir histórias emocionantes e, quem sabe, encontrar seu novo companheiro de quatro patas.
                    </p>
            
                    <p style="font-size: 14px; color: #666;">Com carinho,<br>%s 🐶🐱</p>
                </div>
                """.formatted(nomeUsuario, appName);
    }

    public String getWelcomeEmailSubject(String nomeUsuario) {
        return "Bem-vindo ao PeTinder, %s!".formatted(nomeUsuario);
    }

    public String getLoginNotificationTemplate(String nomeUsuario, String dataHora) {
        return """
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
                    %s 🐶🐱
                  </p>
                </div>
                """.formatted(nomeUsuario, dataHora, appName);
    }

    public String getLoginNotificationSubject(String nomeUsuario) {
        return "🔒 Novo login no PeTinder, %s!".formatted(nomeUsuario);
    }
}
