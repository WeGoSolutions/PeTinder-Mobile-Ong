package cruds.Pets.V2.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class PetStatus {
    
    private UUID id;
    private UUID petId;
    private UUID userId;
    private PetStatusEnum status;
    private LocalDateTime alteradoParaPending;
    private LocalDateTime dataCriacao;
    
    public PetStatus(UUID id, UUID petId, UUID userId, PetStatusEnum status, LocalDateTime alteradoParaPending) {
        this.id = id;
        this.petId = petId;
        this.userId = userId;
        this.status = status;
        this.alteradoParaPending = alteradoParaPending;
        this.dataCriacao = LocalDateTime.now();
        
        validarDados();
    }
    
    public PetStatus(UUID id, UUID petId, UUID userId, PetStatusEnum status, LocalDateTime alteradoParaPending, LocalDateTime dataCriacao) {
        this.id = id;
        this.petId = petId;
        this.userId = userId;
        this.status = status;
        this.alteradoParaPending = alteradoParaPending;
        this.dataCriacao = dataCriacao != null ? dataCriacao : LocalDateTime.now();
        
        validarDados();
    }
    
    public PetStatus(UUID petId, UUID userId, PetStatusEnum status) {
        this(null, petId, userId, status, null);
    }
    
    private void validarDados() {
        if (petId == null) {
            throw new IllegalArgumentException("Pet ID é obrigatório");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID é obrigatório");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status é obrigatório");
        }
    }
    
    public void alterarStatus(PetStatusEnum novoStatus) {
        if (novoStatus == PetStatusEnum.PENDING) {
            this.alteradoParaPending = LocalDateTime.now();
        }
        this.status = novoStatus;
    }
    
    public boolean isPending() {
        return status == PetStatusEnum.PENDING;
    }
    
    public boolean isLiked() {
        return status == PetStatusEnum.LIKED;
    }
    
    public boolean isAdopted() {
        return status == PetStatusEnum.ADOPTED;
    }
    
    // Getters
    public UUID getId() { return id; }
    public UUID getPetId() { return petId; }
    public UUID getUserId() { return userId; }
    public PetStatusEnum getStatus() { return status; }
    public LocalDateTime getAlteradoParaPending() { return alteradoParaPending; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    
    // Setters necessários para persistência
    public void setId(UUID id) { this.id = id; }
    public void setStatus(PetStatusEnum status) { this.status = status; }
    public void setAlteradoParaPending(LocalDateTime alteradoParaPending) { this.alteradoParaPending = alteradoParaPending; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}