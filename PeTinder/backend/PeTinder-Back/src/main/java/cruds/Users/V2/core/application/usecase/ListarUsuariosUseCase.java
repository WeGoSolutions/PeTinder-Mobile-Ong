package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.domain.Usuario;
import java.util.List;

public class ListarUsuariosUseCase {
    
    private final UsuarioGateway usuarioGateway;

    public ListarUsuariosUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public List<Usuario> listar() {
        return usuarioGateway.listarTodos();
    }
}
