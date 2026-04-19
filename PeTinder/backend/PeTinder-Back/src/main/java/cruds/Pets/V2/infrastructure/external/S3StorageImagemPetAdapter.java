package cruds.Pets.V2.infrastructure.external;

import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.mapper.ImagemPetMapper;
import cruds.common.cryptography.AesCriptografiaAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "s3")
public class S3StorageImagemPetAdapter implements ImagemPetGateway {

    private final S3Client s3Client;
    private final String bucketName;
    private final AesCriptografiaAdapter criptografiaAdapter;
    private final ImagemPetJpaRepository imagemPetJpaRepository;
    private final String region;

    public S3StorageImagemPetAdapter(
            S3Client s3Client,
            @Value("${aws.s3.bucket}") String bucketName,
            @Value("${aws.region:us-east-1}") String region,
            AesCriptografiaAdapter criptografiaAdapter,
            ImagemPetJpaRepository imagemPetJpaRepository
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.region = region;
        this.criptografiaAdapter = criptografiaAdapter;
        this.imagemPetJpaRepository = imagemPetJpaRepository;
    }

    @Override
    public ImagemPet salvar(ImagemPet imagem, UUID petId) {
        ImagemPetEntity entity = ImagemPetMapper.toEntity(imagem, petId);
        entity.setId(null);
        imagemPetJpaRepository.save(entity);
        return ImagemPetMapper.toDomain(entity);
    }


    @Override
    public List<ImagemPet> salvarTodas(List<ImagemPet> imagens, UUID petId) {
        return imagens.stream()
                .map(img -> salvar(img, petId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ImagemPet> buscarPorPetId(UUID petId, List<String> keys) {
        return keys.stream()
                .map(key -> {
                    try {
                        byte[] dadosCriptografados = s3Client.getObjectAsBytes(
                                GetObjectRequest.builder()
                                        .bucket(bucketName)
                                        .key(key)
                                        .build()
                        ).asByteArray();

                        byte[] dadosDescriptografados = criptografiaAdapter.descriptografarImagem(dadosCriptografados);

                        String nomeArquivo = extrairNomeArquivo(key);
                        UUID idImagem = extrairIdImagem(key);

                        return new ImagemPet(idImagem, nomeArquivo, dadosDescriptografados, key);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(img -> img != null)
                .collect(Collectors.toList());
    }

    @Override
    public void remover(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());

        imagemPetJpaRepository.deleteByKeyS3(key);
    }

    @Override
    public void removerPorPetId(UUID petId, List<String> keys) {
        keys.forEach(key -> s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        ));

        imagemPetJpaRepository.deleteAllByPetId(petId);
    }

    private String gerarKey(UUID petId, UUID idImagem, String nomeArquivo) {
        return String.format("%s-%s-%s", petId, idImagem, nomeArquivo);
    }

    private String extrairNomeArquivo(String key) {
        String[] partes = key.split("/");
        return partes.length == 3 ? partes[2] : key;
    }

    private UUID extrairIdImagem(String key) {
        String[] partes = key.split("/");
        if (partes.length >= 2) {
            try {
                return UUID.fromString(partes[1]);
            } catch (Exception ignored) {}
        }
        return null;
    }

    @Override
    public List<String> buscarKeysPorPetId(UUID petId) {
        return imagemPetJpaRepository.findKeysByPetId(petId);
    }
}
