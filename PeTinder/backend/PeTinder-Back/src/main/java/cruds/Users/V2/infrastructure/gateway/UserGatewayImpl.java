package cruds.Users.V2.infrastructure.gateway;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.domain.Usuario;
import org.springframework.cache.annotation.Cacheable;
import cruds.Users.V2.infrastructure.persistence.jpa.UsuarioEntity;
import cruds.Users.V2.infrastructure.persistence.jpa.UsuarioJpaRepository;
import cruds.Users.V2.infrastructure.persistence.jpa.mapper.UsuarioMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserGatewayImpl implements UsuarioGateway {

    private final UsuarioJpaRepository usuarioRepository;

    public UserGatewayImpl(UsuarioJpaRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        UsuarioEntity savedEntity = usuarioRepository.save(entity);
        return UsuarioMapper.toDomain(savedEntity);
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        return salvar(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorEmailESenha(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void remover(UUID id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean emailJaExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public boolean cpfJaExiste(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existePorId(UUID id) {
        return usuarioRepository.existsById(id);
    }

    @Override
    public void removerTodos() {
        usuarioRepository.deleteAll();
    }
}