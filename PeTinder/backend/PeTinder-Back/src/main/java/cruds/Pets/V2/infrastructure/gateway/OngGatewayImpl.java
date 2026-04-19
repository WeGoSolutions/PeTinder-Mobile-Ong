package cruds.Pets.V2.infrastructure.gateway;

import cruds.Ong.V2.infrastructure.persistence.jpa.OngJpaRepository;
import cruds.Pets.V2.core.adapter.OngGateway;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OngGatewayImpl implements OngGateway {

    private final OngJpaRepository ongRepository;

    public OngGatewayImpl(OngJpaRepository ongRepository) {
        this.ongRepository = ongRepository;
    }

    @Override
    public boolean existePorId(UUID id) {
        return ongRepository.existsById(id);
    }
}