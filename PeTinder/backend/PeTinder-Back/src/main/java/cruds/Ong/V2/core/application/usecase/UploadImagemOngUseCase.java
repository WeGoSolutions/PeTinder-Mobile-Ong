package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.ArmazenamentoImagemOngGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.UploadImagemOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.ImagemOng;
import cruds.Ong.V2.core.domain.Ong;

public class UploadImagemOngUseCase {

    private final OngGateway ongGateway;
    private final ArmazenamentoImagemOngGateway armazenamentoGateway;

    public UploadImagemOngUseCase(OngGateway ongGateway,
                                  ArmazenamentoImagemOngGateway armazenamentoGateway) {
        this.ongGateway = ongGateway;
        this.armazenamentoGateway = armazenamentoGateway;
    }

    public Ong executar(UploadImagemOngCommand command) {
        Ong ong = ongGateway.buscarPorId(command.getOngId())
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG não encontrada com id: " + command.getOngId()
            ));

        ImagemOng imagemOng = command.criarImagemOng();

        if (ong.getImagemOng() != null && ong.getImagemOng().temImagem()) {
            try {
                armazenamentoGateway.removerImagem(ong.getImagemOng().getArquivo(), ong.getImagemOng().getId());
            } catch (Exception e) {
                System.err.println("Erro ao remover imagem anterior: " + e.getMessage());
            }
        }

        armazenamentoGateway.salvarImagem(imagemOng);

        ong.definirImagem(imagemOng);

        return ongGateway.atualizar(ong);

    }
}


