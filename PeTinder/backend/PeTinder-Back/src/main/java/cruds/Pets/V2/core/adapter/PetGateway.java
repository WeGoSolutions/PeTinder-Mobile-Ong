package cruds.Pets.V2.core.adapter;

import cruds.Pets.V2.core.domain.Pet;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetGateway {

    Pet salvar(Pet pet);

    Pet atualizar(Pet pet);

    Optional<Pet> buscarPorId(UUID id);

    List<Pet> listarTodos();

    List<Pet> listarPorOng(UUID ongId);

    List<Pet> listarDisponiveis();

    List<Pet> listarPetsNaoInteragidosPorUsuario(UUID userId);

    void remover(UUID id);

    boolean existePorId(UUID id);

    void removerTodos();
}