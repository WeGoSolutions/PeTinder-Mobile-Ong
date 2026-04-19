package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.MensagemPendenteGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarMensagensPendentesUseCaseTest {

    @Mock
    private MensagemPendenteGateway mensagemPendenteGateway;

    private ListarMensagensPendentesUseCase listarMensagensPendentesUseCase;

    private UUID ongId;

    @BeforeEach
    void setUp() {
        listarMensagensPendentesUseCase = new ListarMensagensPendentesUseCase(mensagemPendenteGateway);
        ongId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve listar mensagens pendentes com sucesso")
    void deveListarMensagensPendentesComSucesso() {
        // Arrange
        MensagemPendenteGateway.MensagemPendente mensagem = new MensagemPendenteGateway.MensagemPendente(
                UUID.randomUUID(),
                "Rex",
                UUID.randomUUID(),
                "João",
                "joao@email.com",
                null,
                LocalDateTime.now()
        );

        when(mensagemPendenteGateway.listarMensagensPendentes(ongId)).thenReturn(Arrays.asList(mensagem));

        // Act
        List<MensagemPendenteGateway.MensagemPendente> resultado = listarMensagensPendentesUseCase.listar(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Rex", resultado.get(0).getPetNome());
        assertEquals("João", resultado.get(0).getUserName());
        verify(mensagemPendenteGateway).listarMensagensPendentes(ongId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há mensagens pendentes")
    void deveRetornarListaVaziaQuandoNaoHaMensagensPendentes() {
        // Arrange
        when(mensagemPendenteGateway.listarMensagensPendentes(ongId)).thenReturn(Collections.emptyList());

        // Act
        List<MensagemPendenteGateway.MensagemPendente> resultado = listarMensagensPendentesUseCase.listar(ongId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve listar múltiplas mensagens pendentes")
    void deveListarMultiplasMensagensPendentes() {
        // Arrange
        MensagemPendenteGateway.MensagemPendente mensagem1 = new MensagemPendenteGateway.MensagemPendente(
                UUID.randomUUID(),
                "Rex",
                UUID.randomUUID(),
                "João",
                "joao@email.com",
                null,
                LocalDateTime.now()
        );

        MensagemPendenteGateway.MensagemPendente mensagem2 = new MensagemPendenteGateway.MensagemPendente(
                UUID.randomUUID(),
                "Luna",
                UUID.randomUUID(),
                "Maria",
                "maria@email.com",
                null,
                LocalDateTime.now()
        );

        when(mensagemPendenteGateway.listarMensagensPendentes(ongId)).thenReturn(Arrays.asList(mensagem1, mensagem2));

        // Act
        List<MensagemPendenteGateway.MensagemPendente> resultado = listarMensagensPendentesUseCase.listar(ongId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }
}
