package cruds.Pets.V2.core.application.command;

import java.util.List;
import java.util.UUID;

public class AtualizarPetCommand {

    private final UUID id;
    private final String nome;
    private final Double idade;
    private final String porte;
    private final List<String> tags;
    private final String descricao;
    private final Boolean isCastrado;
    private final Boolean isVermifugo;
    private final Boolean isVacinado;
    private final String sexo;

    public AtualizarPetCommand(UUID id, String nome, Double idade, String porte,
                               List<String> tags, String descricao, Boolean isCastrado,
                               Boolean isVermifugo, Boolean isVacinado, String sexo) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.porte = porte;
        this.tags = tags;
        this.descricao = descricao;
        this.isCastrado = isCastrado;
        this.isVermifugo = isVermifugo;
        this.isVacinado = isVacinado;
        this.sexo = sexo;
    }

    // Getters
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public Double getIdade() { return idade; }
    public String getPorte() { return porte; }
    public List<String> getTags() { return tags; }
    public String getDescricao() { return descricao; }
    public Boolean getIsCastrado() { return isCastrado; }
    public Boolean getIsVermifugo() { return isVermifugo; }
    public Boolean getIsVacinado() { return isVacinado; }
    public String getSexo() { return sexo; }
}