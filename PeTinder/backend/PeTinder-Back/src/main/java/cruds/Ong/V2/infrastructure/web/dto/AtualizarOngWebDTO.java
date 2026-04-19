package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.application.command.AtualizarOngCommand;
import jakarta.validation.constraints.Email;
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
public class AtualizarOngWebDTO {

    @Pattern(regexp = "\\d{14}")
    private String cnpj;

    @Pattern(regexp = "\\d{11}")
    private String cpf;

    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-zÀ-Ö ]+$")
    private String nome;

    private String razaoSocial;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String link;

    private EnderecoWebDTO endereco;

    public AtualizarOngCommand toCommand(UUID id) {
        AtualizarOngCommand.EnderecoCommand enderecoCommand = null;
        if (endereco != null) {
            enderecoCommand = new AtualizarOngCommand.EnderecoCommand(
                    endereco.getCep(),
                    endereco.getRua(),
                    endereco.getNumero(),
                    endereco.getCidade(),
                    endereco.getUf(),
                    endereco.getComplemento()
            );
        }

        return new AtualizarOngCommand(
                id, cnpj, cpf, nome, razaoSocial, email, link, enderecoCommand
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnderecoWebDTO {
        private String cep;
        private String rua;
        private String numero;
        private String cidade;
        private String uf;
        private String complemento;
    }
}