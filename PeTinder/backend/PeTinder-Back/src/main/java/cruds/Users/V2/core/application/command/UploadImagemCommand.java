package cruds.Users.V2.core.application.command;

import cruds.Users.V2.core.domain.ImagemUsuario;
import java.util.UUID;

public class UploadImagemCommand {
    private final UUID usuarioId;
    private final byte[] dadosImagem;
    private final String nomeArquivo;

    public UploadImagemCommand(UUID usuarioId, byte[] dadosImagem, String nomeArquivo) {
        this.usuarioId = usuarioId;
        this.dadosImagem = dadosImagem;
        this.nomeArquivo = nomeArquivo != null ? nomeArquivo : "perfil.jpg";
    }

    public UUID getUsuarioId() { 
        return usuarioId; 
    }
    
    public ImagemUsuario criarImagemUsuario() {
        return new ImagemUsuario(dadosImagem, nomeArquivo);
    }
}