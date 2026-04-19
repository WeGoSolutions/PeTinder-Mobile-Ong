package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.application.command.AtualizarUsuarioCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarUsuarioWebDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, message = "Nome deve ter pelo menos 3 caracteres")
    @Pattern(regexp = ("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$"), message = "Nome deve conter apenas letras e espaços")
    @Schema(description = "Nome do usuário", example = "Petinder")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    @Schema(description = "Email do usuário", example = "petinder@gmail.com")
    private String email;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    @Schema(description = "Data de nascimento do usuário", example = "2000-01-01")
    private LocalDate dataNascimento;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}", message = "CPF deve ter formato válido")
    @Schema(description = "CPF do usuário", example = "123.456.789-00")
    private String cpf;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve ter formato válido (00000-000)")
    @Schema(description = "CEP", example = "01234-567")
    private String cep;

    @NotBlank(message = "Rua é obrigatória")
    @Schema(description = "Rua", example = "Rua das Flores")
    private String rua;

    @NotNull(message = "Número é obrigatório")
    @Schema(description = "Número", example = "123")
    private Integer numero;

    @Schema(description = "Complemento", example = "Apto 45")
    private String complemento;

    @NotBlank(message = "Cidade é obrigatória")
    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;

    @NotBlank(message = "UF é obrigatório")
    @Pattern(regexp = "[A-Z]{2}", message = "UF deve ter 2 caracteres")
    @Schema(description = "UF", example = "SP")
    private String uf;

    public AtualizarUsuarioCommand toCommand(UUID usuarioId) {
        return new AtualizarUsuarioCommand(
            usuarioId, nome, email, dataNascimento, cpf,
            cep, rua, numero, complemento, cidade, uf
        );
    }
}