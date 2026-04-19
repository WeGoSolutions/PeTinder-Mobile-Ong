package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoverPetStatusUseCaseTest {

    @Mock
    private PetStatusGateway petStatusGateway;

    private RemoverPetStatusUseCase removerPetStatusUseCase;

    private UUID petId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        removerPetStatusUseCase = new RemoverPetStatusUseCase(petStatusGateway);
        petId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve remover status com sucesso")
    void deveRemoverStatusComSucesso() {
        // Arrange
        PetStatus statusExistente = new PetStatus(petId, userId, PetStatusEnum.LIKED);
        statusExistente.setId(UUID.randomUUID());

        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.of(statusExistente));
        doNothing().when(petStatusGateway).removerPorPetEUsuario(petId, userId);

        // Act
        removerPetStatusUseCase.executar(petId, userId);

        // Assert
        verify(petStatusGateway).buscarPorPetEUsuario(petId, userId);
        verify(petStatusGateway).removerPorPetEUsuario(petId, userId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando status não encontrado")
    void deveLancarExcecaoQuandoStatusNaoEncontrado() {
        // Arrange
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.empty());

        // Act & Assert
        PetException exception = assertThrows(
                PetException.class,
                () -> removerPetStatusUseCase.executar(petId, userId)
        );

        assertTrue(exception.getMessage().contains(petId.toString()));
        assertTrue(exception.getMessage().contains(userId.toString()));
        verify(petStatusGateway, never()).removerPorPetEUsuario(any(UUID.class), any(UUID.class));
    }
}
