package cruds.Users.V2.core.domain;

import java.util.Arrays;
import java.util.UUID;


public class ImagemUsuario {

    private final UUID id;
    private final byte[] dados;
    private final String nomeArquivo;

    public ImagemUsuario(UUID id, byte[] dados, String nomeArquivo) {
        this.id = id;
        this.dados = dados;
        this.nomeArquivo = nomeArquivo;
    }

    // Construtor para criação sem ID (usado nos commands)
    public ImagemUsuario(byte[] dados, String nomeArquivo) {
        this(null, dados, nomeArquivo);
    }

    // Getters
    public UUID getId() { return id; }
    public byte[] getDados() { return dados; }
    public String getNomeArquivo() { return nomeArquivo; }

    public ImagemUsuario comNovoId(UUID novoId) {
        return new ImagemUsuario(novoId, dados, nomeArquivo);
    }

    public boolean temImagem() {
        return dados != null && dados.length > 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ImagemUsuario imagem = (ImagemUsuario) obj;

        if (id != null ? !id.equals(imagem.id) : imagem.id != null) return false;
        if (!Arrays.equals(dados, imagem.dados)) return false;
        return nomeArquivo != null ? nomeArquivo.equals(imagem.nomeArquivo) : imagem.nomeArquivo == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(dados);
        result = 31 * result + (nomeArquivo != null ? nomeArquivo.hashCode() : 0);
        return result;
    }
}