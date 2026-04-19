package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Usuario;

import java.util.UUID;

public class MarcarUsuarioExperienteUseCase {
    
    private final UsuarioGateway usuarioGateway;

    public MarcarUsuarioExperienteUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario executar(UUID usuarioId) {
        Usuario usuario = usuarioGateway.buscarPorId(usuarioId)
            .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException(
                "Usuário não encontrado: " + usuarioId
            ));

        usuario.marcarComoUsuarioExperiente();

        return usuarioGateway.atualizar(usuario);
    }
}
