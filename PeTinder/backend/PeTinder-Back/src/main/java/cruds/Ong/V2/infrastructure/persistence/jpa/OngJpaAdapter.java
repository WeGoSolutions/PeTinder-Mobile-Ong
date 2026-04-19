package cruds.Ong.V2.infrastructure.persistence.jpa;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.domain.Ong;
import cruds.Ong.V2.infrastructure.persistence.jpa.mapper.OngMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Primary
public class OngJpaAdapter implements OngGateway {

    private final OngJpaRepository repository;

    public OngJpaAdapter(OngJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Ong salvar(Ong ong) {
        OngEntity entity = OngMapper.toEntity(ong);
        OngEntity savedEntity = repository.save(entity);
        return OngMapper.toDomain(savedEntity);
    }

    @Override
    public Ong atualizar(Ong ong) {
        return salvar(ong); // JPA save funciona para insert e update
    }

    @Override
    public Optional<Ong> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(OngMapper::toDomain);
    }

    @Override
    public Optional<Ong> buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .map(OngMapper::toDomain);
    }

    @Override
    public List<Ong> listarTodas() {
        return repository.findAll().stream()
                .map(OngMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean emailJaExiste(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existePorId(UUID id) {
        return repository.existsById(id);
    }
}

