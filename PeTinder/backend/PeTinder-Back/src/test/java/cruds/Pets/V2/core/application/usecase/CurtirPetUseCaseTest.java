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
class CurtirPetUseCaseTest {

    @Mock
    private PetGateway petGateway;

    private CurtirPetUseCase curtirPetUseCase;

    private UUID petId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        curtirPetUseCase = new CurtirPetUseCase(petGateway);
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
    @DisplayName("Deve curtir pet com sucesso")
    void deveCurtirPetComSucesso() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petGateway.atualizar(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pet resultado = curtirPetUseCase.curtir(petId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getCurtidas());
        verify(petGateway).buscarPorId(petId);
        verify(petGateway).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve incrementar curtidas corretamente")
    void deveIncrementarCurtidasCorretamente() {
        // Arrange
        petExistente.setCurtidas(5);
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petGateway.atualizar(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pet resultado = curtirPetUseCase.curtir(petId);

        // Assert
        assertEquals(6, resultado.getCurtidas());
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet não encontrado ao curtir")
    void deveLancarExcecaoQuandoPetNaoEncontradoAoCurtir() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(petGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        PetException.PetNaoEncontradoException exception = assertThrows(
                PetException.PetNaoEncontradoException.class,
                () -> curtirPetUseCase.curtir(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(petGateway, never()).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve descurtir pet com sucesso")
    void deveDescurtirPetComSucesso() {
        // Arrange
        petExistente.setCurtidas(5);
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petGateway.atualizar(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pet resultado = curtirPetUseCase.descurtir(petId);

        // Assert
        assertNotNull(resultado);
        assertEquals(4, resultado.getCurtidas());
        verify(petGateway).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Não deve decrementar curtidas abaixo de zero")
    void naoDeveDecrementarCurtidasAbaixoDeZero() {
        // Arrange
        petExistente.setCurtidas(0);
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petGateway.atualizar(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pet resultado = curtirPetUseCase.descurtir(petId);

        // Assert
        assertEquals(0, resultado.getCurtidas());
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet não encontrado ao descurtir")
    void deveLancarExcecaoQuandoPetNaoEncontradoAoDescurtir() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(petGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        PetException.PetNaoEncontradoException exception = assertThrows(
                PetException.PetNaoEncontradoException.class,
                () -> curtirPetUseCase.descurtir(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
    }
}
