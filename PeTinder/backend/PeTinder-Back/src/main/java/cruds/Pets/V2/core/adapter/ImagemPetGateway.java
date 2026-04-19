package cruds.Pets.V2.core.adapter;

import cruds.Pets.V2.core.domain.ImagemPet;

import java.util.List;
import java.util.UUID;

public interface ImagemPetGateway {

    ImagemPet salvar(ImagemPet imagem, UUID petId);
    
    List<ImagemPet> salvarTodas(List<ImagemPet> imagens, UUID petId);

    List<ImagemPet> buscarPorPetId(UUID petId, List<String> keys);

    void remover(String key);

    void removerPorPetId(UUID petId, List<String> keys);

    List<String> buscarKeysPorPetId(UUID petId);
}