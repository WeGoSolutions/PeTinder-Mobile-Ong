package cruds.common.util;

import cruds.common.dto.ImageUploadData;
import cruds.common.exception.BadRequestException;
import java.util.Base64;

public class ImageUploadUtil {

    public static ImageUploadData parseImageUploadRequest(String imagensBytes, String nomeArquivo) {
        if (imagensBytes == null || nomeArquivo == null) {
            throw new BadRequestException("Imagem ou nome do arquivo não foram informados.");
        }

        if (!imagensBytes.startsWith("data:image/")) {
            imagensBytes = "data:image/jpeg;base64," + imagensBytes;
        }

        String extension = "";
        String dadosBase64 = imagensBytes;

        if (dadosBase64.startsWith("data:image/")) {
            int semicolon = dadosBase64.indexOf(";");
            if (semicolon > 5) {
                String mediaType = dadosBase64.substring(5, semicolon);
                int slash = mediaType.indexOf("/");
                if (slash > -1 && slash < mediaType.length() - 1) {
                    extension = mediaType.substring(slash + 1);
                }
            }
            dadosBase64 = dadosBase64.substring(dadosBase64.indexOf(",") + 1);
        }

        if (!extension.isEmpty() && !nomeArquivo.toLowerCase().endsWith("." + extension.toLowerCase())) {
            nomeArquivo = nomeArquivo + "." + extension;
        }

        byte[] imageBytes = Base64.getDecoder().decode(dadosBase64);
        return new ImageUploadData(imageBytes, nomeArquivo, extension);
    }
}