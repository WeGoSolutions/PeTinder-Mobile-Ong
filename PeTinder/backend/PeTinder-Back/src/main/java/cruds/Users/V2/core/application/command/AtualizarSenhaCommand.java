package cruds.Users.V2.core.application.command;

public class AtualizarSenhaCommand {
    
    private final String email;
    private final String senhaAtual;
    private final String novaSenha;

    public AtualizarSenhaCommand(String email, String senhaAtual, String novaSenha) {
        this.email = email;
        this.senhaAtual = senhaAtual;
        this.novaSenha = novaSenha;
    }

    public String getEmail() { return email; }
    public String getSenhaAtual() { return senhaAtual; }
    public String getNovaSenha() { return novaSenha; }
}
