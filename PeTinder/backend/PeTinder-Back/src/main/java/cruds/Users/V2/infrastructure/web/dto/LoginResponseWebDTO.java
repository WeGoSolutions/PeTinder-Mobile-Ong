package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.application.usecase.LoginUsuarioUseCase;
import cruds.Users.V2.core.domain.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseWebDTO {
    private UUID id;
    private String nome;
    private String email;
    private String token;
    private Boolean userNovo;

    public static LoginResponseWebDTO fromResult(LoginUsuarioUseCase.LoginResult result) {
        Usuario usuario = result.getUsuario();
        return LoginResponseWebDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .token(result.getToken())
                .userNovo(usuario.getUsuarioNovo())
                .build();
    }
}
