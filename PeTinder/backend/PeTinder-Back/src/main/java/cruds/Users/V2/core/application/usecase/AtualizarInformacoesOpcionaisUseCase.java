package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.AtualizarInformacoesOpcionaisCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Endereco;
import cruds.Users.V2.core.domain.Usuario;

public class AtualizarInformacoesOpcionaisUseCase {
    
    private final UsuarioGateway usuarioGateway;

    public AtualizarInformacoesOpcionaisUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    public Usuario adicionarInfos(AtualizarInformacoesOpcionaisCommand command) {
        Usuario usuario = usuarioGateway.buscarPorId(command.getUsuarioId())
            .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException(
                "Usuário não encontrado: " + command.getUsuarioId()
            ));

        if (command.getCpf() != null && !command.getCpf().trim().isEmpty()) {
            if (usuarioGateway.cpfJaExiste(command.getCpf()) && 
                !command.getCpf().equals(usuario.getCpf())) {
                throw new UsuarioException.CpfJaExisteException(
                    "CPF já está em uso: " + command.getCpf()
                );
            }
        }

        Endereco novoEndereco = command.criarEndereco();

        usuario.atualizarInformacoesOpcionais(command.getCpf(), novoEndereco);

        return usuarioGateway.atualizar(usuario);
    }
}
