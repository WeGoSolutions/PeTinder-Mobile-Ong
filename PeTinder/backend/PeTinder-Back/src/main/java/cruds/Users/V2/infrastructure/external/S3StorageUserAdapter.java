package cruds.Users.V2.infrastructure.external;

import cruds.Users.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Users.V2.core.domain.ImagemUsuario;
import cruds.common.cryptography.AesCriptografiaAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "s3")
public class S3StorageUserAdapter implements ArmazenamentoImagemGateway {

    private final S3Client s3Client;
    private final String bucketName;
    private final AesCriptografiaAdapter criptografiaAdapter;

    public S3StorageUserAdapter(S3Client s3Client,
                                @Value("${aws.s3.bucket}") String bucketName,
                                AesCriptografiaAdapter criptografiaAdapter) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.criptografiaAdapter = criptografiaAdapter;
    }

    @Override
    public String salvarImagem(ImagemUsuario imagemUsuario) {
        UUID id = imagemUsuario.getId() != null ? imagemUsuario.getId() : UUID.randomUUID();
        String key = imagemUsuario.getNomeArquivo() + "-" + id;

        byte[] imagemCriptografada = criptografiaAdapter.criptografarImagem(imagemUsuario.getDados());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(imagemCriptografada)
        );

        String region = "us-east-1";

        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                region,
                key);
    }

    @Override
    public void removerImagem(String nomeArquivo, UUID idImagem) {
        String key = nomeArquivo + "-" + idImagem;
        s3Client.deleteObject(b -> b.bucket(bucketName).key(key));
    }

    @Override
    public ImagemUsuario buscarImagem(String nomeArquivo, UUID idImagem) {
        String key = nomeArquivo + "-" + idImagem;

        var response = s3Client.getObjectAsBytes(r ->
                r.bucket(bucketName).key(key));

        byte[] imagemDescriptografada = criptografiaAdapter.descriptografarImagem(response.asByteArray());

        return new ImagemUsuario(idImagem, imagemDescriptografada, nomeArquivo);
    }
}