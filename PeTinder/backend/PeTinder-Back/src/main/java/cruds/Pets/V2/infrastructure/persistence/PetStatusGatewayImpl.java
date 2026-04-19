package cruds.Pets.V2.infrastructure.persistence;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.mapper.PetStatusMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PetStatusGatewayImpl implements PetStatusGateway {

    private final PetStatusJpaRepository repository;

    public PetStatusGatewayImpl(PetStatusJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public PetStatus salvar(PetStatus petStatus) {
        var entity = PetStatusMapper.toEntity(petStatus);
        var savedEntity = repository.save(entity);
        return PetStatusMapper.toDomain(savedEntity);
    }

    @Override
    public PetStatus atualizar(PetStatus petStatus) {
        var entity = PetStatusMapper.toEntity(petStatus);
        var updatedEntity = repository.save(entity);
        return PetStatusMapper.toDomain(updatedEntity);
    }

    @Override
    public Optional<PetStatus> buscarPorPetEUsuario(UUID petId, UUID userId) {
        return repository.findByPetIdAndUserId(petId, userId)
                .map(PetStatusMapper::toDomain);
    }

    @Override
    public List<PetStatus> buscarPorUsuario(UUID userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(PetStatusMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PetStatus> buscarPorPet(UUID petId) {
        return repository.findByPetId(petId)
                .stream()
                .map(PetStatusMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PetStatus> buscarPorUsuarioEStatus(UUID userId, PetStatusEnum status) {
        return repository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(PetStatusMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PetStatus> buscarPorStatus(PetStatusEnum status) {
        return repository.findByStatus(status)
                .stream()
                .map(PetStatusMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void removerPorPetEUsuario(UUID petId, UUID userId) {
        repository.deleteByPetIdAndUserId(petId, userId);
    }

    @Override
    @Transactional
    public void removerPorPet(UUID petId) {
        repository.deleteByPetId(petId);
    }

    @Override
    @Transactional
    public void removerPorUsuario(UUID userId) {repository.deleteByUserId(userId);}

    @Override
    public boolean existePorPetEUsuario(UUID petId, UUID userId) {
        return repository.existsByPetIdAndUserId(petId, userId);
    }

    @Override
    public List<UUID> buscarPetsNaoInteragidosPorUsuario(UUID userId) {
        return repository.findPetsNotInteractedByUser(userId);
    }
    
    @Override
    public List<PetStatus> buscarTodosLiked() {
        return repository.findAllLikedStatusPets()
                .stream()
                .map(PetStatusMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PetStatus> buscarLikedPorUsuario(UUID userId) {
        return repository.findLikedStatusPetsByUserId(userId)
                .stream()
                .map(PetStatusMapper::toDomain)
                .collect(Collectors.toList());
    }
}