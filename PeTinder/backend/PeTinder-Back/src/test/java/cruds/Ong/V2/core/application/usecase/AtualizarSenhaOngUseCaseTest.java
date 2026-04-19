package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.CriptografiaOngGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.AtualizarSenhaOngCommand;
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
class AtualizarSenhaOngUseCaseTest {

    @Mock
    private OngGateway ongGateway;

    @Mock
    private CriptografiaOngGateway criptografiaGateway;

    private AtualizarSenhaOngUseCase atualizarSenhaOngUseCase;

    private UUID ongId;
    private Ong ongExistente;

    @BeforeEach
    void setUp() {
        atualizarSenhaOngUseCase = new AtualizarSenhaOngUseCase(ongGateway, criptografiaGateway);
        ongId = UUID.randomUUID();
        
        ongExistente = new Ong(
                ongId,
                "12345678000190",
                null,
                "ONG Teste",
                "ONG Teste LTDA",
                "senhaAtualCriptografada",
                "ong@email.com",
                null
        );
    }

    @Test
    @DisplayName("Deve atualizar senha com sucesso")
    void deveAtualizarSenhaComSucesso() {
        // Arrange
        AtualizarSenhaOngCommand command = new AtualizarSenhaOngCommand(
                ongId,
                "senhaAtual",
                "novaSenha"
        );

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));
        when(criptografiaGateway.verificarSenha("senhaAtual", "senhaAtualCriptografada")).thenReturn(true);
        when(criptografiaGateway.criptografarSenha("novaSenha")).thenReturn("novaSenhaCriptografada");
        when(ongGateway.atualizar(any(Ong.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ong resultado = atualizarSenhaOngUseCase.atualizarSenha(command);

        // Assert
        assertNotNull(resultado);
        verify(ongGateway).buscarPorId(ongId);
        verify(criptografiaGateway).verificarSenha("senhaAtual", "senhaAtualCriptografada");
        verify(criptografiaGateway).criptografarSenha("novaSenha");
        verify(ongGateway).atualizar(any(Ong.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não encontrada")
    void deveLancarExcecaoQuandoOngNaoEncontrada() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        AtualizarSenhaOngCommand command = new AtualizarSenhaOngCommand(
                idInexistente,
                "senhaAtual",
                "novaSenha"
        );

        when(ongGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        OngException.OngNaoEncontradaException exception = assertThrows(
                OngException.OngNaoEncontradaException.class,
                () -> atualizarSenhaOngUseCase.atualizarSenha(command)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(ongGateway, never()).atualizar(any(Ong.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando senha atual incorreta")
    void deveLancarExcecaoQuandoSenhaAtualIncorreta() {
        // Arrange
        AtualizarSenhaOngCommand command = new AtualizarSenhaOngCommand(
                ongId,
                "senhaErrada",
                "novaSenha"
        );

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));
        when(criptografiaGateway.verificarSenha("senhaErrada", "senhaAtualCriptografada")).thenReturn(false);

        // Act & Assert
        OngException.SenhaInvalidaException exception = assertThrows(
                OngException.SenhaInvalidaException.class,
                () -> atualizarSenhaOngUseCase.atualizarSenha(command)
        );

        assertEquals("Senha atual não confere", exception.getMessage());
        verify(ongGateway, never()).atualizar(any(Ong.class));
    }
}
