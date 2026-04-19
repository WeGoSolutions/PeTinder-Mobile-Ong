package cruds.Users.V2.infrastructure.external;

import cruds.Users.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Users.V2.core.domain.ImagemUsuario;
import cruds.common.cryptography.AesCriptografiaAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local")
public class LocalUserStorageAdapter implements ArmazenamentoImagemGateway {

    private final Path rootPath;
    private final AesCriptografiaAdapter criptografiaAdapter;

    public LocalUserStorageAdapter(
            @Value("${app.image.storage.path}") String basePath,
            AesCriptografiaAdapter criptografiaAdapter
    ) {
        this.rootPath = Paths.get(basePath, "users");
        this.criptografiaAdapter = criptografiaAdapter;
    }

    @Override
    public String salvarImagem(ImagemUsuario imagemUsuario) {

        UUID id = imagemUsuario.getId() != null
                ? imagemUsuario.getId()
                : UUID.randomUUID();

        String key = imagemUsuario.getNomeArquivo() + "-" + id;

        byte[] imagemCriptografada =
                criptografiaAdapter.criptografarImagem(imagemUsuario.getDados());

        try {
            Files.createDirectories(rootPath);

            Path arquivo = rootPath.resolve(key);

            Files.write(arquivo, imagemCriptografada);

            return key;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem de usuário no filesystem", e);
        }
    }

    @Override
    public void removerImagem(String nomeArquivo, UUID idImagem) {

        String key = nomeArquivo + "-" + idImagem;
        Path arquivo = rootPath.resolve(key);

        try {
            Files.deleteIfExists(arquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao remover imagem de usuário no filesystem", e);
        }
    }

    @Override
    public ImagemUsuario buscarImagem(String nomeArquivo, UUID idImagem) {

        String key = nomeArquivo + "-" + idImagem;
        Path arquivo = rootPath.resolve(key);

        try {

            if (!Files.exists(arquivo)) {
                throw new RuntimeException("Imagem não encontrada: " + key);
            }

            byte[] bytesCriptografados = Files.readAllBytes(arquivo);

            byte[] imagemDescriptografada =
                    criptografiaAdapter.descriptografarImagem(bytesCriptografados);

            return new ImagemUsuario(idImagem, imagemDescriptografada, nomeArquivo);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar imagem de usuário no filesystem", e);
        }
    }
}
