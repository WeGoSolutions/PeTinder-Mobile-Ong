package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.CriptografiaOngGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.CriarOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.EnderecoOng;
import cruds.Ong.V2.core.domain.Ong;

public class CriarOngUseCase {

    private final OngGateway ongGateway;
    private final CriptografiaOngGateway criptografiaGateway;

    public CriarOngUseCase(OngGateway ongGateway, CriptografiaOngGateway criptografiaGateway) {
        this.ongGateway = ongGateway;
        this.criptografiaGateway = criptografiaGateway;
    }

    public Ong cadastrar(CriarOngCommand command) {
        if (ongGateway.emailJaExiste(command.getEmail())) {
            throw new OngException.EmailJaExisteException(
                "Email já está em uso: " + command.getEmail()
            );
        }

        Ong ong = new Ong(
            command.getCnpj(),
            command.getCpf(),
            command.getNome(),
            command.getRazaoSocial(),
            command.getSenha(),
            command.getEmail(),
            command.getLink()
        );

        String senhaCriptografada = criptografiaGateway.criptografarSenha(ong.getSenha());
        ong.atualizarSenha(senhaCriptografada);

        if (command.getEndereco() != null) {
            EnderecoOng endereco = new EnderecoOng(
                command.getEndereco().getCep(),
                command.getEndereco().getRua(),
                command.getEndereco().getNumero(),
                command.getEndereco().getCidade(),
                command.getEndereco().getUf(),
                command.getEndereco().getComplemento()
            );
            ong.definirEndereco(endereco);
        }

        return ongGateway.salvar(ong);
    }
}
