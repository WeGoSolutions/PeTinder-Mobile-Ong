package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Users.V2.core.adapter.UsuarioGateway;
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
class ListarPetsDisponivelParaUsuarioUseCaseTest {

    @Mock
    private PetGateway petGateway;

    @Mock
    private UsuarioGateway userGateway;

    private ListarPetsDisponivelParaUsuarioUseCase listarPetsDisponivelParaUsuarioUseCase;

    private UUID userId;

    @BeforeEach
    void setUp() {
        listarPetsDisponivelParaUsuarioUseCase = new ListarPetsDisponivelParaUsuarioUseCase(petGateway, userGateway);
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve listar pets disponíveis para usuário")
    void deveListarPetsDisponiveisParaUsuario() {
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

        when(userGateway.existePorId(userId)).thenReturn(true);
        when(petGateway.listarDisponiveis()).thenReturn(Arrays.asList(petDisponivel));

        // Act
        List<Pet> resultado = listarPetsDisponivelParaUsuarioUseCase.listarDisponiveis(userId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).getIsAdotado());
        verify(userGateway).existePorId(userId);
        verify(petGateway).listarDisponiveis();
    }

    @Test
    @DisplayName("Deve filtrar apenas pets disponíveis")
    void deveFiltrarApenasPetsDisponiveis() {
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

        Pet petAdotado = new Pet(
                UUID.randomUUID(),
                "Luna",
                1.5,
                "Pequeno",
                Arrays.asList("Dócil"),
                "Descrição",
                false,
                true,
                true,
                "FEMEA",
                UUID.randomUUID()
        );
        petAdotado.adotar();

        when(userGateway.existePorId(userId)).thenReturn(true);
        when(petGateway.listarDisponiveis()).thenReturn(Arrays.asList(petDisponivel, petAdotado));

        // Act
        List<Pet> resultado = listarPetsDisponivelParaUsuarioUseCase.listarDisponiveis(userId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Rex", resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há pets disponíveis")
    void deveRetornarListaVaziaQuandoNaoHaPetsDisponiveis() {
        // Arrange
        when(userGateway.existePorId(userId)).thenReturn(true);
        when(petGateway.listarDisponiveis()).thenReturn(Collections.emptyList());

        // Act
        List<Pet> resultado = listarPetsDisponivelParaUsuarioUseCase.listarDisponiveis(userId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        UUID userIdInexistente = UUID.randomUUID();
        when(userGateway.existePorId(userIdInexistente)).thenReturn(false);

        // Act & Assert
        PetException exception = assertThrows(
                PetException.class,
                () -> listarPetsDisponivelParaUsuarioUseCase.listarDisponiveis(userIdInexistente)
        );

        assertTrue(exception.getMessage().contains(userIdInexistente.toString()));
        verify(petGateway, never()).listarDisponiveis();
    }
}
