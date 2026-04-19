package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.CriptografiaGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.AtualizarSenhaCommand;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarSenhaUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private CriptografiaGateway criptografiaGateway;

    private AtualizarSenhaUseCase atualizarSenhaUseCase;

    private Usuario usuarioExistente;

    @BeforeEach
    void setUp() {
        atualizarSenhaUseCase = new AtualizarSenhaUseCase(usuarioGateway, criptografiaGateway);
        
        usuarioExistente = new Usuario(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "senhaAtualCriptografada",
                LocalDate.now().minusYears(25),
                null,
                true
        );
    }

    @Test
    @DisplayName("Deve atualizar senha com sucesso quando senha atual é válida")
    void deveAtualizarSenhaComSucessoQuandoSenhaAtualValida() {
        // Arrange
        AtualizarSenhaCommand command = new AtualizarSenhaCommand(
                "joao@email.com",
                "senhaAtual",
                "novaSenha"
        );

        when(usuarioGateway.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioExistente));
        when(criptografiaGateway.verificarSenha("senhaAtual", "senhaAtualCriptografada")).thenReturn(true);
        when(criptografiaGateway.criptografarSenha("novaSenha")).thenReturn("novaSenhaCriptografada");
        when(usuarioGateway.atualizar(any(Usuario.class))).thenReturn(usuarioExistente);

        // Act
        Usuario resultado = atualizarSenhaUseCase.atualizarSenha(command);

        // Assert
        assertNotNull(resultado);
        verify(usuarioGateway).buscarPorEmail("joao@email.com");
        verify(criptografiaGateway).verificarSenha("senhaAtual", "senhaAtualCriptografada");
        verify(criptografiaGateway).criptografarSenha("novaSenha");
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        AtualizarSenhaCommand command = new AtualizarSenhaCommand(
                "naoexiste@email.com",
                "senhaAtual",
                "novaSenha"
        );

        when(usuarioGateway.buscarPorEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioException.UsuarioNaoEncontradoException exception = assertThrows(
                UsuarioException.UsuarioNaoEncontradoException.class,
                () -> atualizarSenhaUseCase.atualizarSenha(command)
        );

        assertTrue(exception.getMessage().contains("naoexiste@email.com"));
        verify(usuarioGateway, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha atual incorreta")
    void deveLancarExcecaoQuandoSenhaAtualIncorreta() {
        // Arrange
        AtualizarSenhaCommand command = new AtualizarSenhaCommand(
                "joao@email.com",
                "senhaErrada",
                "novaSenha"
        );

        when(usuarioGateway.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioExistente));
        when(criptografiaGateway.verificarSenha("senhaErrada", "senhaAtualCriptografada")).thenReturn(false);

        // Act & Assert
        UsuarioException.SenhaInvalidaException exception = assertThrows(
                UsuarioException.SenhaInvalidaException.class,
                () -> atualizarSenhaUseCase.atualizarSenha(command)
        );

        assertEquals("Senha atual incorreta", exception.getMessage());
        verify(usuarioGateway, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve atualizar senha quando senha atual é nula ou vazia")
    void deveAtualizarSenhaQuandoSenhaAtualNulaOuVazia() {
        // Arrange
        AtualizarSenhaCommand command = new AtualizarSenhaCommand(
                "joao@email.com",
                null,
                "novaSenha"
        );

        when(usuarioGateway.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioExistente));
        when(criptografiaGateway.criptografarSenha("novaSenha")).thenReturn("novaSenhaCriptografada");
        when(usuarioGateway.atualizar(any(Usuario.class))).thenReturn(usuarioExistente);

        // Act
        Usuario resultado = atualizarSenhaUseCase.atualizarSenha(command);

        // Assert
        assertNotNull(resultado);
        verify(criptografiaGateway, never()).verificarSenha(anyString(), anyString());
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }
}
