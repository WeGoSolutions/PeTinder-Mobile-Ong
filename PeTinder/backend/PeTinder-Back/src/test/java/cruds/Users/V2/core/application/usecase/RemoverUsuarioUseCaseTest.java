package cruds.Users.V2.core.application.usecase;

import cruds.Pets.V2.core.application.usecase.RemoverPorUsuarioUseCase;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoverUsuarioUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private RemoverPorUsuarioUseCase removerPorUsuarioUseCase;

    private RemoverUsuarioUseCase removerUsuarioUseCase;

    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        removerUsuarioUseCase = new RemoverUsuarioUseCase(usuarioGateway, removerPorUsuarioUseCase);
        usuarioId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve remover usuário com sucesso")
    void deveRemoverUsuarioComSucesso() {
        // Arrange
        Usuario usuarioExistente = new Usuario(
                usuarioId,
                "João Silva",
                "joao@email.com",
                "senhaCriptografada",
                LocalDate.now().minusYears(25),
                null,
                true
        );

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        doNothing().when(removerPorUsuarioUseCase).removerPorId(usuarioId);
        doNothing().when(usuarioGateway).remover(usuarioId);

        // Act
        removerUsuarioUseCase.apagarUser(usuarioId);

        // Assert
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(removerPorUsuarioUseCase).removerPorId(usuarioId);
        verify(usuarioGateway).remover(usuarioId);
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
                () -> removerUsuarioUseCase.apagarUser(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));

        verify(usuarioGateway).buscarPorId(idInexistente);
        verify(removerPorUsuarioUseCase, never()).removerPorId(any());
        verify(usuarioGateway, never()).remover(any());
    }
}
