package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ListarPetsDisponivelParaUsuarioUseCase {

    private final PetGateway petGateway;
    private final UsuarioGateway userGateway;

    public ListarPetsDisponivelParaUsuarioUseCase(PetGateway petGateway, UsuarioGateway userGateway) {
        this.petGateway = petGateway;
        this.userGateway = userGateway;
    }

    public List<Pet> listarDisponiveis(UUID userId) {
        if (!userGateway.existePorId(userId)) {
            throw new PetException("Usuário com ID " + userId + " não encontrado");
        }

        return petGateway.listarDisponiveis()
                .stream()
                .filter(Pet::estaDisponivel)
                .collect(Collectors.toList());
    }
}