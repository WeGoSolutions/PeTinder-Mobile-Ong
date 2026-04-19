package cruds.Users.V2.core.adapter;

import cruds.Users.V2.core.domain.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {

    Usuario salvar(Usuario usuario);

    Usuario atualizar(Usuario usuario);

    Optional<Usuario> buscarPorId(UUID id);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorEmailESenha(String email, String senha);

    List<Usuario> listarTodos();

    void remover(UUID id);

    boolean emailJaExiste(String email);

    boolean cpfJaExiste(String cpf);

    boolean existePorId(UUID id);

    void removerTodos();
}
