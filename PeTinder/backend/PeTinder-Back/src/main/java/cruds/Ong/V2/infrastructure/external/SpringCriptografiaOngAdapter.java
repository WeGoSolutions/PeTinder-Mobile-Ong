package cruds.Ong.V2.infrastructure.external;

import cruds.Ong.V2.core.adapter.CriptografiaOngGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringCriptografiaOngAdapter implements CriptografiaOngGateway {

    private final PasswordEncoder passwordEncoder;

    public SpringCriptografiaOngAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    @Override
    public boolean verificarSenha(String senhaTexto, String senhaCriptografada) {
        return passwordEncoder.matches(senhaTexto, senhaCriptografada);
    }
}

