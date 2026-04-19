package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.command.AtualizarPetCommand;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;

public class AtualizarPetUseCase {

    private final PetGateway petGateway;

    public AtualizarPetUseCase(PetGateway petGateway) {
        this.petGateway = petGateway;
    }

    public Pet atualizar(AtualizarPetCommand command) {
        Pet petExistente = petGateway.buscarPorId(command.getId())
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + command.getId() + " não encontrado"
                ));

        // Criar novo pet com dados atualizados
        Pet petAtualizado = new Pet(
                command.getId(),
                command.getNome(),
                command.getIdade(),
                command.getPorte(),
                command.getTags(),
                command.getDescricao(),
                command.getIsCastrado(),
                command.getIsVermifugo(),
                command.getIsVacinado(),
                command.getSexo(),
                petExistente.getOngId()
        );

        // Preservar dados que não devem ser alterados
        petAtualizado.setCurtidas(petExistente.getCurtidas());
        petAtualizado.setImagens(petExistente.getImagens());
        petAtualizado.setDataCriacao(petExistente.getDataCriacao());
        petAtualizado.setIsAdotado(petExistente.getIsAdotado());

        return petGateway.atualizar(petAtualizado);
    }
}