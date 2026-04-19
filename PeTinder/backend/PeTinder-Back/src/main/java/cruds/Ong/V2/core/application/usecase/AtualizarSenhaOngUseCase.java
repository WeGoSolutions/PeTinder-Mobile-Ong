package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.CriptografiaOngGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.AtualizarSenhaOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;

public class AtualizarSenhaOngUseCase {

    private final OngGateway ongGateway;
    private final CriptografiaOngGateway criptografiaGateway;

    public AtualizarSenhaOngUseCase(OngGateway ongGateway, CriptografiaOngGateway criptografiaGateway) {
        this.ongGateway = ongGateway;
        this.criptografiaGateway = criptografiaGateway;
    }

    public Ong atualizarSenha(AtualizarSenhaOngCommand command) {
        Ong ong = ongGateway.buscarPorId(command.getId())
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG não encontrada com id: " + command.getId()
            ));

        if (!criptografiaGateway.verificarSenha(command.getSenhaAtual(), ong.getSenha())) {
            throw new OngException.SenhaInvalidaException("Senha atual não confere");
        }

        String novaSenhaCriptografada = criptografiaGateway.criptografarSenha(command.getNovaSenha());
        ong.atualizarSenha(novaSenhaCriptografada);

        return ongGateway.atualizar(ong);
    }
}

