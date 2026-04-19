package cruds.common.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@Component
public class LocalImageStorageStrategy implements ImageStorageStrategy {

    @Value("${app.image.storage.path}")
    private String baseDirectory;

    @Override
    public String salvarImagem(byte[] imageBytes, String fileName, String extension) throws IOException {
        String safeFileName = Paths.get(fileName).getFileName().toString();
        Path dirPath = Paths.get(baseDirectory);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = dirPath.resolve(safeFileName);
        Files.write(filePath, imageBytes);
        return filePath.toString();
    }

    @Override
    public void salvarImagem(byte[] imagemBytes, String nomeArquivo) throws IOException {
        salvarImagem(imagemBytes, nomeArquivo, null);
    }

    @Override
    public String gerarCaminho(String nomeArquivo) {
        return Paths.get(baseDirectory, nomeArquivo).toString();
    }
}