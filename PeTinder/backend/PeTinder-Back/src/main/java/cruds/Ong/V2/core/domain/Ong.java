package cruds.Ong.V2.core.domain;

import java.util.UUID;

public class Ong {

    private UUID id;
    private String cnpj;
    private String cpf;
    private String nome;
    private String razaoSocial;
    private String senha;
    private String email;
    private String link;
    private EnderecoOng endereco;
    private ImagemOng imagemOng;

    // Construtor completo
    public Ong(UUID id, String cnpj, String cpf, String nome, String razaoSocial,
               String senha, String email, String link) {
        this.id = id;
        this.cnpj = cnpj;
        this.cpf = cpf;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.senha = senha;
        this.email = email;
        this.link = link;
        validarDados();
    }

    // Construtor para criação (sem ID)
    public Ong(String cnpj, String cpf, String nome, String razaoSocial,
               String senha, String email, String link) {
        this(null, cnpj, cpf, nome, razaoSocial, senha, email, link);
    }

    public Ong(){}

    private void validarDados() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        validarEmail();
        // Validar razaoSocial apenas na criação
        if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("Razão social é obrigatória");
        }
    }

    private void validarEmail() {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email deve ter formato válido");
        }
    }

    public void atualizarSenha(String novaSenha) {
        this.senha = novaSenha;
    }

    public void atualizarDados(String cnpj, String cpf, String nome, String razaoSocial,
                               String email, String link) {
        if (nome != null && !nome.trim().isEmpty()) {
            if (nome.length() < 3) {
                throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
            }
            this.nome = nome;
        }

        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
            validarEmail();
        }

        // Campos opcionais - apenas atualiza se fornecidos
        this.cnpj = cnpj;
        this.cpf = cpf;

        // razaoSocial é opcional na atualização
        if (razaoSocial != null && !razaoSocial.trim().isEmpty()) {
            this.razaoSocial = razaoSocial;
        }

        this.link = link;
    }

    public void definirEndereco(EnderecoOng endereco) {
        this.endereco = endereco;
    }

    public void definirImagem(ImagemOng imagem) {
        this.imagemOng = imagem;
    }

    // Getters
    public UUID getId() { return id; }
    public String getCnpj() { return cnpj; }
    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getRazaoSocial() { return razaoSocial; }
    public String getSenha() { return senha; }
    public String getEmail() { return email; }
    public String getLink() { return link; }
    public EnderecoOng getEndereco() { return endereco; }
    public ImagemOng getImagemOng() { return imagemOng; }

    public void setId(UUID id) { this.id = id; }
}
