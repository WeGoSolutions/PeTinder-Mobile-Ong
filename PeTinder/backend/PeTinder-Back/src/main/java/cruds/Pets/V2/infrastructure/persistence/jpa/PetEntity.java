package cruds.Pets.V2.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    private String nome;

    private Double idade;

    private String porte;

    private Integer curtidas;

    @ElementCollection
    @CollectionTable(name = "pet_tags", joinColumns = @JoinColumn(name = "pet_id"))
    @Column(name = "tag")
    private List<String> tags;

    private String descricao;

    private Boolean isCastrado;

    private Boolean isVermifugo;

    private Boolean isVacinado;

    private Boolean isAdotado;

    private String sexo;

    @Column(name = "fk_ong", columnDefinition = "BINARY(16)")
    private UUID ongId;
}