package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;
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
class BuscarOngPorIdUseCaseTest {

    @Mock
    private OngGateway ongGateway;

    private BuscarOngPorIdUseCase buscarOngPorIdUseCase;

    private UUID ongId;
    private Ong ongExistente;

    @BeforeEach
    void setUp() {
        buscarOngPorIdUseCase = new BuscarOngPorIdUseCase(ongGateway);
        ongId = UUID.randomUUID();
        
        ongExistente = new Ong(
                ongId,
                "12345678000190",
                null,
                "ONG Teste",
                "ONG Teste LTDA",
                "senhaCriptografada",
                "ong@email.com",
                null
        );
    }

    @Test
    @DisplayName("Deve buscar ONG por ID com sucesso")
    void deveBuscarOngPorIdComSucesso() {
        // Arrange
        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));

        // Act
        Ong resultado = buscarOngPorIdUseCase.buscar(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(ongId, resultado.getId());
        assertEquals("ONG Teste", resultado.getNome());
        verify(ongGateway).buscarPorId(ongId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não encontrada")
    void deveLancarExcecaoQuandoOngNaoEncontrada() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(ongGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        OngException.OngNaoEncontradaException exception = assertThrows(
                OngException.OngNaoEncontradaException.class,
                () -> buscarOngPorIdUseCase.buscar(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(ongGateway).buscarPorId(idInexistente);
    }
}
