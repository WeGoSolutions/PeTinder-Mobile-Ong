package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.ArmazenamentoImagemOngGateway;
import cruds.Ong.V2.core.domain.ImagemOng;
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
public class LocalStorageOngAdapter implements ArmazenamentoImagemOngGateway {

    private final String basePath;
    private final AesCriptografiaAdapter criptografiaAdapter;

    public LocalStorageOngAdapter(
            @Value("${app.image.storage.path}") String basePath,
            AesCriptografiaAdapter criptografiaAdapter
    ) {
        this.basePath = basePath;
        this.criptografiaAdapter = criptografiaAdapter;
    }

    @Override
    public String salvarImagem(ImagemOng imagem) {

        UUID id = imagem.getId() != null
                ? imagem.getId()
                : UUID.randomUUID();

        imagem.setId(id);

        String nomeArquivo = imagem.getArquivo();

        String key = id + "_" + nomeArquivo;

        byte[] dadosCriptografados =
                criptografiaAdapter.criptografarImagem(imagem.getDados());

        Path dir = Paths.get(basePath, "ongs");

        try {
            Files.createDirectories(dir);
            Files.write(dir.resolve(key), dadosCriptografados);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem da ONG localmente", e);
        }

        return key;
    }

    @Override
    public void removerImagem(String nomeArquivo, UUID idImagem) {

        String key = idImagem + "_" + nomeArquivo;

        Path file = Paths.get(basePath, "ongs", key);

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("Erro ao remover imagem da ONG local: " + e.getMessage());
        }
    }

    @Override
    public ImagemOng buscarImagem(String nomeArquivo, UUID idImagem) {

        String key = idImagem + "_" + nomeArquivo;

        Path file = Paths.get(basePath, "ongs", key);

        try {
            if (!Files.exists(file)) return null;

            byte[] dadosCriptografados = Files.readAllBytes(file);
            byte[] dados =
                    criptografiaAdapter.descriptografarImagem(dadosCriptografados);

            return new ImagemOng(idImagem, dados, nomeArquivo);

        } catch (IOException e) {
            return null;
        }
    }
}
