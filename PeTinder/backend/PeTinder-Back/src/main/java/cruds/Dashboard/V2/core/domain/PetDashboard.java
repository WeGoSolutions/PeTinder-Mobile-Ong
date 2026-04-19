package cruds.Dashboard.V2.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PetDashboard {

    private UUID id;
    private String nome;
    private String descricao;
    private Double idade;
    private String porte;
    private String sexo;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private Boolean isAdopted;
    private Integer curtidas;

    public PetDashboard(UUID id, String nome, String descricao, Double idade,
                       String porte, String sexo, Boolean isCastrado,
                       Boolean isVermifugo, Boolean isVacinado, Boolean isAdopted,
                       Integer curtidas) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.idade = idade;
        this.porte = porte;
        this.sexo = sexo;
        this.isCastrado = isCastrado;
        this.isVermifugo = isVermifugo;
        this.isVacinado = isVacinado;
        this.isAdopted = isAdopted;
        this.curtidas = curtidas;
    }

    public List<String> obterPendencias() {
        List<String> faltas = new ArrayList<>();
        if (Boolean.FALSE.equals(isCastrado)) {
            faltas.add("Castração");
        }
        if (Boolean.FALSE.equals(isVermifugo)) {
            faltas.add("Vermífugo");
        }
        if (Boolean.FALSE.equals(isVacinado)) {
            faltas.add("Vacina");
        }
        return faltas;
    }

    public boolean temPendencias() {
        return !obterPendencias().isEmpty();
    }

    // Getters
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Double getIdade() { return idade; }
    public String getPorte() { return porte; }
    public String getSexo() { return sexo; }
    public Boolean getIsCastrado() { return isCastrado; }
    public Boolean getIsVermifugo() { return isVermifugo; }
    public Boolean getIsVacinado() { return isVacinado; }
    public Boolean getIsAdopted() { return isAdopted; }
    public Integer getCurtidas() { return curtidas; }
}
