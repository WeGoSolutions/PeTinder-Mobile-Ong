package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.CriptografiaGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.AtualizarSenhaCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Usuario;

public class AtualizarSenhaUseCase {
    
    private final UsuarioGateway usuarioGateway;
    private final CriptografiaGateway criptografiaGateway;

    public AtualizarSenhaUseCase(UsuarioGateway usuarioGateway, CriptografiaGateway criptografiaGateway) {
        this.usuarioGateway = usuarioGateway;
        this.criptografiaGateway = criptografiaGateway;
    }

    public Usuario atualizarSenha(AtualizarSenhaCommand command) {
        Usuario usuario = usuarioGateway.buscarPorEmail(command.getEmail())
                .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException(
                        "Usuário não encontrado com o email: " + command.getEmail()
                ));

        if (command.getSenhaAtual() != null && !command.getSenhaAtual().trim().isEmpty()) {
            if (!criptografiaGateway.verificarSenha(command.getSenhaAtual(), usuario.getSenha())) {
                throw new UsuarioException.SenhaInvalidaException("Senha atual incorreta");
            }
        }

        String novaSenhaCriptografada = criptografiaGateway.criptografarSenha(command.getNovaSenha());
        usuario.atualizarSenha(novaSenhaCriptografada);

        return usuarioGateway.atualizar(usuario);
    }
}
