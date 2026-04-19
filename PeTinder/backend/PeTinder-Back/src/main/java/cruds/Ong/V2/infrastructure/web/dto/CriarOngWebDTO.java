package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.application.command.CriarOngCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CriarOngWebDTO {

    @Pattern(regexp = "\\d{14}")
    private String cnpj;

    @Pattern(regexp = "\\d{11}")
    private String cpf;

    @NotBlank
    @Size(min = 3)
    @Pattern(regexp = "^[A-Za-zÀ-Ö ]+$")
    private String nome;

    @NotBlank
    private String razaoSocial;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\\\":{}|<>]+$")
    private String senha;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String link;

    private EnderecoWebDTO endereco;

    public CriarOngCommand toCommand() {
        CriarOngCommand.EnderecoCommand enderecoCommand = null;
        if (endereco != null) {
            enderecoCommand = new CriarOngCommand.EnderecoCommand(
                endereco.getCep(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getUf(),
                endereco.getComplemento()
            );
        }

        return new CriarOngCommand(
            cnpj, cpf, nome, razaoSocial, senha, email, link, enderecoCommand
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
