package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.CriptografiaGateway;
import cruds.Users.V2.core.adapter.EmailGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.CriarUsuarioCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Usuario;

public class CriarUsuarioUseCase {
    
    private final UsuarioGateway usuarioGateway;
    private final CriptografiaGateway criptografiaGateway;
    private final EmailGateway emailGateway;

    public CriarUsuarioUseCase(UsuarioGateway usuarioGateway, 
                              CriptografiaGateway criptografiaGateway,
                              EmailGateway emailGateway) {
        this.usuarioGateway = usuarioGateway;
        this.criptografiaGateway = criptografiaGateway;
        this.emailGateway = emailGateway;
    }

    public Usuario cadastrar(CriarUsuarioCommand command) {
        if (usuarioGateway.emailJaExiste(command.getEmail())) {
            throw new UsuarioException.EmailJaExisteException(
                "Email já está em uso: " + command.getEmail()
            );
        }

        Usuario usuario = new Usuario(
            command.getNome(),
            command.getEmail(),
            command.getSenha(),
            command.getDataNascimento()
        );

        String senhaCriptografada = criptografiaGateway.criptografarSenha(usuario.getSenha());
        usuario.atualizarSenha(senhaCriptografada);

        Usuario usuarioSalvo = usuarioGateway.salvar(usuario);

        try {
            emailGateway.enviarEmailBoasVindas(usuarioSalvo.getEmail(), usuarioSalvo.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de boas-vindas: " + e.getMessage());
        }

        return usuarioSalvo;
    }
}
