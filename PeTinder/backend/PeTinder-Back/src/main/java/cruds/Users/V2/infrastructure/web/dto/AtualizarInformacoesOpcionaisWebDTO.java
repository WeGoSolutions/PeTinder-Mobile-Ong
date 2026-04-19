package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.application.command.AtualizarInformacoesOpcionaisCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarInformacoesOpcionaisWebDTO {

    @Schema(description = "CPF do usuário", example = "123.456.789-00")
    private String cpf;

    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve ter formato válido (00000-000)")
    @Schema(description = "CEP", example = "01234-567")
    private String cep;

    @Schema(description = "Rua", example = "Rua das Flores")
    private String rua;

    @Schema(description = "Número", example = "123")
    private Integer numero;

    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;

    @Pattern(regexp = "[A-Z]{2}", message = "UF deve ter 2 caracteres")
    @Schema(description = "UF", example = "SP")
    private String uf;

    @Schema(description = "Complemento", example = "Apto 45")
    private String complemento;

    public AtualizarInformacoesOpcionaisCommand toCommand(UUID usuarioId) {
        return new AtualizarInformacoesOpcionaisCommand(
            usuarioId, cpf, cep, rua, numero, cidade, uf, complemento
        );
    }
}
