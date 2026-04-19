package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.application.command.AtualizarSenhaCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarSenhaWebDTO {

    @Schema(description = "Senha atual do usuário", example = "Urubu@123")
    private String senhaAtual;

    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 8, message = "Nova senha deve ter pelo menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$", 
             message = "Nova senha deve conter pelo menos: uma letra maiúscula, uma minúscula, um número e um caractere especial")
    @Schema(description = "Nova senha do usuário", example = "NovaUrubu@456")
    private String novaSenha;

    public AtualizarSenhaCommand toCommand(String email) {
        return new AtualizarSenhaCommand(email, senhaAtual, novaSenha);
    }
}
