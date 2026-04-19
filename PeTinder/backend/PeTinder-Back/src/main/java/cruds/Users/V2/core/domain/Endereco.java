package cruds.Users.V2.core.domain;

import java.util.UUID;

public class Endereco {
    
    private final UUID id;
    private final String cep;
    private final String rua;
    private final Integer numero;
    private final String cidade;
    private final String uf;
    private final String complemento;

    public Endereco(UUID id, String cep, String rua, Integer numero, 
                   String cidade, String uf, String complemento) {
        this.id = id;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.uf = uf;
        this.complemento = complemento;
        validarEndereco();
    }

    // Construtor para criação (sem ID)
    public Endereco(String cep, String rua, Integer numero, 
                   String cidade, String uf, String complemento) {
        this(null, cep, rua, numero, cidade, uf, complemento);
    }

    private void validarEndereco() {
        if (cep != null && !cep.matches("\\d{5}-?\\d{3}")) {
            throw new IllegalArgumentException("CEP deve ter formato válido (00000-000)");
        }
        
        if (uf != null && uf.length() != 2) {
            throw new IllegalArgumentException("UF deve ter 2 caracteres");
        }
    }

    // Getters
    public UUID getId() { return id; }
    public String getCep() { return cep; }
    public String getRua() { return rua; }
    public Integer getNumero() { return numero; }
    public String getCidade() { return cidade; }
    public String getUf() { return uf; }
    public String getComplemento() { return complemento; }

    public Endereco comNovoId(UUID novoId) {
        return new Endereco(novoId, cep, rua, numero, cidade, uf, complemento);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Endereco endereco = (Endereco) obj;
        
        return cep != null ? cep.equals(endereco.cep) : endereco.cep == null &&
               rua != null ? rua.equals(endereco.rua) : endereco.rua == null &&
               numero != null ? numero.equals(endereco.numero) : endereco.numero == null &&
               cidade != null ? cidade.equals(endereco.cidade) : endereco.cidade == null &&
               uf != null ? uf.equals(endereco.uf) : endereco.uf == null &&
               complemento != null ? complemento.equals(endereco.complemento) : endereco.complemento == null;
    }

    @Override
    public int hashCode() {
        int result = cep != null ? cep.hashCode() : 0;
        result = 31 * result + (rua != null ? rua.hashCode() : 0);
        result = 31 * result + (numero != null ? numero.hashCode() : 0);
        result = 31 * result + (cidade != null ? cidade.hashCode() : 0);
        result = 31 * result + (uf != null ? uf.hashCode() : 0);
        result = 31 * result + (complemento != null ? complemento.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "cep='" + cep + '\'' +
                ", rua='" + rua + '\'' +
                ", numero=" + numero +
                ", cidade='" + cidade + '\'' +
                ", uf='" + uf + '\'' +
                ", complemento='" + complemento + '\'' +
                '}';
    }
}
