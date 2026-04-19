package cruds.Users.V2.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Temporal(TemporalType.DATE)
    private LocalDate dataNasc;

    @Column(unique = true)
    private String cpf;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_endereco", columnDefinition = "BINARY(16)")
    private EnderecoEntity endereco;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_imagem_usuario", columnDefinition = "BINARY(16)")
    private ImagemUsuarioEntity imagemUser;

    private Boolean userNovo;
}
