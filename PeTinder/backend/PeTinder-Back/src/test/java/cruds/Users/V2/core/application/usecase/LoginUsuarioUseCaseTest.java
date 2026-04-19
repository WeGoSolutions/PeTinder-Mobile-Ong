package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.AutenticacaoGateway;
import cruds.Users.V2.core.adapter.CriptografiaGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.LoginUsuarioCommand;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private CriptografiaGateway criptografiaGateway;

    @Mock
    private AutenticacaoGateway autenticacaoGateway;

    private LoginUsuarioUseCase loginUsuarioUseCase;

    private Usuario usuarioExistente;

    @BeforeEach
    void setUp() {
        loginUsuarioUseCase = new LoginUsuarioUseCase(usuarioGateway, criptografiaGateway, autenticacaoGateway);
        
        usuarioExistente = new Usuario(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "senhaCriptografada",
                LocalDate.now().minusYears(25),
                null,
                true
        );
    }

    @Test
    @DisplayName("Deve fazer login com sucesso")
    void deveFazerLoginComSucesso() {
        // Arrange
        LoginUsuarioCommand command = new LoginUsuarioCommand("joao@email.com", "senha123");

        when(usuarioGateway.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioExistente));
        when(criptografiaGateway.verificarSenha("senha123", "senhaCriptografada")).thenReturn(true);
        when(autenticacaoGateway.gerarToken("joao@email.com")).thenReturn("token-jwt-valido");

        // Act
        LoginUsuarioUseCase.LoginResult resultado = loginUsuarioUseCase.logar(command);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getUsuario());
        assertEquals("joao@email.com", resultado.getUsuario().getEmail());
        assertEquals("token-jwt-valido", resultado.getToken());
        verify(usuarioGateway).buscarPorEmail("joao@email.com");
        verify(criptografiaGateway).verificarSenha("senha123", "senhaCriptografada");
        verify(autenticacaoGateway).gerarToken("joao@email.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        LoginUsuarioCommand command = new LoginUsuarioCommand("naoexiste@email.com", "senha123");

        when(usuarioGateway.buscarPorEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioException.CredenciaisInvalidasException exception = assertThrows(
                UsuarioException.CredenciaisInvalidasException.class,
                () -> loginUsuarioUseCase.logar(command)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());
        verify(usuarioGateway).buscarPorEmail("naoexiste@email.com");
        verify(criptografiaGateway, never()).verificarSenha(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha incorreta")
    void deveLancarExcecaoQuandoSenhaIncorreta() {
        // Arrange
        LoginUsuarioCommand command = new LoginUsuarioCommand("joao@email.com", "senhaErrada");

        when(usuarioGateway.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioExistente));
        when(criptografiaGateway.verificarSenha("senhaErrada", "senhaCriptografada")).thenReturn(false);

        // Act & Assert
        UsuarioException.CredenciaisInvalidasException exception = assertThrows(
                UsuarioException.CredenciaisInvalidasException.class,
                () -> loginUsuarioUseCase.logar(command)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());
        verify(autenticacaoGateway, never()).gerarToken(anyString());
    }
}
