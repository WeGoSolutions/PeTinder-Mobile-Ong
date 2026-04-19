package cruds.common.strategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AzureImageStorageStrategy implements ImageStorageStrategy {

    private final Map<String, byte[]> blobContainerSimulado = new HashMap<>();
    private static final String CONTAINER_URL = "https://mockazure.blob.core.windows.net/pets/";

    @Override
    public String salvarImagem(byte[] imageBytes, String fileName, String extension) throws IOException {
        return "";
    }

    @Override
    public void salvarImagem(byte[] imagemBytes, String nomeArquivo) throws IOException {
        String caminho = gerarCaminho(nomeArquivo);
        blobContainerSimulado.put(caminho, imagemBytes);
    }

    @Override
    public String gerarCaminho(String nomeArquivo) {
        return CONTAINER_URL + nomeArquivo;
    }

}

