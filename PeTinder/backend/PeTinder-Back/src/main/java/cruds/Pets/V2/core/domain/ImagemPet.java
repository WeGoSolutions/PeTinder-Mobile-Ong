package cruds.Pets.V2.core.domain;

import java.util.UUID;

public class ImagemPet {

    private UUID id;

    private String nomeArquivo;
    private byte[] dados;

    private String keyS3;
    
    public ImagemPet(UUID id, String nomeArquivo, byte[] dados, String keyS3) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.dados = dados;
        this.keyS3 = keyS3;
    }
    
    public ImagemPet(String nomeArquivo, byte[] dados) {
        this(null, nomeArquivo, dados, null);
    }

    public ImagemPet(UUID id, String nomeArquivo, byte[] dados) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.dados = dados;
    }

    public ImagemPet(){}

    public boolean temDados() {
        return dados != null && dados.length > 0;
    }

    // Getters
    public UUID getId() { return id; }
    public String getNomeArquivo() { return nomeArquivo; }
    public byte[] getDados() { return dados; }
    public String getKeyS3() { return keyS3; }
    public String getKey() { return keyS3; }

    // Setters necessários para persistência
    public void setId(UUID id) { this.id = id; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public void setDados(byte[] dados) { this.dados = dados; }
    public void setKeyS3(String keyS3) { this.keyS3 = keyS3; }
    public void setKey(String key) {this.keyS3 = key;}
}