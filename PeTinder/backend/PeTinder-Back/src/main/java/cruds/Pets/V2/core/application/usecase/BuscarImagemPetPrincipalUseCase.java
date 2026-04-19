package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class BuscarImagemPetPrincipalUseCase {

    private final ImagemPetJpaRepository imagemPetRepository;
    private final ArmazenamentoImagemPetGateway armazenamentoImagemGateway;

    @Autowired
    public BuscarImagemPetPrincipalUseCase(ImagemPetJpaRepository imagemPetRepository,
                                           ArmazenamentoImagemPetGateway armazenamentoImagemGateway) {
        this.imagemPetRepository = imagemPetRepository;
        this.armazenamentoImagemGateway = armazenamentoImagemGateway;
    }

    public String buscarImagemPrincipal(UUID petId) {
        List<ImagemPetEntity> imagens = imagemPetRepository.findByPetId(petId);

        if (imagens == null || imagens.isEmpty()) {
            return null;
        }

        ImagemPetEntity primeiraImagem = imagens.get(0);

        try {
            ImagemPet imagem = armazenamentoImagemGateway.buscarPorKey(primeiraImagem.getKeyS3());

            if (imagem == null || imagem.getDados() == null) {
                return null;
            }

            return "data:image/jpeg;base64," +
                    Base64.getEncoder().encodeToString(imagem.getDados());

        } catch (Exception e) {
            return null;
        }
    }

}
