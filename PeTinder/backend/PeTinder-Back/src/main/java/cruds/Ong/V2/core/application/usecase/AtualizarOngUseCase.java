package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.AtualizarOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.EnderecoOng;
import cruds.Ong.V2.core.domain.Ong;

public class AtualizarOngUseCase {

    private final OngGateway ongGateway;

    public AtualizarOngUseCase(OngGateway ongGateway) {
        this.ongGateway = ongGateway;
    }

    public Ong atualizar(AtualizarOngCommand command) {
        Ong ong = ongGateway.buscarPorId(command.getId())
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG não encontrada com id: " + command.getId()
            ));

        // Verifica se o email está sendo alterado e se já existe
        if (!ong.getEmail().equals(command.getEmail()) &&
            ongGateway.emailJaExiste(command.getEmail())) {
            throw new OngException.EmailJaExisteException(
                "Email já está em uso: " + command.getEmail()
            );
        }

        ong.atualizarDados(
            command.getCnpj(),
            command.getCpf(),
            command.getNome(),
            command.getRazaoSocial(),
            command.getEmail(),
            command.getLink()
        );

        if (command.getEndereco() != null) {
            EnderecoOng endereco;
            if (ong.getEndereco() != null) {
                endereco = new EnderecoOng(
                    ong.getEndereco().getId(),
                    command.getEndereco().getCep(),
                    command.getEndereco().getRua(),
                    command.getEndereco().getNumero(),
                    command.getEndereco().getCidade(),
                    command.getEndereco().getUf(),
                    command.getEndereco().getComplemento()
                );
            } else {
                endereco = new EnderecoOng(
                    command.getEndereco().getCep(),
                    command.getEndereco().getRua(),
                    command.getEndereco().getNumero(),
                    command.getEndereco().getCidade(),
                    command.getEndereco().getUf(),
                    command.getEndereco().getComplemento()
                );
            }
            ong.definirEndereco(endereco);
        }

        return ongGateway.atualizar(ong);
    }
}

