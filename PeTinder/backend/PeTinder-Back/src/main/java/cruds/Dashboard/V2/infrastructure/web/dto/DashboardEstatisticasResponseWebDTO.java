package cruds.Dashboard.V2.infrastructure.web.dto;

import cruds.Dashboard.V2.core.application.usecase.ObterEstatisticasPetsUseCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardEstatisticasResponseWebDTO {
    private int adotados;
    private int naoAdotados;

    public static DashboardEstatisticasResponseWebDTO fromDomain(
            ObterEstatisticasPetsUseCase.EstatisticasPets estatisticas) {
        return new DashboardEstatisticasResponseWebDTO(
            estatisticas.getAdotados(),
            estatisticas.getNaoAdotados()
        );
    }
}

