package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.OngGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.command.CriarPetCommand;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarPetUseCaseTest {

    @Mock
    private PetGateway petGateway;

    @Mock
    private OngGateway ongGateway;

    private CriarPetUseCase criarPetUseCase;

    private UUID ongId;

    @BeforeEach
    void setUp() {
        criarPetUseCase = new CriarPetUseCase(petGateway, ongGateway);
        ongId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve criar pet com sucesso")
    void deveCriarPetComSucesso() {
        // Arrange
        CriarPetCommand command = new CriarPetCommand(
                "Rex",
                2.5,
                "Médio",
                Arrays.asList("Brincalhão", "Dócil"),
                "Um cachorro muito amigável",
                true,
                true,
                true,
                "MACHO",
                ongId
        );

        when(ongGateway.existePorId(ongId)).thenReturn(true);
        when(petGateway.salvar(any(Pet.class))).thenAnswer(invocation -> {
            Pet pet = invocation.getArgument(0);
            pet.setId(UUID.randomUUID());
            return pet;
        });

        // Act
        Pet resultado = criarPetUseCase.cadastrar(command);

        // Assert
        assertNotNull(resultado);
        assertEquals("Rex", resultado.getNome());
        assertEquals(2.5, resultado.getIdade());
        assertEquals("Médio", resultado.getPorte());
        assertEquals("MACHO", resultado.getSexo());
        assertTrue(resultado.getIsCastrado());
        verify(ongGateway).existePorId(ongId);
        verify(petGateway).salvar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não encontrada")
    void deveLancarExcecaoQuandoOngNaoEncontrada() {
        // Arrange
        UUID ongInexistente = UUID.randomUUID();
        CriarPetCommand command = new CriarPetCommand(
                "Rex",
                2.5,
                "Médio",
                Arrays.asList("Brincalhão"),
                "Descrição",
                true,
                true,
                true,
                "MACHO",
                ongInexistente
        );

        when(ongGateway.existePorId(ongInexistente)).thenReturn(false);

        // Act & Assert
        PetException.OngNaoEncontradaException exception = assertThrows(
                PetException.OngNaoEncontradaException.class,
                () -> criarPetUseCase.cadastrar(command)
        );

        assertTrue(exception.getMessage().contains(ongInexistente.toString()));
        verify(petGateway, never()).salvar(any(Pet.class));
    }
}
