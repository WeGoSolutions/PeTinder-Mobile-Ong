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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarPetPorIdUseCaseTest {

    @Mock
    private PetGateway petGateway;

    private BuscarPetPorIdUseCase buscarPetPorIdUseCase;

    private UUID petId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        buscarPetPorIdUseCase = new BuscarPetPorIdUseCase(petGateway);
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
    @DisplayName("Deve buscar pet por ID com sucesso")
    void deveBuscarPetPorIdComSucesso() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act
        Pet resultado = buscarPetPorIdUseCase.buscar(petId);

        // Assert
        assertNotNull(resultado);
        assertEquals(petId, resultado.getId());
        assertEquals("Rex", resultado.getNome());
        verify(petGateway).buscarPorId(petId);
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
                () -> buscarPetPorIdUseCase.buscar(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(petGateway).buscarPorId(idInexistente);
    }
}
