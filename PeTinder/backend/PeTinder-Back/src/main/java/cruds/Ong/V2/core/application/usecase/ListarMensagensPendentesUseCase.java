package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.MensagemPendenteGateway;

import java.util.List;
import java.util.UUID;

public class ListarMensagensPendentesUseCase {

    private final MensagemPendenteGateway mensagemPendenteGateway;

    public ListarMensagensPendentesUseCase(MensagemPendenteGateway mensagemPendenteGateway) {
        this.mensagemPendenteGateway = mensagemPendenteGateway;
    }

    public List<MensagemPendenteGateway.MensagemPendente> listar(UUID ongId) {
        return mensagemPendenteGateway.listarMensagensPendentes(ongId);
    }
}

