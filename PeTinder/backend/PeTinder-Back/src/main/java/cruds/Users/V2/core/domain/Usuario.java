package cruds.Users.V2.core.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class Usuario {

    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;
    private String cpf;
    private Endereco endereco;
    private ImagemUsuario imagemUsuario;
    private Boolean usuarioNovo;

    // Construtor
    public Usuario(UUID id, String nome, String email, String senha,
                   LocalDate dataNascimento, String cpf, Boolean usuarioNovo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.usuarioNovo = usuarioNovo != null ? usuarioNovo : true;
        validarIdade();
        validarEmail();
        validarNome();
    }

    // Construtor para criação (sem ID)
    public Usuario(String nome, String email, String senha, LocalDate dataNascimento) {
        this(null, nome, email, senha, dataNascimento, null, true);
    }

    public Usuario(){}

    private void validarIdade() {
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }

        LocalDate hoje = LocalDate.now();
        Period periodo = Period.between(dataNascimento, hoje);
        int idade = periodo.getYears();

        System.out.println("DEBUG: Data nascimento: " + dataNascimento);
        System.out.println("DEBUG: Data hoje: " + hoje);
        System.out.println("DEBUG: Idade calculada: " + idade);

        if (idade < 21) {
            throw new IllegalArgumentException("Usuário deve ter mais de 21 anos. Idade atual: " + idade);
        }
    }

    private void validarEmail() {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email deve ter formato válido");
        }
    }

    private void validarNome() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getCpf() { return cpf; }
    public Endereco getEndereco() { return endereco; }
    public ImagemUsuario getImagemUsuario() { return imagemUsuario; }
    public Boolean getUsuarioNovo() { return usuarioNovo; }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setImagemUsuario(ImagemUsuario imagemUsuario) {
        this.imagemUsuario = imagemUsuario;
    }

    public Usuario comNovoId(UUID novoId) {
        Usuario usuario = new Usuario(novoId, nome, email, senha, dataNascimento, cpf, usuarioNovo);
        usuario.setEndereco(endereco);
        usuario.setImagemUsuario(imagemUsuario);
        return usuario;
    }

    public Usuario comNovoEndereco(Endereco novoEndereco) {
        Usuario usuario = new Usuario(id, nome, email, senha, dataNascimento, cpf, usuarioNovo);
        usuario.setEndereco(novoEndereco);
        usuario.setImagemUsuario(imagemUsuario);
        return usuario;
    }

    public Usuario comNovaImagem(ImagemUsuario novaImagem) {
        Usuario usuario = new Usuario(id, nome, email, senha, dataNascimento, cpf, usuarioNovo);
        usuario.setEndereco(endereco);
        usuario.setImagemUsuario(novaImagem);
        return usuario;
    }

    // Métodos adicionais necessários para o sistema
    public void setId(UUID id) {
        // Método para permitir definição do ID após criação (usado no mapper)
        // Em um domínio rico, normalmente seria imutável, mas necessário para persistência
        this.id = id;
    }

    public void atualizarEndereco(Endereco novoEndereco) {
        this.endereco = novoEndereco;
    }

    public void atualizarImagemUsuario(ImagemUsuario novaImagem) {
        this.imagemUsuario = novaImagem;
    }

    public void atualizarInformacoesOpcionais(String novoCpf, Endereco novoEndereco) {
        this.cpf = novoCpf;
        this.endereco = novoEndereco;
    }

    public void atualizarSenha(String novaSenha) {
        this.senha = novaSenha;
    }

    public void marcarComoUsuarioExperiente() {
        this.usuarioNovo = false;
    }
}