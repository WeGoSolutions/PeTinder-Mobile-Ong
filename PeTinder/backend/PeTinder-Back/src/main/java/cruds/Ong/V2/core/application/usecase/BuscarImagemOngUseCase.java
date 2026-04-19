package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.exception.OngException;

import java.util.UUID;

public class BuscarImagemOngUseCase {

    private final OngGateway ongGateway;

    public BuscarImagemOngUseCase(OngGateway ongGateway) {
        this.ongGateway = ongGateway;
    }

    public byte[] buscarPorIndice(UUID ongId, int indice) {
        var ong = ongGateway.buscarPorId(ongId)
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG não encontrada com id: " + ongId
            ));

        if (ong.getImagemOng() == null || indice != 0) {
            throw new OngException.ImagemInvalidaException(
                "Imagem não encontrada para a ONG com id " + ongId
            );
        }

        return ong.getImagemOng().getDados();
    }
}

