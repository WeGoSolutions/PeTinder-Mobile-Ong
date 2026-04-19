package cruds.Ong.V2.core.domain;

import java.util.UUID;

public class EnderecoOng {

    private UUID id;
    private String cep;
    private String rua;
    private String numero;
    private String cidade;
    private String uf;
    private String complemento;

    public EnderecoOng(UUID id, String cep, String rua, String numero,
                       String cidade, String uf, String complemento) {
        this.id = id;
        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.uf = uf;
        this.complemento = complemento;
    }

    public EnderecoOng(String cep, String rua, String numero,
                       String cidade, String uf, String complemento) {
        this(null, cep, rua, numero, cidade, uf, complemento);
    }

    public EnderecoOng() {}

    // Getters
    public UUID getId() { return id; }
    public String getCep() { return cep; }
    public String getRua() { return rua; }
    public String getNumero() { return numero; }
    public String getCidade() { return cidade; }
    public String getUf() { return uf; }
    public String getComplemento() { return complemento; }

    public void setId(UUID id) { this.id = id; }
}

