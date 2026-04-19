package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.core.domain.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UploadImagemPetUseCase {

    private final PetGateway petGateway;
    private final ImagemPetGateway imagemPetGateway;
    private final ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway;

    public UploadImagemPetUseCase(PetGateway petGateway,
                                  ImagemPetGateway imagemPetGateway,
                                  ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway) {
        this.petGateway = petGateway;
        this.imagemPetGateway = imagemPetGateway;
        this.armazenamentoImagemPetGateway = armazenamentoImagemPetGateway;
    }

    public Pet uploadImagens(UUID petId, List<byte[]> imagensBytes, List<String> nomesArquivos) {
        Pet pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + petId + " não encontrado"
                ));

        if (imagensBytes == null || imagensBytes.isEmpty()) {
            throw new PetException.ImagemInvalidaException("É necessário fornecer pelo menos uma imagem");
        }

        if (nomesArquivos == null || nomesArquivos.size() != imagensBytes.size()) {
            throw new PetException.ImagemInvalidaException("Número de nomes não confere com número de imagens");
        }

        List<ImagemPet> imagensSalvas = new ArrayList<>();

        for (int i = 0; i < imagensBytes.size(); i++) {
            byte[] dados = imagensBytes.get(i);
            String nomeArquivo = nomesArquivos.get(i);

            if (dados == null || dados.length == 0) {
                throw new PetException.ImagemInvalidaException("Imagem " + i + " está vazia");
            }

            ImagemPet imagemParaS3 = new ImagemPet(null, nomeArquivo, dados, null);
            String key = armazenamentoImagemPetGateway.salvarImagem(imagemParaS3, petId);

            ImagemPet imagemParaBanco = imagemParaS3;
            imagemParaBanco.setKeyS3(key);

            imagemPetGateway.salvar(imagemParaBanco, petId);
        }


        List<String> keys = imagemPetGateway.buscarKeysPorPetId(petId);
        pet.setImagens(imagemPetGateway.buscarPorPetId(petId, keys));

        return petGateway.atualizar(pet);
    }
}
