package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.ImagemOng;
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
class BuscarImagemOngUseCaseTest {

    @Mock
    private OngGateway ongGateway;

    private BuscarImagemOngUseCase buscarImagemOngUseCase;

    private UUID ongId;
    private Ong ongExistente;

    @BeforeEach
    void setUp() {
        buscarImagemOngUseCase = new BuscarImagemOngUseCase(ongGateway);
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
    @DisplayName("Deve buscar imagem da ONG com sucesso")
    void deveBuscarImagemDaOngComSucesso() {
        // Arrange
        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        ImagemOng imagemOng = new ImagemOng(UUID.randomUUID(), dadosImagem, "imagem.jpg");
        ongExistente.definirImagem(imagemOng);

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));

        // Act
        byte[] resultado = buscarImagemOngUseCase.buscarPorIndice(ongId, 0);

        // Assert
        assertNotNull(resultado);
        assertArrayEquals(dadosImagem, resultado);
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
                () -> buscarImagemOngUseCase.buscarPorIndice(idInexistente, 0)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando imagem não encontrada")
    void deveLancarExcecaoQuandoImagemNaoEncontrada() {
        // Arrange
        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));

        // Act & Assert
        OngException.ImagemInvalidaException exception = assertThrows(
                OngException.ImagemInvalidaException.class,
                () -> buscarImagemOngUseCase.buscarPorIndice(ongId, 0)
        );

        assertTrue(exception.getMessage().contains(ongId.toString()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando índice diferente de zero")
    void deveLancarExcecaoQuandoIndiceDiferenteDeZero() {
        // Arrange
        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        ImagemOng imagemOng = new ImagemOng(UUID.randomUUID(), dadosImagem, "imagem.jpg");
        ongExistente.definirImagem(imagemOng);

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));

        // Act & Assert
        OngException.ImagemInvalidaException exception = assertThrows(
                OngException.ImagemInvalidaException.class,
                () -> buscarImagemOngUseCase.buscarPorIndice(ongId, 1)
        );

        assertTrue(exception.getMessage().contains(ongId.toString()));
    }
}
