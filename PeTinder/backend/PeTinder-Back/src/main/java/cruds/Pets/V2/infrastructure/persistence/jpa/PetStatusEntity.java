package cruds.Pets.V2.infrastructure.persistence.jpa;

import cruds.Pets.V2.core.domain.PetStatusEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pet_status")
public class PetStatusEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "fk_pet")
    private UUID petId;

    @Column(name = "fk_usuario")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private PetStatusEnum status;

    private LocalDateTime alteradoParaPending;
    
    private LocalDateTime dataCriacao;

    public PetStatusEntity() {}

    public PetStatusEntity(UUID id, UUID petId, UUID userId, PetStatusEnum status, 
                          LocalDateTime alteradoParaPending, LocalDateTime dataCriacao) {
        this.id = id;
        this.petId = petId;
        this.userId = userId;
        this.status = status;
        this.alteradoParaPending = alteradoParaPending;
        this.dataCriacao = dataCriacao;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPetId() { return petId; }
    public void setPetId(UUID petId) { this.petId = petId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public PetStatusEnum getStatus() { return status; }
    public void setStatus(PetStatusEnum status) { this.status = status; }

    public LocalDateTime getAlteradoParaPending() { return alteradoParaPending; }
    public void setAlteradoParaPending(LocalDateTime alteradoParaPending) { this.alteradoParaPending = alteradoParaPending; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}