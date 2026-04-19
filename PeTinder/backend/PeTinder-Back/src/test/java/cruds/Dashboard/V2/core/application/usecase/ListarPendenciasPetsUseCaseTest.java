package cruds.Dashboard.V2.core.application.usecase;

import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;
import cruds.Dashboard.V2.core.application.exception.DashboardException;
import cruds.Dashboard.V2.core.domain.PetDashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarPendenciasPetsUseCaseTest {

    @Mock
    private PetDashboardGateway petDashboardGateway;

    private ListarPendenciasPetsUseCase listarPendenciasPetsUseCase;

    private UUID ongId;

    @BeforeEach
    void setUp() {
        listarPendenciasPetsUseCase = new ListarPendenciasPetsUseCase(petDashboardGateway);
        ongId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve listar pets com pendências")
    void deveListarPetsComPendencias() {
        // Arrange
        PetDashboard petComPendencia = new PetDashboard(
                UUID.randomUUID(),
                "Rex",
                "Descrição",
                2.5,
                "Médio",
                "MACHO",
                false, // não castrado - pendência
                true,
                true,
                false,
                5
        );

        PetDashboard petSemPendencia = new PetDashboard(
                UUID.randomUUID(),
                "Luna",
                "Descrição",
                1.5,
                "Pequeno",
                "FEMEA",
                true,
                true,
                true,
                false,
                3
        );

        when(petDashboardGateway.listarPetsPorOngId(ongId))
                .thenReturn(Arrays.asList(petComPendencia, petSemPendencia));

        // Act
        List<PetDashboard> resultado = listarPendenciasPetsUseCase.listarPendencias(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Rex", resultado.get(0).getNome());
        assertTrue(resultado.get(0).temPendencias());
        verify(petDashboardGateway).listarPetsPorOngId(ongId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há pets com pendências")
    void deveRetornarListaVaziaQuandoNaoHaPetsComPendencias() {
        // Arrange
        PetDashboard petSemPendencia = new PetDashboard(
                UUID.randomUUID(),
                "Luna",
                "Descrição",
                1.5,
                "Pequeno",
                "FEMEA",
                true,
                true,
                true,
                false,
                3
        );

        when(petDashboardGateway.listarPetsPorOngId(ongId))
                .thenReturn(Arrays.asList(petSemPendencia));

        // Act
        List<PetDashboard> resultado = listarPendenciasPetsUseCase.listarPendencias(ongId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não tem pets")
    void deveLancarExcecaoQuandoOngNaoTemPets() {
        // Arrange
        when(petDashboardGateway.listarPetsPorOngId(ongId)).thenReturn(Collections.emptyList());

        // Act & Assert
        DashboardException.NenhumPetEncontradoException exception = assertThrows(
                DashboardException.NenhumPetEncontradoException.class,
                () -> listarPendenciasPetsUseCase.listarPendencias(ongId)
        );

        assertTrue(exception.getMessage().contains(ongId.toString()));
    }

    @Test
    @DisplayName("Deve identificar múltiplas pendências")
    void deveIdentificarMultiplasPendencias() {
        // Arrange
        PetDashboard petMultiplasPendencias = new PetDashboard(
                UUID.randomUUID(),
                "Rex",
                "Descrição",
                2.5,
                "Médio",
                "MACHO",
                false, // não castrado
                false, // não vermifugo
                false, // não vacinado
                false,
                5
        );

        when(petDashboardGateway.listarPetsPorOngId(ongId))
                .thenReturn(Arrays.asList(petMultiplasPendencias));

        // Act
        List<PetDashboard> resultado = listarPendenciasPetsUseCase.listarPendencias(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        List<String> pendencias = resultado.get(0).obterPendencias();
        assertEquals(3, pendencias.size());
        assertTrue(pendencias.contains("Castração"));
        assertTrue(pendencias.contains("Vermífugo"));
        assertTrue(pendencias.contains("Vacina"));
    }
}
