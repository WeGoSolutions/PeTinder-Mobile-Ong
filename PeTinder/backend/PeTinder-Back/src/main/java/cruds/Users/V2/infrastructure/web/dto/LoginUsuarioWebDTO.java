package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.application.command.LoginUsuarioCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUsuarioWebDTO {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    @Schema(description = "Email do usuário", example = "petinder@gmail.com")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "Urubu@123")
    private String senha;

    public LoginUsuarioCommand toCommand() {
        return new LoginUsuarioCommand(email, senha);
    }
}
