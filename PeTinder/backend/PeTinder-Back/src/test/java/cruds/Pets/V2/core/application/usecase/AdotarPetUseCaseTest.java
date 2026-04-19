package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdotarPetUseCaseTest {

    @Mock
    private PetGateway petGateway;

    private AdotarPetUseCase adotarPetUseCase;

    private UUID petId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        adotarPetUseCase = new AdotarPetUseCase(petGateway);
        petId = UUID.randomUUID();

        petExistente = new Pet(
                petId,
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
    }

    @Test
    @DisplayName("Deve adotar pet com sucesso")
    void deveAdotarPetComSucesso() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petGateway.atualizar(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pet resultado = adotarPetUseCase.adotar(petId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getIsAdotado());
        verify(petGateway).buscarPorId(petId);
        verify(petGateway).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet não encontrado")
    void deveLancarExcecaoQuandoPetNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(petGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        PetException.PetNaoEncontradoException exception = assertThrows(
                PetException.PetNaoEncontradoException.class,
                () -> adotarPetUseCase.adotar(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(petGateway, never()).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet já foi adotado")
    void deveLancarExcecaoQuandoPetJaAdotado() {
        // Arrange
        petExistente.adotar(); // Marca como adotado
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act & Assert
        PetException.PetJaAdotadoException exception = assertThrows(
                PetException.PetJaAdotadoException.class,
                () -> adotarPetUseCase.adotar(petId)
        );

        assertEquals("Pet já foi adotado", exception.getMessage());
        verify(petGateway, never()).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve cancelar adoção com sucesso")
    void deveCancelarAdocaoComSucesso() {
        // Arrange
        petExistente.adotar(); // Marca como adotado
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petGateway.atualizar(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pet resultado = adotarPetUseCase.cancelarAdocao(petId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.getIsAdotado());
        verify(petGateway).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao cancelar adoção de pet não encontrado")
    void deveLancarExcecaoAoCancelarAdocaoPetNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(petGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        PetException.PetNaoEncontradoException exception = assertThrows(
                PetException.PetNaoEncontradoException.class,
                () -> adotarPetUseCase.cancelarAdocao(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
    }
}
