package cruds.Ong.V2.core.adapter;

import cruds.Ong.V2.core.domain.ImagemOng;

import java.util.UUID;

public interface ArmazenamentoImagemOngGateway {

    String salvarImagem(ImagemOng imagem);

    void removerImagem(String nomeArquivo, UUID idImagem);

    ImagemOng buscarImagem(String nomeArquivo, UUID idImagem);
}
