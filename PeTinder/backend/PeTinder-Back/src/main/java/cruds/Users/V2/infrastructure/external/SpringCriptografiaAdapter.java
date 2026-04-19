package cruds.Users.V2.infrastructure.external;

import cruds.Users.V2.core.adapter.CriptografiaGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringCriptografiaAdapter implements CriptografiaGateway {

    private final PasswordEncoder passwordEncoder;

    public SpringCriptografiaAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String criptografarSenha(String senhaPlana) {
        return passwordEncoder.encode(senhaPlana);
    }

    @Override
    public boolean verificarSenha(String senhaPlana, String senhaCriptografada) {
        return passwordEncoder.matches(senhaPlana, senhaCriptografada);
    }
}
