package cruds.Users.V2.infrastructure.web.dto;

import cruds.Users.V2.core.domain.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable(value = "user", key = "#id")
public class UsuarioResponseWebDTO {
    private UUID id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String cpf;
    private String cep;
    private String rua;
    private Integer numero;
    private String cidade;
    private String uf;
    private String complemento;
    private String imagemUrl;
    private Boolean usuarioNovo;

    public static UsuarioResponseWebDTO fromDomain(Usuario usuario) {
        UsuarioResponseWebDTO dto = UsuarioResponseWebDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataNascimento(usuario.getDataNascimento())
                .cpf(usuario.getCpf())
                .usuarioNovo(usuario.getUsuarioNovo())
                .build();

        if (usuario.getEndereco() != null) {
            dto.setCep(usuario.getEndereco().getCep());
            dto.setRua(usuario.getEndereco().getRua());
            dto.setNumero(usuario.getEndereco().getNumero());
            dto.setCidade(usuario.getEndereco().getCidade());
            dto.setUf(usuario.getEndereco().getUf());
            dto.setComplemento(usuario.getEndereco().getComplemento());
        }

        if (usuario.getImagemUsuario() != null && usuario.getImagemUsuario().temImagem()) {
            String base64Image = Base64.getEncoder().encodeToString(usuario.getImagemUsuario().getDados());
            dto.setImagemUrl("data:image/jpeg;base64," + base64Image);
        }

        return dto;
    }
}
