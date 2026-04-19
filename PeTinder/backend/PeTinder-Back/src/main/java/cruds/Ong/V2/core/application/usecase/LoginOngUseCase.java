package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.CriptografiaOngGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.LoginOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;

public class LoginOngUseCase {

    private final OngGateway ongGateway;
    private final CriptografiaOngGateway criptografiaGateway;

    public LoginOngUseCase(OngGateway ongGateway, CriptografiaOngGateway criptografiaGateway) {
        this.ongGateway = ongGateway;
        this.criptografiaGateway = criptografiaGateway;
    }

    public Ong logar(LoginOngCommand command) {
        Ong ong = ongGateway.buscarPorEmail(command.getEmail())
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "Email não encontrado: " + command.getEmail()
            ));

        if (!criptografiaGateway.verificarSenha(command.getSenha(), ong.getSenha())) {
            throw new OngException.SenhaInvalidaException("Senha inválida");
        }

        return ong;
    }
}

