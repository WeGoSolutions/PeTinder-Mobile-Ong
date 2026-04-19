package cruds.Users.V2.core.application.usecase;

import cruds.Users.V2.core.adapter.ArmazenamentoImagemGateway;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.application.command.UploadImagemCommand;
import cruds.Users.V2.core.application.exception.UsuarioException;
import cruds.Users.V2.core.domain.ImagemUsuario;
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
class UploadImagemPerfilUseCaseTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @Mock
    private ArmazenamentoImagemGateway armazenamentoImagemGateway;

    private UploadImagemPerfilUseCase uploadImagemPerfilUseCase;

    private Usuario usuarioExistente;
    private UUID usuarioId;

    @BeforeEach
    void setUp() {
        uploadImagemPerfilUseCase = new UploadImagemPerfilUseCase(usuarioGateway, armazenamentoImagemGateway);
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
    @DisplayName("Deve fazer upload de imagem de perfil com sucesso")
    void deveFazerUploadImagemPerfilComSucesso() {
        // Arrange
        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        UploadImagemCommand command = new UploadImagemCommand(usuarioId, dadosImagem, "perfil.jpg");

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(armazenamentoImagemGateway.salvarImagem(any(ImagemUsuario.class))).thenReturn("url-imagem");
        when(usuarioGateway.atualizar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = uploadImagemPerfilUseCase.executar(command);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getImagemUsuario());
        verify(usuarioGateway).buscarPorId(usuarioId);
        verify(armazenamentoImagemGateway).salvarImagem(any(ImagemUsuario.class));
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        UploadImagemCommand command = new UploadImagemCommand(idInexistente, dadosImagem, "perfil.jpg");

        when(usuarioGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        UsuarioException.UsuarioNaoEncontradoException exception = assertThrows(
                UsuarioException.UsuarioNaoEncontradoException.class,
                () -> uploadImagemPerfilUseCase.executar(command)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
        verify(armazenamentoImagemGateway, never()).salvarImagem(any(ImagemUsuario.class));
    }

    @Test
    @DisplayName("Deve remover imagem anterior ao fazer upload de nova imagem")
    void deveRemoverImagemAnteriorAoFazerUpload() {
        // Arrange
        UUID imagemAnteriorId = UUID.randomUUID();
        ImagemUsuario imagemAnterior = new ImagemUsuario(imagemAnteriorId, new byte[]{6, 7, 8}, "anterior.jpg");
        usuarioExistente.atualizarImagemUsuario(imagemAnterior);

        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        UploadImagemCommand command = new UploadImagemCommand(usuarioId, dadosImagem, "perfil.jpg");

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        doNothing().when(armazenamentoImagemGateway).removerImagem(anyString(), any(UUID.class));
        when(armazenamentoImagemGateway.salvarImagem(any(ImagemUsuario.class))).thenReturn("url-nova-imagem");
        when(usuarioGateway.atualizar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = uploadImagemPerfilUseCase.executar(command);

        // Assert
        assertNotNull(resultado);
        verify(armazenamentoImagemGateway).removerImagem("anterior.jpg", imagemAnteriorId);
        verify(armazenamentoImagemGateway).salvarImagem(any(ImagemUsuario.class));
    }

    @Test
    @DisplayName("Deve continuar upload mesmo quando remoção de imagem anterior falha")
    void deveContinuarUploadMesmoQuandoRemocaoFalha() {
        // Arrange
        UUID imagemAnteriorId = UUID.randomUUID();
        ImagemUsuario imagemAnterior = new ImagemUsuario(imagemAnteriorId, new byte[]{6, 7, 8}, "anterior.jpg");
        usuarioExistente.atualizarImagemUsuario(imagemAnterior);

        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        UploadImagemCommand command = new UploadImagemCommand(usuarioId, dadosImagem, "perfil.jpg");

        when(usuarioGateway.buscarPorId(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        doThrow(new RuntimeException("Erro ao remover")).when(armazenamentoImagemGateway).removerImagem(anyString(), any(UUID.class));
        when(armazenamentoImagemGateway.salvarImagem(any(ImagemUsuario.class))).thenReturn("url-nova-imagem");
        when(usuarioGateway.atualizar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = uploadImagemPerfilUseCase.executar(command);

        // Assert
        assertNotNull(resultado);
        verify(armazenamentoImagemGateway).salvarImagem(any(ImagemUsuario.class));
        verify(usuarioGateway).atualizar(any(Usuario.class));
    }
}
