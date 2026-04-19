package cruds.Pets.V2.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Builder;

import java.util.UUID;

@Builder
@Entity
@Table(name = "imagem_pet")
public class ImagemPetEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "fk_pet")
    private UUID petId;

    @Column(name = "key_s3", nullable = false, unique = true)
    private String keyS3;

    public ImagemPetEntity() {}

    public ImagemPetEntity(UUID id, String nomeArquivo, UUID petId, String keyS3) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.petId = petId;
        this.keyS3 = keyS3;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }

    public String getKeyS3() { return keyS3; }
    public void setKeyS3(String keyS3) { this.keyS3 = keyS3; }
}