package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.CriptografiaGateway;
import cruds.Users.V2.core.adapter.EmailGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.CriarUsuarioCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private CriptografiaGateway criptografiaGateway;

    @Mock
    private EmailGateway emailGateway;

    private CriarUsuarioUseCase criarUsuarioUseCase;

    @BeforeEach
    void setUp() {
        criarUsuarioUseCase = new CriarUsuarioUseCase(usuarioGateway, criptografiaGateway, emailGateway);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // Arrange
        CriarUsuarioCommand command = new CriarUsuarioCommand(
                "João Silva",
                "joao@email.com",
                "senha123",
                LocalDate.now().minusYears(25)
        );

        when(usuarioGateway.emailJaExiste(anyString())).thenReturn(false);
        when(criptografiaGateway.criptografarSenha(anyString())).thenReturn("senhaCriptografada");
        when(usuarioGateway.salvar(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(UUID.randomUUID());
            return usuario;
        });
        doNothing().when(emailGateway).enviarEmailBoasVindas(anyString(), anyString());

        // Act
        Usuario resultado = criarUsuarioUseCase.cadastrar(command);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioGateway).emailJaExiste("joao@email.com");
        verify(criptografiaGateway).criptografarSenha("senha123");
        verify(usuarioGateway).salvar(any(Usuario.class));
        verify(emailGateway).enviarEmailBoasVindas("joao@email.com", "João Silva");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Arrange
        CriarUsuarioCommand command = new CriarUsuarioCommand(
                "João Silva",
                "joao@email.com",
                "senha123",
                LocalDate.now().minusYears(25)
        );

        when(usuarioGateway.emailJaExiste("joao@email.com")).thenReturn(true);

        // Act & Assert
        UsuarioException.EmailJaExisteException exception = assertThrows(
                UsuarioException.EmailJaExisteException.class,
                () -> criarUsuarioUseCase.cadastrar(command)
        );

        assertTrue(exception.getMessage().contains("joao@email.com"));
        verify(usuarioGateway).emailJaExiste("joao@email.com");
        verify(usuarioGateway, never()).salvar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve criar usuário mesmo quando envio de email falha")
    void deveCriarUsuarioMesmoQuandoEnvioEmailFalha() {
        // Arrange
        CriarUsuarioCommand command = new CriarUsuarioCommand(
                "João Silva",
                "joao@email.com",
                "senha123",
                LocalDate.now().minusYears(25)
        );

        when(usuarioGateway.emailJaExiste(anyString())).thenReturn(false);
        when(criptografiaGateway.criptografarSenha(anyString())).thenReturn("senhaCriptografada");
        when(usuarioGateway.salvar(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(UUID.randomUUID());
            return usuario;
        });
        doThrow(new RuntimeException("Erro ao enviar email")).when(emailGateway).enviarEmailBoasVindas(anyString(), anyString());

        // Act
        Usuario resultado = criarUsuarioUseCase.cadastrar(command);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(usuarioGateway).salvar(any(Usuario.class));
    }
}
