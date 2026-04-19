package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class BuscarImagemPetUseCase {

    private final PetGateway petGateway;
    private final ImagemPetGateway imagemPetGateway;
    private final ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway;
    private final ImagemPetJpaRepository imagemPetJpaRepository;

    public BuscarImagemPetUseCase(PetGateway petGateway,
                                  ImagemPetGateway imagemPetGateway,
                                  ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway,
                                  ImagemPetJpaRepository imagemPetJpaRepository) {
        this.petGateway = petGateway;
        this.imagemPetGateway = imagemPetGateway;
        this.armazenamentoImagemPetGateway = armazenamentoImagemPetGateway;
        this.imagemPetJpaRepository = imagemPetJpaRepository;
    }

    public List<ImagemPet> listarImagensPet(UUID petId) {
        petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException.PetNaoEncontradoException(
                        "Pet com ID " + petId + " não encontrado"
                ));

        List<String> keys = imagemPetJpaRepository.findKeysByPetId(petId);

        if (keys.isEmpty()) {
            throw new PetException.ImagemNaoEncontradaException(
                    "Nenhuma imagem encontrada para o pet com ID " + petId
            );
        }

        return imagemPetGateway.buscarPorPetId(petId, keys);
    }

    public byte[] buscarImagemPorIndice(UUID petId, int indice) {
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

        ImagemPet imagem = pet.getImagens().get(indice);
        return imagem.getDados();
    }

    public List<String> listarUrlsImagens(UUID petId) {
        List<ImagemPet> imagens = listarImagensPet(petId);

        return imagens.stream()
                .filter(imagem -> imagem != null && imagem.temDados())
                .map(imagem -> {
                    String base64 = Base64.getEncoder().encodeToString(imagem.getDados());
                    return "data:image/jpeg;base64," + base64;
                })
                .toList();
    }
}
