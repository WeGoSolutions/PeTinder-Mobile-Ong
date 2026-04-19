package cruds.Users.V2.core.application.command;

import java.time.LocalDate;

public class CriarUsuarioCommand {
    
    private final String nome;
    private final String email;
    private final String senha;
    private final LocalDate dataNascimento;

    public CriarUsuarioCommand(String nome, String email, String senha, LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public LocalDate getDataNascimento() { return dataNascimento; }
}
