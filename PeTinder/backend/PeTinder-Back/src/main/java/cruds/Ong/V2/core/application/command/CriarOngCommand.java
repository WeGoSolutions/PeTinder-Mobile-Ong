package cruds.Ong.V2.core.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CriarOngCommand {
    private String cnpj;
    private String cpf;
    private String nome;
    private String razaoSocial;
    private String senha;
    private String email;
    private String link;
    private EnderecoCommand endereco;

    @Getter
    @AllArgsConstructor
    public static class EnderecoCommand {
        private String cep;
        private String rua;
        private String numero;
        private String cidade;
        private String uf;
        private String complemento;
    }
}
