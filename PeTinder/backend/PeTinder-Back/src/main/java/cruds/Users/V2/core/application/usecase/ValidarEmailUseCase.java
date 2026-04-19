package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Usuario;

public class ValidarEmailUseCase {
    
    private final UsuarioGateway usuarioGateway;

    public ValidarEmailUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario validar(String email) {
        return usuarioGateway.buscarPorEmail(email)
            .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException(
                "Usuário com email " + email + " não encontrado"
            ));
    }
}