package cruds.common.strategy;

import java.io.IOException;

public interface ImageStorageStrategy {
    String salvarImagem(byte[] imageBytes, String fileName, String extension) throws IOException;

    void salvarImagem(byte[] imagemBytes, String nomeArquivo) throws IOException;
    String gerarCaminho(String nomeArquivo);
}
