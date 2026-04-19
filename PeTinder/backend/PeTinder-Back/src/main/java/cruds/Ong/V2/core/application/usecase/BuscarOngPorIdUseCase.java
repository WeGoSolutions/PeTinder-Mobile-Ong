package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

public class BuscarOngPorIdUseCase {

    private final OngGateway ongGateway;

    public BuscarOngPorIdUseCase(OngGateway ongGateway) {
        this.ongGateway = ongGateway;
    }

    @Cacheable("ongPorId")
    public Ong buscar(UUID id) {
        return ongGateway.buscarPorId(id)
            .orElseThrow(() -> new OngException.OngNaoEncontradaException(
                "ONG não encontrada com id: " + id
            ));
    }
}

