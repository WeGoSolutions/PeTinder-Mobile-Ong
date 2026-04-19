package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.adapter.PetOngGateway;
import cruds.Ong.V2.core.application.exception.OngException;
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
class RemoverOngUseCaseTest {

    @Mock
    private OngGateway ongGateway;

    @Mock
    private PetOngGateway petOngGateway;

    private RemoverOngUseCase removerOngUseCase;

    private UUID ongId;

    @BeforeEach
    void setUp() {
        removerOngUseCase = new RemoverOngUseCase(ongGateway, petOngGateway);
        ongId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve remover ONG com sucesso")
    void deveRemoverOngComSucesso() {
        // Arrange
        when(ongGateway.existePorId(ongId)).thenReturn(true);
        doNothing().when(petOngGateway).removerPetsPorOng(ongId);
        doNothing().when(ongGateway).remover(ongId);

        // Act
        removerOngUseCase.remover(ongId);

        // Assert
        verify(ongGateway).existePorId(ongId);
        verify(petOngGateway).removerPetsPorOng(ongId);
        verify(ongGateway).remover(ongId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não encontrada")
    void deveLancarExcecaoQuandoOngNaoEncontrada() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(ongGateway.existePorId(idInexistente)).thenReturn(false);

        // Act & Assert
        OngException.OngNaoEncontradaException exception = assertThrows(
                OngException.OngNaoEncontradaException.class,
                () -> removerOngUseCase.remover(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(petOngGateway, never()).removerPetsPorOng(any(UUID.class));
        verify(ongGateway, never()).remover(any(UUID.class));
    }
}
