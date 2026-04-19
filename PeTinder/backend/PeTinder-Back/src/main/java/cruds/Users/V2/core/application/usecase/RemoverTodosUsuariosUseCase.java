package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;

public class RemoverTodosUsuariosUseCase {
    
    private final UsuarioGateway usuarioGateway;

    public RemoverTodosUsuariosUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public void removerTodos() {
        usuarioGateway.removerTodos();
    }
}