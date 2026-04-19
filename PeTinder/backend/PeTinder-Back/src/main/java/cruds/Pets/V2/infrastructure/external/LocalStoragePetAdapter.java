package cruds.Pets.V2.infrastructure.external;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.domain.ImagemPet;
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
public class LocalStoragePetAdapter implements ArmazenamentoImagemPetGateway {

    private final String basePath;
    private final AesCriptografiaAdapter criptografiaAdapter;

    public LocalStoragePetAdapter(
            @Value("${app.image.storage.path}") String basePath,
            AesCriptografiaAdapter criptografiaAdapter
    ) {
        this.basePath = basePath;
        this.criptografiaAdapter = criptografiaAdapter;
    }

    @Override
    public String salvarImagem(ImagemPet imagem, UUID petId) {

        UUID id = imagem.getId() != null
                ? imagem.getId()
                : UUID.randomUUID();

        imagem.setId(id);

        String key = String.format(
                "%s-%s-%s",
                petId,
                id,
                imagem.getNomeArquivo()
        );

        byte[] dadosCriptografados =
                criptografiaAdapter.criptografarImagem(imagem.getDados());

        Path dir = Paths.get(basePath, "pets");

        try {
            Files.createDirectories(dir);
            Files.write(dir.resolve(key), dadosCriptografados);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem do pet localmente", e);
        }

        imagem.setKeyS3(key);
        return key;
    }

    /**
     * ⚠️ Mantido apenas para compatibilidade com a interface.
     * No fluxo novo de pets, prefira sempre usar removerPorKey.
     */
    @Override
    public void removerImagem(String nomeArquivo, UUID idImagem) {

        String key = idImagem + "_" + nomeArquivo;

        Path file = Paths.get(basePath, "pets", key);

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("Erro ao remover imagem local: " + e.getMessage());
        }
    }

    /**
     * ⚠️ Mantido apenas para compatibilidade com a interface.
     * No fluxo novo de pets, prefira sempre usar buscarPorKey.
     */
    @Override
    public ImagemPet buscarImagem(String nomeArquivo, UUID idImagem) {

        String key = idImagem + "_" + nomeArquivo;

        Path file = Paths.get(basePath, "pets", key);

        try {
            if (!Files.exists(file)) return null;

            byte[] dadosCriptografados = Files.readAllBytes(file);
            byte[] dados =
                    criptografiaAdapter.descriptografarImagem(dadosCriptografados);

            return new ImagemPet(idImagem, nomeArquivo, dados, key);

        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public ImagemPet buscarPorKey(String key) {

        Path file = Paths.get(basePath, "pets", key);

        try {
            if (!Files.exists(file)) return null;

            byte[] dadosCriptografados = Files.readAllBytes(file);
            byte[] dados =
                    criptografiaAdapter.descriptografarImagem(dadosCriptografados);

            return new ImagemPet(null, null, dados, key);

        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void removerPorKey(String key) {

        Path file = Paths.get(basePath, "pets", key);

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("Erro ao remover imagem local por key: " + e.getMessage());
        }
    }
}