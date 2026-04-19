package cruds.Users.V2.infrastructure.external;

import cruds.Users.V2.core.adapter.AutenticacaoGateway;
import cruds.config.token.GerenciadorTokenJwt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SpringAutenticacaoAdapter implements AutenticacaoGateway {

    private final AuthenticationManager authenticationManager;
    private final GerenciadorTokenJwt gerenciadorTokenJwt;

    public SpringAutenticacaoAdapter(AuthenticationManager authenticationManager,
                                   GerenciadorTokenJwt gerenciadorTokenJwt) {
        this.authenticationManager = authenticationManager;
        this.gerenciadorTokenJwt = gerenciadorTokenJwt;
    }

    @Override
    public boolean autenticar(String email, String senha) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
            );
            return auth.isAuthenticated();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String gerarToken(String email) {
        Authentication auth = new UsernamePasswordAuthenticationToken(email, null);
        return gerenciadorTokenJwt.generateToken(auth);
    }

    @Override
    public boolean validarToken(String token) {
        try {
            String email = gerenciadorTokenJwt.getUsernameFromToken(token);
            org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                    email, "", java.util.Collections.emptyList());
            return gerenciadorTokenJwt.validateToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extrairEmailDoToken(String token) {
        return gerenciadorTokenJwt.getUsernameFromToken(token);
    }
}
