package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.domain.Ong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginOngResponseWebDTO {

    private UUID id;
    private String nome;
    private String email;

    public static LoginOngResponseWebDTO fromDomain(Ong ong) {
        return LoginOngResponseWebDTO.builder()
            .id(ong.getId())
            .nome(ong.getNome())
            .email(ong.getEmail())
            .build();
    }
}

