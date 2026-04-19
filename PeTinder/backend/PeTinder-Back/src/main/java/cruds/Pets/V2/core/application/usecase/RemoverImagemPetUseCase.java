package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.core.domain.Pet;

import java.util.UUID;

public class RemoverImagemPetUseCase {

    private final PetGateway petGateway;
    private final ImagemPetGateway imagemPetGateway;
    private final ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway;

    public RemoverImagemPetUseCase(PetGateway petGateway, 
                                   ImagemPetGateway imagemPetGateway,
                                   ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway) {
        this.petGateway = petGateway;
        this.imagemPetGateway = imagemPetGateway;
        this.armazenamentoImagemPetGateway = armazenamentoImagemPetGateway;
    }

    public void removerImagem(UUID petId, int indice) {
        Pet pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + petId + " não encontrado"
                ));

        if (!pet.temImagens()) {
            throw new PetException.ImagemNaoEncontradaException(
                    "Nenhuma imagem encontrada para o pet com ID " + petId
            );
        }

        if (indice < 0 || indice >= pet.getImagens().size()) {
            throw new PetException.ImagemNaoEncontradaException(
                    "Índice " + indice + " inválido para o pet com ID " + petId +
                    ". Total de imagens: " + pet.getImagens().size()
            );
        }

        ImagemPet imagemParaRemover = pet.getImagens().get(indice);
        
        // Remover do armazenamento físico
        if (imagemParaRemover.getNomeArquivo() != null) {
            armazenamentoImagemPetGateway.removerImagem(imagemParaRemover.getNomeArquivo(), imagemParaRemover.getId());
        }
        
        // Remover do banco de dados
        if (imagemParaRemover.getId() != null) {
            imagemPetGateway.remover(imagemParaRemover.getKeyS3());
        }
        
        // Remover da lista do pet
        pet.removerImagem(indice);
        
        // Atualizar pet
        petGateway.atualizar(pet);
    }
}