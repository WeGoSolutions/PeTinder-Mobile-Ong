package cruds.Imagem.repository;

import cruds.Imagem.entity.ImagemOng;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImagemOngRepository extends JpaRepository<ImagemOng, UUID> {
}
