package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.ArmazenamentoImagemOngGateway;
import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.core.application.command.UploadImagemOngCommand;
import cruds.Ong.V2.core.application.exception.OngException;
import cruds.Ong.V2.core.domain.ImagemOng;
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
class UploadImagemOngUseCaseTest {

    @Mock
    private OngGateway ongGateway;

    @Mock
    private ArmazenamentoImagemOngGateway armazenamentoGateway;

    private UploadImagemOngUseCase uploadImagemOngUseCase;

    private UUID ongId;
    private Ong ongExistente;

    @BeforeEach
    void setUp() {
        uploadImagemOngUseCase = new UploadImagemOngUseCase(ongGateway, armazenamentoGateway);
        ongId = UUID.randomUUID();
        
        ongExistente = new Ong(
                ongId,
                "12345678000190",
                null,
                "ONG Teste",
                "ONG Teste LTDA",
                "senhaCriptografada",
                "ong@email.com",
                null
        );
    }

    @Test
    @DisplayName("Deve fazer upload de imagem com sucesso")
    void deveFazerUploadDeImagemComSucesso() {
        // Arrange
        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        UploadImagemOngCommand command = new UploadImagemOngCommand(ongId, dadosImagem, "imagem.jpg");

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));
        when(armazenamentoGateway.salvarImagem(any(ImagemOng.class))).thenReturn("url-imagem");
        when(ongGateway.atualizar(any(Ong.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ong resultado = uploadImagemOngUseCase.executar(command);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getImagemOng());
        verify(ongGateway).buscarPorId(ongId);
        verify(armazenamentoGateway).salvarImagem(any(ImagemOng.class));
        verify(ongGateway).atualizar(any(Ong.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando ONG não encontrada")
    void deveLancarExcecaoQuandoOngNaoEncontrada() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        UploadImagemOngCommand command = new UploadImagemOngCommand(idInexistente, dadosImagem, "imagem.jpg");

        when(ongGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        OngException.OngNaoEncontradaException exception = assertThrows(
                OngException.OngNaoEncontradaException.class,
                () -> uploadImagemOngUseCase.executar(command)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(armazenamentoGateway, never()).salvarImagem(any(ImagemOng.class));
    }

    @Test
    @DisplayName("Deve remover imagem anterior ao fazer upload de nova imagem")
    void deveRemoverImagemAnteriorAoFazerUpload() {
        // Arrange
        UUID imagemAnteriorId = UUID.randomUUID();
        ImagemOng imagemAnterior = new ImagemOng(imagemAnteriorId, new byte[]{6, 7, 8}, "anterior.jpg");
        ongExistente.definirImagem(imagemAnterior);

        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        UploadImagemOngCommand command = new UploadImagemOngCommand(ongId, dadosImagem, "nova.jpg");

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));
        doNothing().when(armazenamentoGateway).removerImagem(anyString(), any(UUID.class));
        when(armazenamentoGateway.salvarImagem(any(ImagemOng.class))).thenReturn("url-nova-imagem");
        when(ongGateway.atualizar(any(Ong.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ong resultado = uploadImagemOngUseCase.executar(command);

        // Assert
        assertNotNull(resultado);
        verify(armazenamentoGateway).removerImagem("anterior.jpg", imagemAnteriorId);
        verify(armazenamentoGateway).salvarImagem(any(ImagemOng.class));
    }

    @Test
    @DisplayName("Deve continuar upload mesmo quando remoção de imagem anterior falha")
    void deveContinuarUploadMesmoQuandoRemocaoFalha() {
        // Arrange
        UUID imagemAnteriorId = UUID.randomUUID();
        ImagemOng imagemAnterior = new ImagemOng(imagemAnteriorId, new byte[]{6, 7, 8}, "anterior.jpg");
        ongExistente.definirImagem(imagemAnterior);

        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        UploadImagemOngCommand command = new UploadImagemOngCommand(ongId, dadosImagem, "nova.jpg");

        when(ongGateway.buscarPorId(ongId)).thenReturn(Optional.of(ongExistente));
        doThrow(new RuntimeException("Erro ao remover")).when(armazenamentoGateway).removerImagem(anyString(), any(UUID.class));
        when(armazenamentoGateway.salvarImagem(any(ImagemOng.class))).thenReturn("url-nova-imagem");
        when(ongGateway.atualizar(any(Ong.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Ong resultado = uploadImagemOngUseCase.executar(command);

        // Assert
        assertNotNull(resultado);
        verify(armazenamentoGateway).salvarImagem(any(ImagemOng.class));
        verify(ongGateway).atualizar(any(Ong.class));
    }
}
