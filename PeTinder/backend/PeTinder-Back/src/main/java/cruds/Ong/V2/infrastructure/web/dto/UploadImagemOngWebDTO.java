package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.application.command.UploadImagemOngCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadImagemOngWebDTO {

    @NotBlank
    private String imagem;

    public UploadImagemOngCommand toCommand(UUID ongId) {
        String base64Data = imagem;
        String nomeArquivo = "ong_" + ongId + "_ong.jpg";
        if (base64Data != null && base64Data.contains(",")) {
            base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
        }
        byte[] imagemBytes = Base64.getDecoder().decode(base64Data);
        return new UploadImagemOngCommand(ongId, imagemBytes, nomeArquivo);
    }

    public byte[] getImagensBytesDecoded() {
        String base64Data = imagem;
        if (base64Data != null && base64Data.contains(",")) {
            base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
        }
        return Base64.getDecoder().decode(base64Data);
    }
}

