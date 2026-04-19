package cruds.Pets.V2.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pet {
    
    private UUID id;
    private String nome;
    private Double idade;
    private String porte;
    private Integer curtidas;
    private List<String> tags;
    private String descricao;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private Boolean isAdotado;
    private String sexo;
    private UUID ongId;
    private LocalDateTime dataCriacao;
    private List<ImagemPet> imagens;
    
    // Construtor principal
    public Pet(UUID id, String nome, Double idade, String porte, List<String> tags, 
               String descricao, Boolean isCastrado, Boolean isVermifugo, Boolean isVacinado, 
               String sexo, UUID ongId) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.porte = porte;
        this.curtidas = 0;
        this.tags = tags;
        this.descricao = descricao;
        this.isCastrado = isCastrado != null ? isCastrado : false;
        this.isVermifugo = isVermifugo != null ? isVermifugo : false;
        this.isVacinado = isVacinado != null ? isVacinado : false;
        this.isAdotado = false;
        this.sexo = sexo;
        this.ongId = ongId;
        this.dataCriacao = LocalDateTime.now();
        this.imagens = new ArrayList<>();
        validarDados();
    }
    
    // Construtor para criação (sem ID)
    public Pet(String nome, Double idade, String porte, List<String> tags, 
               String descricao, Boolean isCastrado, Boolean isVermifugo, Boolean isVacinado, 
               String sexo, UUID ongId) {
        this(null, nome, idade, porte, tags, descricao, isCastrado, isVermifugo, isVacinado, sexo, ongId);
    }

    public Pet(){}
    
    private void validarDados() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (idade == null || idade <= 0) {
            throw new IllegalArgumentException("Idade deve ser maior que zero");
        }
        if (porte == null || porte.trim().isEmpty()) {
            throw new IllegalArgumentException("Porte é obrigatório");
        }
        if (!List.of("Pequeno", "Médio", "Grande").contains(porte)) {
            throw new IllegalArgumentException("Porte deve ser Pequeno, Médio ou Grande");
        }
        if (sexo == null || (!sexo.equals("MACHO") && !sexo.equals("FEMEA"))) {
            throw new IllegalArgumentException("Sexo deve ser MACHO ou FEMEA");
        }
        if (ongId == null) {
            throw new IllegalArgumentException("ONG é obrigatória");
        }
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("Pet deve ter pelo menos uma tag");
        }
    }
    
    public void curtir() {
        this.curtidas = (this.curtidas == null ? 0 : this.curtidas) + 1;
    }
    
    public void descurtir() {
        if (this.curtidas != null && this.curtidas > 0) {
            this.curtidas = this.curtidas - 1;
        }
    }
    
    public void adotar() {
        this.isAdotado = true;
    }
    
    public void cancelarAdocao() {
        this.isAdotado = false;
    }
    
    public boolean estaDisponivel() {
        return this.isAdotado == null || !this.isAdotado;
    }
    
    public Pet comNovoId(UUID novoId) {
        Pet pet = new Pet(novoId, nome, idade, porte, tags, descricao, isCastrado, isVermifugo, isVacinado, sexo, ongId);
        pet.setCurtidas(curtidas);
        pet.setDataCriacao(dataCriacao);
        pet.setIsAdotado(isAdotado);
        pet.setImagens(new ArrayList<>(imagens != null ? imagens : new ArrayList<>()));
        return pet;
    }
    
    public void adicionarImagem(ImagemPet imagem) {
        if (this.imagens == null) {
            this.imagens = new ArrayList<>();
        }
        this.imagens.add(imagem);
    }
    
    public void removerImagem(int indice) {
        if (this.imagens != null && indice >= 0 && indice < this.imagens.size()) {
            this.imagens.remove(indice);
        }
    }
    
    public boolean temImagens() {
        return this.imagens != null && !this.imagens.isEmpty();
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public Double getIdade() { return idade; }
    public String getPorte() { return porte; }
    public Integer getCurtidas() { return curtidas; }
    public List<String> getTags() { return tags; }
    public String getDescricao() { return descricao; }
    public Boolean getIsCastrado() { return isCastrado; }
    public Boolean getIsVermifugo() { return isVermifugo; }
    public Boolean getIsVacinado() { return isVacinado; }
    public Boolean getIsAdotado() { return isAdotado; }
    public String getSexo() { return sexo; }
    public UUID getOngId() { return ongId; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public List<ImagemPet> getImagens() { return imagens; }
    
    // Setters necessários para persistência
    public void setId(UUID id) { this.id = id; }
    public void setCurtidas(Integer curtidas) { this.curtidas = curtidas; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setIsAdotado(Boolean isAdotado) { this.isAdotado = isAdotado; }
    public void setImagens(List<ImagemPet> imagens) { this.imagens = imagens; }
}