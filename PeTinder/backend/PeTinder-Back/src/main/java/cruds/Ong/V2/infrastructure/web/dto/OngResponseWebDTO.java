package cruds.Ong.V2.infrastructure.web.dto;

import cruds.Ong.V2.core.domain.Ong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable(value = "ongs", key = "#id")
public class OngResponseWebDTO {

    private UUID id;
    private String cnpj;
    private String cpf;
    private String nome;
    private String razaoSocial;
    private String email;
    private String link;
    private EnderecoResponseWebDTO endereco;

    public static OngResponseWebDTO fromDomain(Ong ong) {
        if (ong == null) return null;

        EnderecoResponseWebDTO enderecoDTO = null;
        if (ong.getEndereco() != null) {
            enderecoDTO = new EnderecoResponseWebDTO(
                ong.getEndereco().getId(),
                ong.getEndereco().getCep(),
                ong.getEndereco().getRua(),
                ong.getEndereco().getNumero(),
                ong.getEndereco().getCidade(),
                ong.getEndereco().getUf(),
                ong.getEndereco().getComplemento()
            );
        }

        return OngResponseWebDTO.builder()
            .id(ong.getId())
            .cnpj(ong.getCnpj())
            .cpf(ong.getCpf())
            .nome(ong.getNome())
            .razaoSocial(ong.getRazaoSocial())
            .email(ong.getEmail())
            .link(ong.getLink())
            .endereco(enderecoDTO)
            .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnderecoResponseWebDTO {
        private UUID id;
        private String cep;
        private String rua;
        private String numero;
        private String cidade;
        private String uf;
        private String complemento;
    }
}

