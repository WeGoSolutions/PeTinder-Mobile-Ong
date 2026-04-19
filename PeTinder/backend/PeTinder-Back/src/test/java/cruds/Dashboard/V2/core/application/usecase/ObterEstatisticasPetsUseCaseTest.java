package cruds.Dashboard.V2.core.application.usecase;

import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;
import cruds.Dashboard.V2.core.application.exception.DashboardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObterEstatisticasPetsUseCaseTest {

    @Mock
    private PetDashboardGateway petDashboardGateway;

    private ObterEstatisticasPetsUseCase obterEstatisticasPetsUseCase;

    private UUID ongId;

    @BeforeEach
    void setUp() {
        obterEstatisticasPetsUseCase = new ObterEstatisticasPetsUseCase(petDashboardGateway);
        ongId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve obter estatísticas com sucesso")
    void deveObterEstatisticasComSucesso() {
        // Arrange
        when(petDashboardGateway.contarPetsAdotadosPorOngId(ongId)).thenReturn(5L);
        when(petDashboardGateway.contarPetsNaoAdotadosPorOngId(ongId)).thenReturn(10L);

        // Act
        ObterEstatisticasPetsUseCase.EstatisticasPets resultado = obterEstatisticasPetsUseCase.obterEstatisticas(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(5, resultado.getAdotados());
        assertEquals(10, resultado.getNaoAdotados());
        verify(petDashboardGateway).contarPetsAdotadosPorOngId(ongId);
        verify(petDashboardGateway).contarPetsNaoAdotadosPorOngId(ongId);
    }

    @Test
    @DisplayName("Deve obter estatísticas quando não há pets adotados")
    void deveObterEstatisticasQuandoNaoHaPetsAdotados() {
        // Arrange
        when(petDashboardGateway.contarPetsAdotadosPorOngId(ongId)).thenReturn(0L);
        when(petDashboardGateway.contarPetsNaoAdotadosPorOngId(ongId)).thenReturn(10L);

        // Act
        ObterEstatisticasPetsUseCase.EstatisticasPets resultado = obterEstatisticasPetsUseCase.obterEstatisticas(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.getAdotados());
        assertEquals(10, resultado.getNaoAdotados());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não tem pets")
    void deveLancarExcecaoQuandoOngNaoTemPets() {
        // Arrange
        when(petDashboardGateway.contarPetsAdotadosPorOngId(ongId)).thenReturn(0L);
        when(petDashboardGateway.contarPetsNaoAdotadosPorOngId(ongId)).thenReturn(0L);

        // Act & Assert
        DashboardException.NenhumPetEncontradoException exception = assertThrows(
                DashboardException.NenhumPetEncontradoException.class,
                () -> obterEstatisticasPetsUseCase.obterEstatisticas(ongId)
        );

        assertTrue(exception.getMessage().contains(ongId.toString()));
    }

    @Test
    @DisplayName("Deve obter estatísticas quando todos os pets são adotados")
    void deveObterEstatisticasQuandoTodosPetsSaoAdotados() {
        // Arrange
        when(petDashboardGateway.contarPetsAdotadosPorOngId(ongId)).thenReturn(15L);
        when(petDashboardGateway.contarPetsNaoAdotadosPorOngId(ongId)).thenReturn(0L);

        // Act
        ObterEstatisticasPetsUseCase.EstatisticasPets resultado = obterEstatisticasPetsUseCase.obterEstatisticas(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(15, resultado.getAdotados());
        assertEquals(0, resultado.getNaoAdotados());
    }
}
