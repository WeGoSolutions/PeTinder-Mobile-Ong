package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.AtualizarUsuarioCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Endereco;
import cruds.Users.V2.core.domain.Usuario;

public class AtualizarUsuarioUseCase {
    
    private final UsuarioGateway usuarioGateway;

    public AtualizarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario atualizar(AtualizarUsuarioCommand command) {
        Usuario usuario = usuarioGateway.buscarPorId(command.getUsuarioId())
            .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException(
                "Usuário com id: " + command.getUsuarioId() + " não encontrado"
            ));

        // Verificar se email já existe (exceto para o próprio usuário)
        if (!command.getEmail().equals(usuario.getEmail()) && 
            usuarioGateway.emailJaExiste(command.getEmail())) {
            throw new UsuarioException.EmailJaExisteException(
                "Email já está em uso: " + command.getEmail()
            );
        }

        // Verificar se CPF já existe (exceto para o próprio usuário)
        if (command.getCpf() != null && 
            !command.getCpf().equals(usuario.getCpf()) && 
            usuarioGateway.cpfJaExiste(command.getCpf())) {
            throw new UsuarioException.CpfJaExisteException(
                "CPF já está em uso: " + command.getCpf()
            );
        }

        // Criar novo usuário com dados atualizados
        Usuario usuarioAtualizado = new Usuario(
            usuario.getId(),
            command.getNome(),
            command.getEmail(),
            usuario.getSenha(), // Mantém a senha atual
            command.getDataNasc(),
            command.getCpf(),
            usuario.getUsuarioNovo()
        );

        // Atualizar endereço
        Endereco novoEndereco = command.criarEndereco();
        usuarioAtualizado.atualizarEndereco(novoEndereco);

        // Manter imagem atual se existir
        if (usuario.getImagemUsuario() != null) {
            usuarioAtualizado.atualizarImagemUsuario(usuario.getImagemUsuario());
        }

        return usuarioGateway.atualizar(usuarioAtualizado);
    }
}