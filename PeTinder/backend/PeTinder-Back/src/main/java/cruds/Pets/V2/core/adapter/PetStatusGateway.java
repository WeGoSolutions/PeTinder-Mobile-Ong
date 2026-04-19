package cruds.Pets.V2.core.adapter;

import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetStatusGateway {

    PetStatus salvar(PetStatus petStatus);
    
    PetStatus atualizar(PetStatus petStatus);

    Optional<PetStatus> buscarPorPetEUsuario(UUID petId, UUID userId);

    List<PetStatus> buscarPorUsuario(UUID userId);

    List<PetStatus> buscarPorPet(UUID petId);

    List<PetStatus> buscarPorUsuarioEStatus(UUID userId, PetStatusEnum status);

    List<PetStatus> buscarPorStatus(PetStatusEnum status);

    void remover(UUID id);
    
    void removerPorPetEUsuario(UUID petId, UUID userId);
    
    void removerPorPet(UUID petId);

    void removerPorUsuario(UUID userId);

    boolean existePorPetEUsuario(UUID petId, UUID userId);

    List<UUID> buscarPetsNaoInteragidosPorUsuario(UUID userId);
    
    List<PetStatus> buscarTodosLiked();
    
    List<PetStatus> buscarLikedPorUsuario(UUID userId);
}