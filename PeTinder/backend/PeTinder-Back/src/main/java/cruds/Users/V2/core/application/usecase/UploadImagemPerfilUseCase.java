package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.UploadImagemCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.ImagemUsuario;
import cruds.Users.V2.core.domain.Usuario;

public class UploadImagemPerfilUseCase {

    private final UsuarioGateway usuarioGateway;
    private final ArmazenamentoImagemGateway armazenamentoImagemGateway;

    public UploadImagemPerfilUseCase(UsuarioGateway usuarioGateway,
                                     ArmazenamentoImagemGateway armazenamentoImagemGateway) {
        this.usuarioGateway = usuarioGateway;
        this.armazenamentoImagemGateway = armazenamentoImagemGateway;
    }

    public Usuario executar(UploadImagemCommand command) {
        // Buscar usuário
        Usuario usuario = usuarioGateway.buscarPorId(command.getUsuarioId())
                .orElseThrow(() -> new UsuarioException.UsuarioNaoEncontradoException(
                        "Usuário não encontrado: " + command.getUsuarioId()
                ));

        // Criar imagem de usuário
        ImagemUsuario novaImagem = command.criarImagemUsuario();

        // Remover imagem anterior se existir
        if (usuario.getImagemUsuario() != null && usuario.getImagemUsuario().temImagem()) {
            try {
                armazenamentoImagemGateway.removerImagem(usuario.getImagemUsuario().getNomeArquivo(), usuario.getImagemUsuario().getId());
            } catch (Exception e) {
                // Log do erro, mas não falha a operação
                System.err.println("Erro ao remover imagem anterior: " + e.getMessage());
            }
        }

        // Salvar nova imagem
        String urlImagem = armazenamentoImagemGateway.salvarImagem(novaImagem);

        // Atualizar usuário com nova imagem
        usuario.atualizarImagemUsuario(novaImagem);

        // Salvar alterações
        return usuarioGateway.atualizar(usuario);
    }
}