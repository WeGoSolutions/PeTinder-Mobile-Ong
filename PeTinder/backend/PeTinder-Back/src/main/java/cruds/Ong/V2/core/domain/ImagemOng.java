package cruds.Ong.V2.core.domain;

import java.util.UUID;

public class ImagemOng {

    private UUID id;
    private byte[] dados;
    private String arquivo;

    public ImagemOng(UUID id, byte[] dados, String arquivo) {
        this.id = id;
        this.dados = dados;
        this.arquivo = arquivo;
    }

    public ImagemOng(byte[] dados, String arquivo) {
        this(null, dados, arquivo);
    }

    public ImagemOng(){}

    public boolean temImagem() {
        return dados != null && dados.length > 0;
    }

    // Getters
    public UUID getId() { return id; }
    public byte[] getDados() { return dados; }
    public String getArquivo() { return arquivo; }

    public void setId(UUID id) { this.id = id; }
    public void setDados(byte[] dados) { this.dados = dados; }
    public void setArquivo(String arquivo) { this.arquivo = arquivo; }
}

