package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.CriptografiaOngGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.CriarOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.Ong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarOngUseCaseTest {

    @Mock
    private OngGateway ongGateway;

    @Mock
    private CriptografiaOngGateway criptografiaGateway;

    private CriarOngUseCase criarOngUseCase;

    @BeforeEach
    void setUp() {
        criarOngUseCase = new CriarOngUseCase(ongGateway, criptografiaGateway);
    }

    @Test
    @DisplayName("Deve criar ONG com sucesso")
    void deveCriarOngComSucesso() {
        // Arrange
        CriarOngCommand.EnderecoCommand enderecoCommand = new CriarOngCommand.EnderecoCommand(
                "01310-100",
                "Rua Teste",
                "123",
                "São Paulo",
                "SP",
                "Apto 1"
        );

        CriarOngCommand command = new CriarOngCommand(
                "12345678000190",
                null,
                "ONG Teste",
                "ONG Teste LTDA",
                "senha123",
                "ong@email.com",
                "http://ong.com",
                enderecoCommand
        );

        when(ongGateway.emailJaExiste(anyString())).thenReturn(false);
        when(criptografiaGateway.criptografarSenha(anyString())).thenReturn("senhaCriptografada");
        when(ongGateway.salvar(any(Ong.class))).thenAnswer(invocation -> {
            Ong ong = invocation.getArgument(0);
            ong.setId(UUID.randomUUID());
            return ong;
        });

        // Act
        Ong resultado = criarOngUseCase.cadastrar(command);

        // Assert
        assertNotNull(resultado);
        assertEquals("ONG Teste", resultado.getNome());
        assertEquals("ong@email.com", resultado.getEmail());
        verify(ongGateway).emailJaExiste("ong@email.com");
        verify(criptografiaGateway).criptografarSenha("senha123");
        verify(ongGateway).salvar(any(Ong.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Arrange
        CriarOngCommand command = new CriarOngCommand(
                "12345678000190",
                null,
                "ONG Teste",
                "ONG Teste LTDA",
                "senha123",
                "existente@email.com",
                null,
                null
        );

        when(ongGateway.emailJaExiste("existente@email.com")).thenReturn(true);

        // Act & Assert
        OngException.EmailJaExisteException exception = assertThrows(
                OngException.EmailJaExisteException.class,
                () -> criarOngUseCase.cadastrar(command)
        );

        assertTrue(exception.getMessage().contains("existente@email.com"));
        verify(ongGateway, never()).salvar(any(Ong.class));
    }

    @Test
    @DisplayName("Deve criar ONG sem endereço")
    void deveCriarOngSemEndereco() {
        // Arrange
        CriarOngCommand command = new CriarOngCommand(
                "12345678000190",
                null,
                "ONG Teste",
                "ONG Teste LTDA",
                "senha123",
                "ong@email.com",
                null,
                null
        );

        when(ongGateway.emailJaExiste(anyString())).thenReturn(false);
        when(criptografiaGateway.criptografarSenha(anyString())).thenReturn("senhaCriptografada");
        when(ongGateway.salvar(any(Ong.class))).thenAnswer(invocation -> {
            Ong ong = invocation.getArgument(0);
            ong.setId(UUID.randomUUID());
            return ong;
        });

        // Act
        Ong resultado = criarOngUseCase.cadastrar(command);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getEndereco());
    }
}
