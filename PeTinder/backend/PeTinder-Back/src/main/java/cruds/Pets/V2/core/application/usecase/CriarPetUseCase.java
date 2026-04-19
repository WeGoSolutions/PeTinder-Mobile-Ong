package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.OngGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.command.CriarPetCommand;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;

public class CriarPetUseCase {

    private final PetGateway petGateway;
    private final OngGateway ongGateway;

    public CriarPetUseCase(PetGateway petGateway, OngGateway ongGateway) {
        this.petGateway = petGateway;
        this.ongGateway = ongGateway;
    }

    public Pet cadastrar(CriarPetCommand command) {
        // Validar se ONG existe
        if (!ongGateway.existePorId(command.getOngId())) {
            throw new PetException.OngNaoEncontradaException(
                    "ONG com ID " + command.getOngId() + " não encontrada"
            );
        }

        // Criar domínio Pet
        Pet pet = new Pet(
                command.getNome(),
                command.getIdade(),
                command.getPorte(),
                command.getTags(),
                command.getDescricao(),
                command.getIsCastrado(),
                command.getIsVermifugo(),
                command.getIsVacinado(),
                command.getSexo(),
                command.getOngId()
        );


        // Salvar pet
        return petGateway.salvar(pet);
    }
}