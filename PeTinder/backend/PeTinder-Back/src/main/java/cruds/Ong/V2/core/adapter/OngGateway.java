package cruds.Ong.V2.core.adapter;

import cruds.Ong.V2.core.domain.Ong;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OngGateway {

    Ong salvar(Ong ong);

    Ong atualizar(Ong ong);

    Optional<Ong> buscarPorId(UUID id);

    Optional<Ong> buscarPorEmail(String email);

    List<Ong> listarTodas();

    void remover(UUID id);

    boolean emailJaExiste(String email);

    boolean existePorId(UUID id);
}

