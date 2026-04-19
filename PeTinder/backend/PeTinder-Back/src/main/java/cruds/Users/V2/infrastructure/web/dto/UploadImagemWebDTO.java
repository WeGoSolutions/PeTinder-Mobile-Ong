package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.application.command.UploadImagemCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadImagemWebDTO {

    @NotBlank
    private String imagemUsuario;

    public UploadImagemCommand toCommand(UUID usuarioId) {
        byte[] dadosImagem = getImagemDecodificada();
        String nomeArquivo = "user_" + usuarioId + "_perfil.jpg";
        return new UploadImagemCommand(usuarioId, dadosImagem, nomeArquivo);
    }

    public byte[] getImagemDecodificada() {
        String base64Data = imagemUsuario;
        if (base64Data.startsWith("data:")) {
            int commaIndex = base64Data.indexOf(",");
            if (commaIndex != -1) {
                base64Data = base64Data.substring(commaIndex + 1);
            }
        }
        return Base64.getDecoder().decode(base64Data);
    }
}