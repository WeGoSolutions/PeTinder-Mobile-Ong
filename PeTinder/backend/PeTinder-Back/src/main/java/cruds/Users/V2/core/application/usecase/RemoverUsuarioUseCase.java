package cruds.Users.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.application.usecase.RemoverPorUsuarioUseCase;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.exception.UsuarioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoverUsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final RemoverPorUsuarioUseCase removerPorUsuarioUseCase;

    public RemoverUsuarioUseCase(UsuarioGateway usuarioGateway,
                                 RemoverPorUsuarioUseCase removerPorUsuarioUseCase) {
        this.usuarioGateway = usuarioGateway;
        this.removerPorUsuarioUseCase = removerPorUsuarioUseCase;
    }

    @Transactional
    public void apagarUser(UUID usuarioId) {
        if (!usuarioGateway.buscarPorId(usuarioId).isPresent()) {
            throw new UsuarioException.UsuarioNaoEncontradoException(
                    "Usuário não encontrado: " + usuarioId
            );
        }

        removerPorUsuarioUseCase.removerPorId(usuarioId);
        usuarioGateway.remover(usuarioId);
    }
}
