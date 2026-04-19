package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.application.command.LoginOngCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginOngWebDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    public LoginOngCommand toCommand() {
        return new LoginOngCommand(email, senha);
    }
}
