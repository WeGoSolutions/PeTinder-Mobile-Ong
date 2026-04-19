package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.AtualizarUsuarioCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    private Usuario usuarioExistente;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        atualizarUsuarioUseCase = new AtualizarUsuarioUseCase(usuarioGateway);
        usuarioId = UUID.randomUUID();
        
        usuarioExistente = new Usuario(
                usuarioId,
                "João Silva",
                "joao@email.com",
                "senhaCriptografada",
                LocalDate.now().minusYears(25),
                "12345678901",
                true
        );
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        // Arrange
        AtualizarUsuarioCommand command = new AtualizarUsuarioCommand(
                usuarioId,
                "João Silva Atualizado",
                "joao@email.com",
                LocalDate.now().minusYears(25),
                "12345678901",
                "01310-100",
                "Rua Teste",
                123,
                "Apto 1",
                "São Paulo",
                "SP"
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.atualizar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = atualizarUsuarioUseCase.atualizar(command);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva Atualizado", resultado.getNome());
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        AtualizarUsuarioCommand command = new AtualizarUsuarioCommand(
                idInexistente,
                "João Silva",
                "joao@email.com",
                LocalDate.now().minusYears(25),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(usuarioGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioException.UsuarioNaoEncontradoException exception = assertThrows(
                UsuarioException.UsuarioNaoEncontradoException.class,
                () -> atualizarUsuarioUseCase.atualizar(command)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(usuarioGateway, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe para outro usuário")
    void deveLancarExcecaoQuandoEmailJaExisteParaOutroUsuario() {
        // Arrange
        AtualizarUsuarioCommand command = new AtualizarUsuarioCommand(
                usuarioId,
                "João Silva",
                "outro@email.com",
                LocalDate.now().minusYears(25),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.emailJaExiste("outro@email.com")).thenReturn(true);

        // Act & Assert
        UsuarioException.EmailJaExisteException exception = assertThrows(
                UsuarioException.EmailJaExisteException.class,
                () -> atualizarUsuarioUseCase.atualizar(command)
        );

        assertTrue(exception.getMessage().contains("outro@email.com"));
        verify(usuarioGateway, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF já existe para outro usuário")
    void deveLancarExcecaoQuandoCpfJaExisteParaOutroUsuario() {
        // Arrange
        AtualizarUsuarioCommand command = new AtualizarUsuarioCommand(
                usuarioId,
                "João Silva",
                "joao@email.com",
                LocalDate.now().minusYears(25),
                "98765432109",
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.cpfJaExiste("98765432109")).thenReturn(true);

        // Act & Assert
        UsuarioException.CpfJaExisteException exception = assertThrows(
                UsuarioException.CpfJaExisteException.class,
                () -> atualizarUsuarioUseCase.atualizar(command)
        );

        assertTrue(exception.getMessage().contains("98765432109"));
        verify(usuarioGateway, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve permitir atualizar para o mesmo email")
    void devePermitirAtualizarParaMesmoEmail() {
        // Arrange
        AtualizarUsuarioCommand command = new AtualizarUsuarioCommand(
                usuarioId,
                "João Silva Atualizado",
                "joao@email.com", // Mesmo email
                LocalDate.now().minusYears(25),
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.atualizar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = atualizarUsuarioUseCase.atualizar(command);

        // Assert
        assertNotNull(resultado);
        verify(usuarioGateway, never()).emailJaExiste(anyString());
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }
}
