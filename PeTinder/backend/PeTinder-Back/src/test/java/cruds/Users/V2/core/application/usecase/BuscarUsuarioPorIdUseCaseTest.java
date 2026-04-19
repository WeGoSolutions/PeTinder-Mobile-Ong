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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioPorIdUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    private BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    private Usuario usuarioExistente;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        buscarUsuarioPorIdUseCase = new BuscarUsuarioPorIdUseCase(usuarioGateway);
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
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        // Arrange
        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));

        // Act
        Usuario resultado = buscarUsuarioPorIdUseCase.buscar(usuarioId);

        // Assert
        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioGateway).buscarPorId(usuarioId);
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
                () -> buscarUsuarioPorIdUseCase.buscar(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(usuarioGateway).buscarPorId(idInexistente);
    }
}
