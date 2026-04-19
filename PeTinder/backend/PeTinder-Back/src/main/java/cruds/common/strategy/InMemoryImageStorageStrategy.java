package cruds.common.strategy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InMemoryImageStorageStrategy implements ImageStorageStrategy {

    private final Map<String, byte[]> storage = new HashMap<>();

    @Override
    public String salvarImagem(byte[] imageBytes, String fileName, String extension) throws IOException {
        return "";
    }

    @Override
    public void salvarImagem(byte[] imagemBytes, String nomeArquivo) {
        storage.put(nomeArquivo, imagemBytes);
    }

    @Override
    public String gerarCaminho(String nomeArquivo) {
        return "memory://" + nomeArquivo;
    }
}

