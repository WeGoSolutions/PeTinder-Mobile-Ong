package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
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
class ListarPetStatusLikedUseCaseTest {

    @Mock
    private PetStatusGateway petStatusGateway;

    private ListarPetStatusLikedUseCase listarPetStatusLikedUseCase;

    @BeforeEach
    void setUp() {
        listarPetStatusLikedUseCase = new ListarPetStatusLikedUseCase(petStatusGateway);
    }

    @Test
    @DisplayName("Deve listar todos os status LIKED")
    void deveListarTodosStatusLiked() {
        // Arrange
        UUID petId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        PetStatus status = new PetStatus(petId, userId, PetStatusEnum.LIKED);

        when(petStatusGateway.buscarTodosLiked()).thenReturn(Arrays.asList(status));

        // Act
        List<PetStatus> resultado = listarPetStatusLikedUseCase.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(PetStatusEnum.LIKED, resultado.get(0).getStatus());
        verify(petStatusGateway).buscarTodosLiked();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há status LIKED")
    void deveRetornarListaVaziaQuandoNaoHaStatusLiked() {
        // Arrange
        when(petStatusGateway.buscarTodosLiked()).thenReturn(Collections.emptyList());

        // Act
        List<PetStatus> resultado = listarPetStatusLikedUseCase.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve listar status LIKED por usuário")
    void deveListarStatusLikedPorUsuario() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID petId = UUID.randomUUID();
        PetStatus status = new PetStatus(petId, userId, PetStatusEnum.LIKED);

        when(petStatusGateway.buscarLikedPorUsuario(userId)).thenReturn(Arrays.asList(status));

        // Act
        List<PetStatus> resultado = listarPetStatusLikedUseCase.listarPorUsuario(userId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(userId, resultado.get(0).getUserId());
        verify(petStatusGateway).buscarLikedPorUsuario(userId);
    }
}
