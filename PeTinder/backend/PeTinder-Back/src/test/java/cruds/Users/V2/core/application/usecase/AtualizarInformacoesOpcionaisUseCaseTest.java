package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.AtualizarInformacoesOpcionaisCommand;
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
class AtualizarInformacoesOpcionaisUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    private AtualizarInformacoesOpcionaisUseCase atualizarInformacoesOpcionaisUseCase;

    private Usuario usuarioExistente;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        atualizarInformacoesOpcionaisUseCase = new AtualizarInformacoesOpcionaisUseCase(usuarioGateway);
        usuarioId = UUID.randomUUID();
        
        usuarioExistente = new Usuario(
                usuarioId,
                "João Silva",
                "joao@email.com",
                "senhaCriptografada",
                LocalDate.now().minusYears(25),
                null,
                true
        );
    }

    @Test
    @DisplayName("Deve adicionar informações opcionais com sucesso")
    void deveAdicionarInfosComSucesso() {
        // Arrange
        AtualizarInformacoesOpcionaisCommand command = new AtualizarInformacoesOpcionaisCommand(
                usuarioId,
                "12345678901",
                "01310-100",
                "Rua Teste",
                123,
                "São Paulo",
                "SP",
                "Apto 1"
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.cpfJaExiste("12345678901")).thenReturn(false);
        when(usuarioGateway.atualizar(any(Usuario.class))).thenReturn(usuarioExistente);

        // Act
        Usuario resultado = atualizarInformacoesOpcionaisUseCase.adicionarInfos(command);

        // Assert
        assertNotNull(resultado);
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(usuarioGateway).cpfJaExiste("12345678901");
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        AtualizarInformacoesOpcionaisCommand command = new AtualizarInformacoesOpcionaisCommand(
                idInexistente,
                "12345678901",
                "01310-100",
                "Rua Teste",
                123,
                "São Paulo",
                "SP",
                "Apto 1"
        );

        when(usuarioGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioException.UsuarioNaoEncontradoException exception = assertThrows(
                UsuarioException.UsuarioNaoEncontradoException.class,
                () -> atualizarInformacoesOpcionaisUseCase.adicionarInfos(command)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(usuarioGateway, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF já existe para outro usuário")
    void deveLancarExcecaoQuandoCpfJaExiste() {
        // Arrange
        AtualizarInformacoesOpcionaisCommand command = new AtualizarInformacoesOpcionaisCommand(
                usuarioId,
                "98765432109",
                "01310-100",
                "Rua Teste",
                123,
                "São Paulo",
                "SP",
                "Apto 1"
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.cpfJaExiste("98765432109")).thenReturn(true);

        // Act & Assert
        UsuarioException.CpfJaExisteException exception = assertThrows(
                UsuarioException.CpfJaExisteException.class,
                () -> atualizarInformacoesOpcionaisUseCase.adicionarInfos(command)
        );

        assertTrue(exception.getMessage().contains("98765432109"));
        verify(usuarioGateway, never()).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve permitir manter o mesmo CPF")
    void devePermitirManterMesmoCpf() {
        // Arrange
        Usuario usuarioComCpf = new Usuario(
                usuarioId,
                "João Silva",
                "joao@email.com",
                "senhaCriptografada",
                LocalDate.now().minusYears(25),
                "12345678901",
                true
        );

        AtualizarInformacoesOpcionaisCommand command = new AtualizarInformacoesOpcionaisCommand(
                usuarioId,
                "12345678901", // Mesmo CPF
                "01310-100",
                "Rua Teste",
                123,
                "São Paulo",
                "SP",
                "Apto 1"
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioComCpf));
        when(usuarioGateway.atualizar(any(Usuario.class))).thenReturn(usuarioComCpf);

        // Act
        Usuario resultado = atualizarInformacoesOpcionaisUseCase.adicionarInfos(command);

        // Assert
        assertNotNull(resultado);
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve adicionar informações quando CPF é nulo ou vazio")
    void deveAdicionarInfosQuandoCpfNuloOuVazio() {
        // Arrange
        AtualizarInformacoesOpcionaisCommand command = new AtualizarInformacoesOpcionaisCommand(
                usuarioId,
                null,
                "01310-100",
                "Rua Teste",
                123,
                "São Paulo",
                "SP",
                "Apto 1"
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.atualizar(any(Usuario.class))).thenReturn(usuarioExistente);

        // Act
        Usuario resultado = atualizarInformacoesOpcionaisUseCase.adicionarInfos(command);

        // Assert
        assertNotNull(resultado);
        verify(usuarioGateway, never()).cpfJaExiste(anyString());
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }
}
