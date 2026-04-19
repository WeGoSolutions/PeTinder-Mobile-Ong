package cruds.Imagem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "imagem_ong")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ImagemOng {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Lob
    private byte[] dados;

    private String arquivo;

    public ImagemOng(byte[] dados) {
        this.dados = dados;
    }
}
