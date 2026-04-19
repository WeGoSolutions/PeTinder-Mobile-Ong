package cruds.common.dto;

public class ImageUploadData {
    private byte[] imageBytes;
    private String nomeArquivo;
    private String extension;

    public ImageUploadData(byte[] imageBytes, String nomeArquivo, String extension) {
        this.imageBytes = imageBytes;
        this.nomeArquivo = nomeArquivo;
        this.extension = extension;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public String getExtension() {
        return extension;
    }
}