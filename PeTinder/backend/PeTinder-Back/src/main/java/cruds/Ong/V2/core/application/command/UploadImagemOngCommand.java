package cruds.Ong.V2.core.application.command;

import cruds.Ong.V2.core.domain.ImagemOng;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UploadImagemOngCommand {
    private UUID ongId;
    private byte[] imagemBytes;
    private final String nomeArquivo;

    public ImagemOng criarImagemOng() {
        return new ImagemOng(imagemBytes, nomeArquivo);
    }

}

