package cruds.Pets.V2.infrastructure.external;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.mapper.ImagemPetMapper;
import cruds.common.cryptography.AesCriptografiaAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local")
public class LocalStorageImagemPetAdapter implements ImagemPetGateway {

    private final ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway;
    private final ImagemPetJpaRepository imagemPetJpaRepository;

    public LocalStorageImagemPetAdapter(
            ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway,
            ImagemPetJpaRepository imagemPetJpaRepository
    ) {
        this.armazenamentoImagemPetGateway = armazenamentoImagemPetGateway;
        this.imagemPetJpaRepository = imagemPetJpaRepository;
    }

    @Override
    public ImagemPet salvar(ImagemPet imagem, UUID petId) {

        String key = armazenamentoImagemPetGateway.salvarImagem(imagem, petId);

        ImagemPetEntity entity = ImagemPetEntity.builder()
                .petId(petId)
                .keyS3(key)
                .nomeArquivo(imagem.getNomeArquivo())
                .build();

        imagemPetJpaRepository.save(entity);

        imagem.setKey(key);

        return imagem;
    }

    @Override
    public List<ImagemPet> salvarTodas(List<ImagemPet> imagens, UUID petId) {

        List<ImagemPet> resultado = new ArrayList<>();

        for (ImagemPet imagem : imagens) {
            resultado.add(salvar(imagem, petId));
        }

        return resultado;
    }

    @Override
    public List<ImagemPet> buscarPorPetId(UUID petId, List<String> keys) {

        List<ImagemPet> imagens = new ArrayList<>();

        for (String key : keys) {

            ImagemPet imagem = armazenamentoImagemPetGateway.buscarPorKey(key);

            if (imagem != null) {
                imagem.setKey(key);
                imagens.add(imagem);
            }
        }

        return imagens;
    }

    @Override
    public void remover(String key) {

        armazenamentoImagemPetGateway.removerPorKey(key);
        imagemPetJpaRepository.deleteByKeyS3(key);
    }

    @Override
    public void removerPorPetId(UUID petId, List<String> keys) {

        for (String key : keys) {
            armazenamentoImagemPetGateway.removerPorKey(key);
        }

        imagemPetJpaRepository.deleteByPetIdAndKeyS3In(petId, keys);
    }

    @Override
    public List<String> buscarKeysPorPetId(UUID petId) {
        return imagemPetJpaRepository.findKeysByPetId(petId);
    }

}