package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.domain.Pet;
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
class ListarPetsUseCaseTest {

    @Mock
    private PetGateway petGateway;

    private ListarPetsUseCase listarPetsUseCase;

    @BeforeEach
    void setUp() {
        listarPetsUseCase = new ListarPetsUseCase(petGateway);
    }

    @Test
    @DisplayName("Deve listar todos os pets com sucesso")
    void deveListarTodosPetsComSucesso() {
        // Arrange
        Pet pet1 = new Pet(
                UUID.randomUUID(),
                "Rex",
                2.5,
                "Médio",
                Arrays.asList("Brincalhão"),
                "Descrição 1",
                true,
                true,
                true,
                "MACHO",
                UUID.randomUUID()
        );

        Pet pet2 = new Pet(
                UUID.randomUUID(),
                "Luna",
                1.5,
                "Pequeno",
                Arrays.asList("Dócil"),
                "Descrição 2",
                false,
                true,
                true,
                "FEMEA",
                UUID.randomUUID()
        );

        when(petGateway.listarTodos()).thenReturn(Arrays.asList(pet1, pet2));

        // Act
        List<Pet> resultado = listarPetsUseCase.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Rex", resultado.get(0).getNome());
        assertEquals("Luna", resultado.get(1).getNome());
        verify(petGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há pets")
    void deveRetornarListaVaziaQuandoNaoHaPets() {
        // Arrange
        when(petGateway.listarTodos()).thenReturn(Collections.emptyList());

        // Act
        List<Pet> resultado = listarPetsUseCase.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(petGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve listar pets por ONG")
    void deveListarPetsPorOng() {
        // Arrange
        UUID ongId = UUID.randomUUID();
        Pet pet = new Pet(
                UUID.randomUUID(),
                "Rex",
                2.5,
                "Médio",
                Arrays.asList("Brincalhão"),
                "Descrição",
                true,
                true,
                true,
                "MACHO",
                ongId
        );

        when(petGateway.listarPorOng(ongId)).thenReturn(Arrays.asList(pet));

        // Act
        List<Pet> resultado = listarPetsUseCase.listarPorOng(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(ongId, resultado.get(0).getOngId());
        verify(petGateway).listarPorOng(ongId);
    }

    @Test
    @DisplayName("Deve listar pets disponíveis")
    void deveListarPetsDisponiveis() {
        // Arrange
        Pet petDisponivel = new Pet(
                UUID.randomUUID(),
                "Rex",
                2.5,
                "Médio",
                Arrays.asList("Brincalhão"),
                "Descrição",
                true,
                true,
                true,
                "MACHO",
                UUID.randomUUID()
        );

        when(petGateway.listarDisponiveis()).thenReturn(Arrays.asList(petDisponivel));

        // Act
        List<Pet> resultado = listarPetsUseCase.listarDisponiveis();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).getIsAdotado());
        verify(petGateway).listarDisponiveis();
    }
}
