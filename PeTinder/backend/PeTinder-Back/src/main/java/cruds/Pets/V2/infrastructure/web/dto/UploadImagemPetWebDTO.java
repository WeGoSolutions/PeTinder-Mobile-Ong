package cruds.Pets.V2.infrastructure.web.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UploadImagemPetWebDTO {
    
    @NotEmpty(message = "É necessário fornecer pelo menos uma imagem")
    private List<String> imagensBase64;
    
    private List<String> nomesArquivos;
    
    public UploadImagemPetWebDTO() {}
    
    public UploadImagemPetWebDTO(List<String> imagensBase64, List<String> nomesArquivos) {
        this.imagensBase64 = imagensBase64;
        this.nomesArquivos = nomesArquivos;
    }
    
    // Conversão de Base64 para bytes
    public List<byte[]> getImagensBytes() {
        if (imagensBase64 == null || imagensBase64.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma imagem Base64 fornecida");
        }

        return imagensBase64.stream()
                .map(base64 -> {
                    try {
                        if (base64 == null || base64.isBlank()) {
                            throw new IllegalArgumentException("Imagem Base64 vazia");
                        }

                        String cleanBase64 = base64
                                .replaceFirst("^data:image/[^;]+;base64,", "")
                                .replaceAll("\\s+", "");

                        byte[] bytes = java.util.Base64.getDecoder().decode(cleanBase64);

                        if (bytes == null || bytes.length == 0) {
                            throw new IllegalArgumentException("Falha ao decodificar imagem Base64");
                        }

                        return bytes;
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Formato de imagem Base64 inválido", e);
                    }
                })
                .toList();
    }
    
    public List<String> getImagensBase64() { return imagensBase64; }
    public void setImagensBase64(List<String> imagensBase64) { this.imagensBase64 = imagensBase64; }
    
    public List<String> getNomesArquivos() { return nomesArquivos; }
    public void setNomesArquivos(List<String> nomesArquivos) { this.nomesArquivos = nomesArquivos; }
}