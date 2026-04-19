package cruds.Dashboard.V2.infrastructure.config;

import cruds.Dashboard.V2.core.adapter.DashboardGateway;
import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;
import cruds.Dashboard.V2.core.application.usecase.ListarPendenciasPetsUseCase;
import cruds.Dashboard.V2.core.application.usecase.ObterEstatisticasPetsUseCase;
import cruds.Dashboard.V2.core.application.usecase.ObterRankingPetsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashboardConfig {

    @Bean
    public ObterRankingPetsUseCase obterRankingPetsUseCase(PetDashboardGateway petDashboardGateway) {
        return new ObterRankingPetsUseCase(petDashboardGateway);
    }

    @Bean
    public ListarPendenciasPetsUseCase listarPendenciasPetsUseCase(PetDashboardGateway petDashboardGateway) {
        return new ListarPendenciasPetsUseCase(petDashboardGateway);
    }

    @Bean
    public ObterEstatisticasPetsUseCase obterEstatisticasPetsUseCase(PetDashboardGateway petDashboardGateway) {
        return new ObterEstatisticasPetsUseCase(petDashboardGateway);
    }
}

