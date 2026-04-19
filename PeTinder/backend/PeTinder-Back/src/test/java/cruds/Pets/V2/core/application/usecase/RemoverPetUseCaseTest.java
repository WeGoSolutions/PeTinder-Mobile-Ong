package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
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
class RemoverPetUseCaseTest {

    @Mock
    private PetGateway petGateway;

    private RemoverPetUseCase removerPetUseCase;

    private UUID petId;

    @BeforeEach
    void setUp() {
        removerPetUseCase = new RemoverPetUseCase(petGateway);
        petId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve remover pet com sucesso")
    void deveRemoverPetComSucesso() {
        // Arrange
        when(petGateway.existePorId(petId)).thenReturn(true);
        doNothing().when(petGateway).remover(petId);

        // Act
        removerPetUseCase.remover(petId);

        // Assert
        verify(petGateway).existePorId(petId);
        verify(petGateway).remover(petId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet não encontrado")
    void deveLancarExcecaoQuandoPetNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(petGateway.existePorId(idInexistente)).thenReturn(false);

        // Act & Assert
        PetException.PetNaoEncontradoException exception = assertThrows(
                PetException.PetNaoEncontradoException.class,
                () -> removerPetUseCase.remover(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(petGateway, never()).remover(any(UUID.class));
    }
}
