package cruds.Users.V2.core.application.command;

public class LoginUsuarioCommand {
    
    private final String email;
    private final String senha;

    public LoginUsuarioCommand(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}
