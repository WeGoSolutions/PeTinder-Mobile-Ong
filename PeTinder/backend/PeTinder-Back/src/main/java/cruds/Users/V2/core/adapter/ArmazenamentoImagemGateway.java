package cruds.Users.V2.core.adapter;

import cruds.Users.V2.core.domain.ImagemUsuario;

import java.util.UUID;

public interface ArmazenamentoImagemGateway {

    String salvarImagem(ImagemUsuario imagem);

    void removerImagem(String nomeArquivo, UUID idImagem);

    ImagemUsuario buscarImagem(String nomeArquivo, UUID idImagem);
}
