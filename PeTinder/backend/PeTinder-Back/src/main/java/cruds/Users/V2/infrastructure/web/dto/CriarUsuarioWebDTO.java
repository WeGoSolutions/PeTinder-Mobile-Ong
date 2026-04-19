package cruds.Users.V2.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import cruds.Users.V2.core.application.command.CriarUsuarioCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriarUsuarioWebDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, message = "Nome deve ter pelo menos 3 caracteres")
    @Pattern(regexp = ("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$"), message = "Nome deve conter apenas letras e espaços")
    @Schema(description = "Nome do usuário", example = "Petinder")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    @Schema(description = "Email do usuário", example = "petinder@gmail.com")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$", 
             message = "Senha deve conter pelo menos: uma letra maiúscula, uma minúscula, um número e um caractere especial")
    @Schema(description = "Senha do usuário", example = "Urubu@123")
    private String senha;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("dataNasc")
    @JsonAlias({"dataNascimento"})
    @Schema(description = "Data de nascimento do usuário", example = "2000-01-01")
    private LocalDate dataNascimento;

    public CriarUsuarioCommand toCommand() {
        return new CriarUsuarioCommand(nome, email, senha, dataNascimento);
    }
}
