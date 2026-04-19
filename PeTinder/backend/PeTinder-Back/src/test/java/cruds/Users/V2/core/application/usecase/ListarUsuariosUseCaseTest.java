package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarUsuariosUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    private ListarUsuariosUseCase listarUsuariosUseCase;

    @BeforeEach
    void setUp() {
        listarUsuariosUseCase = new ListarUsuariosUseCase(usuarioGateway);
    }

    @Test
    @DisplayName("Deve listar todos os usuários com sucesso")
    void deveListarTodosUsuariosComSucesso() {
        // Arrange
        Usuario usuario1 = new Usuario(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "senha123",
                LocalDate.now().minusYears(25),
                null,
                true
        );

        Usuario usuario2 = new Usuario(
                UUID.randomUUID(),
                "Maria Santos",
                "maria@email.com",
                "senha456",
                LocalDate.now().minusYears(30),
                null,
                false
        );

        when(usuarioGateway.listarTodos()).thenReturn(Arrays.asList(usuario1, usuario2));

        // Act
        List<Usuario> resultado = listarUsuariosUseCase.listar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
        assertEquals("Maria Santos", resultado.get(1).getNome());
        verify(usuarioGateway).listarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há usuários")
    void deveRetornarListaVaziaQuandoNaoHaUsuarios() {
        // Arrange
        when(usuarioGateway.listarTodos()).thenReturn(Collections.emptyList());

        // Act
        List<Usuario> resultado = listarUsuariosUseCase.listar();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioGateway).listarTodos();
    }
}
