package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.AtualizarOngCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarOngUseCaseTest {

    @Mock
    private OngGateway ongGateway;

    private AtualizarOngUseCase atualizarOngUseCase;

    private UUID ongId;
    private Ong ongExistente;

    @BeforeEach
    void setUp() {
        atualizarOngUseCase = new AtualizarOngUseCase(ongGateway);
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
    @DisplayName("Deve atualizar ONG com sucesso")
    void deveAtualizarOngComSucesso() {
        // Arrange
        AtualizarOngCommand command = new AtualizarOngCommand(
                ongId,
                "12345678000190",
                null,
                "ONG Atualizada",
                "ONG Atualizada LTDA",
                "ong@email.com",
                "http://novolink.com",
                null
        );

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));
        when(ongGateway.atualizar(any(Ong.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ong resultado = atualizarOngUseCase.atualizar(command);

        // Assert
        assertNotNull(resultado);
        assertEquals("ONG Atualizada", resultado.getNome());
        verify(ongGateway).buscarPorId(ongId);
        verify(ongGateway).atualizar(any(Ong.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não encontrada")
    void deveLancarExcecaoQuandoOngNaoEncontrada() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        AtualizarOngCommand command = new AtualizarOngCommand(
                idInexistente,
                null,
                null,
                "ONG Teste",
                "ONG Teste LTDA",
                "ong@email.com",
                null,
                null
        );

        when(ongGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        OngException.OngNaoEncontradaException exception = assertThrows(
                OngException.OngNaoEncontradaException.class,
                () -> atualizarOngUseCase.atualizar(command)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(ongGateway, never()).atualizar(any(Ong.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe para outra ONG")
    void deveLancarExcecaoQuandoEmailJaExisteParaOutraOng() {
        // Arrange
        AtualizarOngCommand command = new AtualizarOngCommand(
                ongId,
                null,
                null,
                "ONG Teste",
                "ONG Teste LTDA",
                "outro@email.com",
                null,
                null
        );

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));
        when(ongGateway.emailJaExiste("outro@email.com")).thenReturn(true);

        // Act & Assert
        OngException.EmailJaExisteException exception = assertThrows(
                OngException.EmailJaExisteException.class,
                () -> atualizarOngUseCase.atualizar(command)
        );

        assertTrue(exception.getMessage().contains("outro@email.com"));
        verify(ongGateway, never()).atualizar(any(Ong.class));
    }

    @Test
    @DisplayName("Deve permitir atualizar para o mesmo email")
    void devePermitirAtualizarParaMesmoEmail() {
        // Arrange
        AtualizarOngCommand command = new AtualizarOngCommand(
                ongId,
                null,
                null,
                "ONG Atualizada",
                "ONG Atualizada LTDA",
                "ong@email.com", // Mesmo email
                null,
                null
        );

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));
        when(ongGateway.atualizar(any(Ong.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ong resultado = atualizarOngUseCase.atualizar(command);

        // Assert
        assertNotNull(resultado);
        verify(ongGateway, never()).emailJaExiste(anyString());
        verify(ongGateway).atualizar(any(Ong.class));
    }

    @Test
    @DisplayName("Deve atualizar ONG com endereço")
    void deveAtualizarOngComEndereco() {
        // Arrange
        AtualizarOngCommand.EnderecoCommand enderecoCommand = new AtualizarOngCommand.EnderecoCommand(
                "01310-100",
                "Rua Nova",
                "456",
                "São Paulo",
                "SP",
                "Sala 1"
        );

        AtualizarOngCommand command = new AtualizarOngCommand(
                ongId,
                null,
                null,
                "ONG Atualizada",
                "ONG Atualizada LTDA",
                "ong@email.com",
                null,
                enderecoCommand
        );

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));
        when(ongGateway.atualizar(any(Ong.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ong resultado = atualizarOngUseCase.atualizar(command);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getEndereco());
        assertEquals("Rua Nova", resultado.getEndereco().getRua());
    }
}
