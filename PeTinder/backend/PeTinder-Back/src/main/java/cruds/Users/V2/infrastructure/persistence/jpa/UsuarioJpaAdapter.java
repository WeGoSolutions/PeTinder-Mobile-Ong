package cruds.Users.V2.infrastructure.persistence.jpa;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.domain.Usuario;
import cruds.Users.V2.infrastructure.persistence.jpa.mapper.UsuarioMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Primary
public class UsuarioJpaAdapter implements UsuarioGateway {

    private final UsuarioJpaRepository repository;

    public UsuarioJpaAdapter(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        UsuarioEntity savedEntity = repository.save(entity);
        return UsuarioMapper.toDomain(savedEntity);
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        return salvar(usuario); // JPA save funciona para insert e update
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmailESenha(String email, String senha) {
        return repository.findByEmailAndSenha(email, senha)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        return repository.findAll().stream()
                .map(UsuarioMapper::toDomain)
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
    public boolean cpfJaExiste(String cpf) {
        return repository.existsByCpf(cpf);
    }

    @Override
    public boolean existePorId(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void removerTodos() {
        repository.deleteAll();
    }
}
