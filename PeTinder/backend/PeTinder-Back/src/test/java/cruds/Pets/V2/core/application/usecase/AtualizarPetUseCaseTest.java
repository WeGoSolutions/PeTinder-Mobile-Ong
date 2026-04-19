package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.command.AtualizarPetCommand;
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
class AtualizarPetUseCaseTest {

    @Mock
    private PetGateway petGateway;

    private AtualizarPetUseCase atualizarPetUseCase;

    private UUID petId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        atualizarPetUseCase = new AtualizarPetUseCase(petGateway);
        petId = UUID.randomUUID();

        petExistente = new Pet(
                petId,
                "Rex",
                2.5,
                "Médio",
                Arrays.asList("Brincalhão"),
                "Descrição original",
                true,
                true,
                true,
                "MACHO",
                UUID.randomUUID()
        );
        petExistente.setCurtidas(5);
    }

    @Test
    @DisplayName("Deve atualizar pet com sucesso")
    void deveAtualizarPetComSucesso() {
        // Arrange
        AtualizarPetCommand command = new AtualizarPetCommand(
                petId,
                "Rex Atualizado",
                3.0,
                "Grande",
                Arrays.asList("Brincalhão", "Carinhoso"),
                "Descrição atualizada",
                true,
                true,
                true,
                "MACHO"
        );

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petGateway.atualizar(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pet resultado = atualizarPetUseCase.atualizar(command);

        // Assert
        assertNotNull(resultado);
        assertEquals("Rex Atualizado", resultado.getNome());
        assertEquals(3.0, resultado.getIdade());
        assertEquals("Grande", resultado.getPorte());
        assertEquals(5, resultado.getCurtidas()); // Deve preservar curtidas
        verify(petGateway).buscarPorId(petId);
        verify(petGateway).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet não encontrado")
    void deveLancarExcecaoQuandoPetNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        AtualizarPetCommand command = new AtualizarPetCommand(
                idInexistente,
                "Rex",
                2.5,
                "Médio",
                Arrays.asList("Brincalhão"),
                "Descrição",
                true,
                true,
                true,
                "MACHO"
        );

        when(petGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        PetException.PetNaoEncontradoException exception = assertThrows(
                PetException.PetNaoEncontradoException.class,
                () -> atualizarPetUseCase.atualizar(command)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(petGateway, never()).atualizar(any(Pet.class));
    }
}
