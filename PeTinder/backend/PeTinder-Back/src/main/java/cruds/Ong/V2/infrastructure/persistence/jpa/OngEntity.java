package cruds.Ong.V2.infrastructure.persistence.jpa;

import cruds.Ong.V2.infrastructure.persistence.jpa.ImagemOngEntity;
import cruds.Users.V2.infrastructure.persistence.jpa.EnderecoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ong")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OngEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nome")
    private String nome;

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "senha")
    private String senha;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_imagem_ong")
    private ImagemOngEntity imagemOng;

    @Column(name = "link")
    private String link;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_endereco")
    private EnderecoEntity endereco;
}
