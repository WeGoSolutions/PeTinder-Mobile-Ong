package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.CriptografiaOngGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.LoginOngCommand;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginOngUseCaseTest {

    @Mock
    private OngGateway ongGateway;

    @Mock
    private CriptografiaOngGateway criptografiaGateway;

    private LoginOngUseCase loginOngUseCase;

    private Ong ongExistente;

    @BeforeEach
    void setUp() {
        loginOngUseCase = new LoginOngUseCase(ongGateway, criptografiaGateway);
        
        ongExistente = new Ong(
                UUID.randomUUID(),
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
    @DisplayName("Deve fazer login com sucesso")
    void deveFazerLoginComSucesso() {
        // Arrange
        LoginOngCommand command = new LoginOngCommand("ong@email.com", "senha123");

        when(ongGateway.buscarPorEmail("ong@email.com")).thenReturn(Optional.of(ongExistente));
        when(criptografiaGateway.verificarSenha("senha123", "senhaCriptografada")).thenReturn(true);

        // Act
        Ong resultado = loginOngUseCase.logar(command);

        // Assert
        assertNotNull(resultado);
        assertEquals("ong@email.com", resultado.getEmail());
        assertEquals("ONG Teste", resultado.getNome());
        verify(ongGateway).buscarPorEmail("ong@email.com");
        verify(criptografiaGateway).verificarSenha("senha123", "senhaCriptografada");
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não encontrada")
    void deveLancarExcecaoQuandoOngNaoEncontrada() {
        // Arrange
        LoginOngCommand command = new LoginOngCommand("naoexiste@email.com", "senha123");

        when(ongGateway.buscarPorEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        OngException.OngNaoEncontradaException exception = assertThrows(
                OngException.OngNaoEncontradaException.class,
                () -> loginOngUseCase.logar(command)
        );

        assertTrue(exception.getMessage().contains("naoexiste@email.com"));
        verify(criptografiaGateway, never()).verificarSenha(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha inválida")
    void deveLancarExcecaoQuandoSenhaInvalida() {
        // Arrange
        LoginOngCommand command = new LoginOngCommand("ong@email.com", "senhaErrada");

        when(ongGateway.buscarPorEmail("ong@email.com")).thenReturn(Optional.of(ongExistente));
        when(criptografiaGateway.verificarSenha("senhaErrada", "senhaCriptografada")).thenReturn(false);

        // Act & Assert
        OngException.SenhaInvalidaException exception = assertThrows(
                OngException.SenhaInvalidaException.class,
                () -> loginOngUseCase.logar(command)
        );

        assertEquals("Senha inválida", exception.getMessage());
    }
}
