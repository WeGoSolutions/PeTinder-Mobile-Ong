package cruds.Pets.V2.infrastructure.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PetResponseAdotanteWebDTO {
    private UUID petId;
    private String nomePet;
    private Double idadePet;
    private String portePet;
    private Integer curtidas;
    private String descricao;
    private List<String> tags;
    private Boolean isCastrado;
    private Boolean isVermifugo;
    private Boolean isVacinado;
    private String sexoPet;
    private UUID userId;
    private UUID imagemUsuarioId;
    private String imagemUrl;
    private String nomeUsuario;
    private String email;
    private LocalDate dataNascUsuario;
    private String cpf;
}
