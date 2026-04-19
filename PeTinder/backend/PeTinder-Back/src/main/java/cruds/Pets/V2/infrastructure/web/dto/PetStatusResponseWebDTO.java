package cruds.Pets.V2.infrastructure.web.dto;

import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetStatusResponseWebDTO {

    private UUID petId;
    private String petNome;
    private UUID usuarioId;
    private String status;
    private String imageUrl;

    // V2 method
    public static PetStatusResponseWebDTO fromEntity(PetStatusEntity petStatus, PetEntity pet) {
        PetStatusResponseWebDTO dto = new PetStatusResponseWebDTO();
        dto.petId = petStatus.getPetId();
        dto.petNome = pet != null ? pet.getNome() : null;
        dto.usuarioId = petStatus.getUserId();
        dto.status = petStatus.getStatus().name();

        if (pet != null) {
            // Image URL generation can be added here if needed
            String base = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();
            dto.imageUrl = base + "/api/pets/" + pet.getId() + "/imagens/0";
        }

        return dto;
    }
}
