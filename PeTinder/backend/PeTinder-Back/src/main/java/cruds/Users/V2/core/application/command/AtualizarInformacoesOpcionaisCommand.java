package cruds.Users.V2.core.application.command;

import cruds.Users.V2.core.domain.Endereco;

import java.util.UUID;

public class AtualizarInformacoesOpcionaisCommand {
    
    private final UUID usuarioId;
    private final String cpf;
    private final String cep;
    private final String rua;
    private final Integer numero;
    private final String cidade;
    private final String uf;
    private final String complemento;

    public AtualizarInformacoesOpcionaisCommand(UUID usuarioId, String cpf, String cep, 
                                              String rua, Integer numero, String cidade, 
                                              String uf, String complemento) {
        this.usuarioId = usuarioId;
        this.cpf = cpf;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.uf = uf;
        this.complemento = complemento;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public String getCpf() {
        return cpf;
    }
    
    public Endereco criarEndereco() {
        return new Endereco(cep, rua, numero, cidade, uf, complemento);
    }
}
