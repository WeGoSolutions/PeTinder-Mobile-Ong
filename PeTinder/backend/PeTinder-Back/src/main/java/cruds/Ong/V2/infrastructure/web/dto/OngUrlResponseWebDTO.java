package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.domain.Ong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.Base64;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable(value = "ongs", key = "#id")
public class OngUrlResponseWebDTO {

    private UUID id;
    private String imageUrl;

    public static OngUrlResponseWebDTO fromDomain(Ong ong) {
        OngUrlResponseWebDTO dto = OngUrlResponseWebDTO.builder()
                .id(ong.getId())
                .imageUrl(null)
                .build();

        if (ong.getImagemOng() != null && ong.getImagemOng().temImagem()) {
            String base64Image = Base64.getEncoder().encodeToString(ong.getImagemOng().getDados());
            dto.setImageUrl("data:image/jpeg;base64," + base64Image);
        }

        return dto;
    }
}

