package cruds.config.token;

import cruds.Users.V2.infrastructure.persistence.jpa.UsuarioEntity;
import cruds.Users.V2.infrastructure.persistence.jpa.UsuarioJpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * V2 Adapter for Spring Security Authentication.
 * This bridges the gap between Spring Security's UserDetailsService and the V2 architecture.
 */
@Service
public class AutenticacaoServiceV2Adapter implements UserDetailsService {

    private final UsuarioJpaRepository usuarioJpaRepository;

    public AutenticacaoServiceV2Adapter(UsuarioJpaRepository usuarioJpaRepository) {
        this.usuarioJpaRepository = usuarioJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsuarioEntity> usuarioOpt = usuarioJpaRepository.findByEmail(username);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("usuario: %s nao encontrado", username));
        }

        UsuarioEntity usuario = usuarioOpt.get();
        
        // Return Spring Security UserDetails
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(Collections.emptyList())
                .build();
    }
}
