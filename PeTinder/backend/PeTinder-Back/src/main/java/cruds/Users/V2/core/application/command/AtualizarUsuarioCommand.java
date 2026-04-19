package cruds.Users.V2.core.application.command;

import cruds.Users.V2.core.domain.Endereco;

import java.time.LocalDate;
import java.util.UUID;

public class AtualizarUsuarioCommand {
    
    private final UUID usuarioId;
    private final String nome;
    private final String email;
    private final LocalDate dataNasc;
    private final String cpf;
    private final String cep;
    private final String rua;
    private final Integer numero;
    private final String complemento;
    private final String cidade;
    private final String uf;

    public AtualizarUsuarioCommand(UUID usuarioId, String nome, String email, 
                                 LocalDate dataNasc, String cpf, String cep,
                                 String rua, Integer numero, String complemento,
                                 String cidade, String uf) {
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.email = email;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.uf = uf;
    }

    public UUID getUsuarioId() { return usuarioId; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public LocalDate getDataNasc() { return dataNasc; }
    public String getCpf() { return cpf; }
    
    public Endereco criarEndereco() {
        return new Endereco(cep, rua, numero, cidade, uf, complemento);
    }
}