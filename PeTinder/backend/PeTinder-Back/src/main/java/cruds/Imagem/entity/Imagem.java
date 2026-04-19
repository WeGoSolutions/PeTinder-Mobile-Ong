package cruds.Imagem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "imagem_pet")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Imagem {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "link")
    private String caminho;

    @ManyToOne
    @JoinColumn(name = "fk_pet")
    @JsonBackReference
    private PetEntity pet;

    public Imagem() {}

    public Imagem(String caminho) {
        this.caminho = caminho;
    }

    public Imagem(String caminho, PetEntity pet) {
        this.caminho = caminho;
        this.pet = pet;
    }

    public Imagem(UUID id, byte[] bytes, Object pet) {
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public void setPet(PetEntity pet) {
        this.pet = pet;
    }
}