package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.application.command.AtualizarSenhaOngCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarSenhaOngWebDTO {

    @NotBlank
    private String senhaAtual;

    @NotBlank
    private String novaSenha;

    public AtualizarSenhaOngCommand toCommand(UUID id) {
        return new AtualizarSenhaOngCommand(id, senhaAtual, novaSenha);
    }
}

