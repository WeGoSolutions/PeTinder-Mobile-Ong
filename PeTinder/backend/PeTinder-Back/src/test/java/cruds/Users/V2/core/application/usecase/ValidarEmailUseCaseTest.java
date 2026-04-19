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
class ValidarEmailUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    private ValidarEmailUseCase validarEmailUseCase;

    @BeforeEach
    void setUp() {
        validarEmailUseCase = new ValidarEmailUseCase(usuarioGateway);
    }

    @Test
    @DisplayName("Deve validar email com sucesso")
    void deveValidarEmailComSucesso() {
        // Arrange
        Usuario usuarioExistente = new Usuario(
                UUID.randomUUID(),
                "João Silva",
                "joao@email.com",
                "senhaCriptografada",
                LocalDate.now().minusYears(25),
                null,
                true
        );

        when(usuarioGateway.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(usuarioExistente));

        // Act
        Usuario resultado = validarEmailUseCase.validar("joao@email.com");

        // Assert
        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioGateway).buscarPorEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email não encontrado")
    void deveLancarExcecaoQuandoEmailNaoEncontrado() {
        // Arrange
        when(usuarioGateway.buscarPorEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioException.UsuarioNaoEncontradoException exception = assertThrows(
                UsuarioException.UsuarioNaoEncontradoException.class,
                () -> validarEmailUseCase.validar("naoexiste@email.com")
        );

        assertTrue(exception.getMessage().contains("naoexiste@email.com"));
        verify(usuarioGateway).buscarPorEmail("naoexiste@email.com");
    }
}
