package cruds.Imagem.service;

import cruds.Ong.V2.infrastructure.persistence.jpa.ImagemOngEntity;
import cruds.Imagem.repository.ImagemOngRepository;
import cruds.Ong.V2.infrastructure.persistence.jpa.OngEntity;
import cruds.Ong.V2.infrastructure.persistence.jpa.OngJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagemOngService {

    @Autowired
    private ImagemOngRepository imagemOngRepository;

    @Autowired
    private OngJpaRepository ongRepository;

    public void salvarOngComImagem(OngEntity ong, ImagemOngEntity imagemOng) {
        // The ImagemOngRepository uses the old entity, but we can work around it
        // by creating the equivalent V1 entity for persistence
        cruds.Imagem.entity.ImagemOng imagemOngV1 = cruds.Imagem.entity.ImagemOng.builder()
                .id(imagemOng.getId())
                .dados(imagemOng.getDados())
                .arquivo(imagemOng.getArquivo())
                .build();
        cruds.Imagem.entity.ImagemOng imagemSalva = imagemOngRepository.save(imagemOngV1);
        
        // Convert back to V2
        ImagemOngEntity imagemOngV2 = ImagemOngEntity.builder()
                .id(imagemSalva.getId())
                .dados(imagemSalva.getDados())
                .arquivo(imagemSalva.getArquivo())
                .build();
        ong.setImagemOng(imagemOngV2);
        ongRepository.save(ong);
    }
}