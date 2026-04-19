package cruds.Pets.V2.infrastructure.web.dto;

import cruds.Pets.V2.core.application.command.AtualizarPetCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarPetWebDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    @Schema(description = "Nome do pet", example = "Rex")
    private String nome;

    @NotNull(message = "Idade é obrigatória")
    @Positive(message = "Idade deve ser maior que zero")
    @Schema(description = "Idade do pet", example = "2.5")
    private Double idade;

    @NotBlank(message = "Porte é obrigatório")
    @Pattern(regexp = "Pequeno|Médio|Grande", message = "Porte deve ser Pequeno, Médio ou Grande")
    @Schema(description = "Porte do pet", example = "Médio")
    private String porte;

    @NotEmpty(message = "Pet deve ter pelo menos uma tag")
    @Size(max = 10, message = "Máximo de 10 tags permitidas")
    @Schema(description = "Tags do pet", example = "[\"brincalhão\", \"carinhoso\"]")
    private List<String> tags;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Schema(description = "Descrição do pet", example = "Um cãozinho muito carinhoso e brincalhão")
    private String descricao;

    @Schema(description = "Pet castrado", example = "true")
    private Boolean isCastrado;

    @Schema(description = "Pet vermifugado", example = "true")
    private Boolean isVermifugo;

    @Schema(description = "Pet vacinado", example = "true")
    private Boolean isVacinado;

    @NotBlank(message = "Sexo é obrigatório")
    @Pattern(regexp = "MACHO|FEMEA", message = "Sexo deve ser MACHO ou FEMEA")
    @Schema(description = "Sexo do pet", example = "MACHO")
    private String sexo;

    public AtualizarPetCommand toCommand(UUID id) {
        return new AtualizarPetCommand(
                id,
                nome,
                idade,
                porte,
                tags,
                descricao,
                isCastrado,
                isVermifugo,
                isVacinado,
                sexo
        );
    }
}