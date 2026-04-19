package cruds.Imagem.repository;

import cruds.Imagem.entity.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImagemRepository extends JpaRepository<Imagem, UUID> {
}