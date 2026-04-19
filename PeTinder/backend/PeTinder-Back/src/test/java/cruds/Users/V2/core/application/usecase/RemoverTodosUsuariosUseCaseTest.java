package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoverTodosUsuariosUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    private RemoverTodosUsuariosUseCase removerTodosUsuariosUseCase;

    @BeforeEach
    void setUp() {
        removerTodosUsuariosUseCase = new RemoverTodosUsuariosUseCase(usuarioGateway);
    }

    @Test
    @DisplayName("Deve remover todos os usuários com sucesso")
    void deveRemoverTodosUsuariosComSucesso() {
        // Arrange
        doNothing().when(usuarioGateway).removerTodos();

        // Act
        removerTodosUsuariosUseCase.removerTodos();

        // Assert
        verify(usuarioGateway).removerTodos();
    }
}
