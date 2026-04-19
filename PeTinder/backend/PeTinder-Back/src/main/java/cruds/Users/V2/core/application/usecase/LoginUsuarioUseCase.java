package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.AutenticacaoGateway;
import cruds.Users.V2.core.adapter.CriptografiaGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.LoginUsuarioCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Usuario;

public class LoginUsuarioUseCase {
    
    private final UsuarioGateway usuarioGateway;
    private final CriptografiaGateway criptografiaGateway;
    private final AutenticacaoGateway autenticacaoGateway;

    public LoginUsuarioUseCase(UsuarioGateway usuarioGateway,
                              CriptografiaGateway criptografiaGateway,
                              AutenticacaoGateway autenticacaoGateway) {
        this.usuarioGateway = usuarioGateway;
        this.criptografiaGateway = criptografiaGateway;
        this.autenticacaoGateway = autenticacaoGateway;
    }

    public LoginResult logar(LoginUsuarioCommand command) {
        Usuario usuario = usuarioGateway.buscarPorEmail(command.getEmail())
            .orElseThrow(() -> new UsuarioException.CredenciaisInvalidasException(
                "Credenciais inválidas"
            ));

        if (!criptografiaGateway.verificarSenha(command.getSenha(), usuario.getSenha())) {
            throw new UsuarioException.CredenciaisInvalidasException("Credenciais inválidas");
        }

        String token = autenticacaoGateway.gerarToken(usuario.getEmail());

        return new LoginResult(usuario, token);
    }

    public static class LoginResult {
        private final Usuario usuario;
        private final String token;

        public LoginResult(Usuario usuario, String token) {
            this.usuario = usuario;
            this.token = token;
        }

        public Usuario getUsuario() { return usuario; }
        public String getToken() { return token; }
    }
}
