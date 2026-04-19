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
class ObterRankingPetsUseCaseTest {

    @Mock
    private PetDashboardGateway petDashboardGateway;

    private ObterRankingPetsUseCase obterRankingPetsUseCase;

    private UUID ongId;

    @BeforeEach
    void setUp() {
        obterRankingPetsUseCase = new ObterRankingPetsUseCase(petDashboardGateway);
        ongId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve obter ranking de pets ordenado por curtidas")
    void deveObterRankingDePetsOrdenadoPorCurtidas() {
        // Arrange
        PetDashboard pet1 = new PetDashboard(
                UUID.randomUUID(),
                "Rex",
                "Descrição",
                2.5,
                "Médio",
                "MACHO",
                true,
                true,
                true,
                false,
                15 // mais curtidas
        );

        PetDashboard pet2 = new PetDashboard(
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
                10
        );

        PetDashboard pet3 = new PetDashboard(
                UUID.randomUUID(),
                "Max",
                "Descrição",
                3.0,
                "Grande",
                "MACHO",
                true,
                true,
                true,
                false,
                5 // menos curtidas
        );

        when(petDashboardGateway.listarPetsPorOngIdOrderByCurtidas(ongId))
                .thenReturn(Arrays.asList(pet1, pet2, pet3));

        // Act
        List<PetDashboard> resultado = obterRankingPetsUseCase.obterRanking(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("Rex", resultado.get(0).getNome());
        assertEquals(15, resultado.get(0).getCurtidas());
        assertEquals("Luna", resultado.get(1).getNome());
        assertEquals("Max", resultado.get(2).getNome());
        verify(petDashboardGateway).listarPetsPorOngIdOrderByCurtidas(ongId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não tem pets")
    void deveLancarExcecaoQuandoOngNaoTemPets() {
        // Arrange
        when(petDashboardGateway.listarPetsPorOngIdOrderByCurtidas(ongId)).thenReturn(Collections.emptyList());

        // Act & Assert
        DashboardException.NenhumPetEncontradoException exception = assertThrows(
                DashboardException.NenhumPetEncontradoException.class,
                () -> obterRankingPetsUseCase.obterRanking(ongId)
        );

        assertTrue(exception.getMessage().contains(ongId.toString()));
    }

    @Test
    @DisplayName("Deve retornar ranking com um único pet")
    void deveRetornarRankingComUnicoPet() {
        // Arrange
        PetDashboard pet = new PetDashboard(
                UUID.randomUUID(),
                "Rex",
                "Descrição",
                2.5,
                "Médio",
                "MACHO",
                true,
                true,
                true,
                false,
                5
        );

        when(petDashboardGateway.listarPetsPorOngIdOrderByCurtidas(ongId))
                .thenReturn(Arrays.asList(pet));

        // Act
        List<PetDashboard> resultado = obterRankingPetsUseCase.obterRanking(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Rex", resultado.get(0).getNome());
    }
}
