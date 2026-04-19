package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
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
class MarcarUsuarioExperienteUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    private MarcarUsuarioExperienteUseCase marcarUsuarioExperienteUseCase;

    private Usuario usuarioExistente;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        marcarUsuarioExperienteUseCase = new MarcarUsuarioExperienteUseCase(usuarioGateway);
        usuarioId = UUID.randomUUID();
        
        usuarioExistente = new Usuario(
                usuarioId,
                "João Silva",
                "joao@email.com",
                "senhaCriptografada",
                LocalDate.now().minusYears(25),
                null,
                true // Usuário novo
        );
    }

    @Test
    @DisplayName("Deve marcar usuário como experiente com sucesso")
    void deveMarcarUsuarioComoExperienteComSucesso() {
        // Arrange
        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioGateway.atualizar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = marcarUsuarioExperienteUseCase.executar(usuarioId);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.getUsuarioNovo());
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(usuarioGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioException.UsuarioNaoEncontradoException exception = assertThrows(
                UsuarioException.UsuarioNaoEncontradoException.class,
                () -> marcarUsuarioExperienteUseCase.executar(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(usuarioGateway, never()).atualizar(any(Usuario.class));
    }
}
