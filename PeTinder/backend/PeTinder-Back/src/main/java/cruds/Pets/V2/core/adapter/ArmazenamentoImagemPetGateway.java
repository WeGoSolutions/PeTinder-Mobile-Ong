package cruds.Pets.V2.core.adapter;

import cruds.Pets.V2.core.domain.ImagemPet;

import java.util.UUID;

public interface ArmazenamentoImagemPetGateway {

    String salvarImagem(ImagemPet imagem, UUID petId);

    void removerImagem(String nomeArquivo, UUID idImagem);

    ImagemPet buscarImagem(String nomeArquivo, UUID idImagem);

    ImagemPet buscarPorKey(String key);

    void removerPorKey(String key);
}