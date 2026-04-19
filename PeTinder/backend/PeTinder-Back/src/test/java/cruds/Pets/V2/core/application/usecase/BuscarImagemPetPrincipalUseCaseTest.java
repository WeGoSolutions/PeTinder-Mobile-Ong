package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarImagemPetPrincipalUseCaseTest {

    @Mock
    private ImagemPetJpaRepository imagemPetRepository;

    @Mock
    private ArmazenamentoImagemPetGateway armazenamentoImagemGateway;

    private BuscarImagemPetPrincipalUseCase buscarImagemPetPrincipalUseCase;

    private UUID petId;

    @BeforeEach
    void setUp() {
        buscarImagemPetPrincipalUseCase = new BuscarImagemPetPrincipalUseCase(imagemPetRepository, armazenamentoImagemGateway);
        petId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve buscar imagem principal do pet com sucesso")
    void deveBuscarImagemPrincipalDoPetComSucesso() {
        // Arrange
        ImagemPetEntity imagemEntity = mock(ImagemPetEntity.class);
        when(imagemEntity.getKeyS3()).thenReturn("key-s3");

        ImagemPet imagemPet = new ImagemPet(UUID.randomUUID(), "imagem.jpg", new byte[]{1, 2, 3, 4, 5}, "key-s3");

        when(imagemPetRepository.findByPetId(petId)).thenReturn(Arrays.asList(imagemEntity));
        when(armazenamentoImagemGateway.buscarPorKey("key-s3")).thenReturn(imagemPet);

        // Act
        String resultado = buscarImagemPetPrincipalUseCase.buscarImagemPrincipal(petId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.startsWith("data:image/jpeg;base64,"));
        verify(imagemPetRepository).findByPetId(petId);
        verify(armazenamentoImagemGateway).buscarPorKey("key-s3");
    }

    @Test
    @DisplayName("Deve retornar null quando pet não tem imagens")
    void deveRetornarNullQuandoPetNaoTemImagens() {
        // Arrange
        when(imagemPetRepository.findByPetId(petId)).thenReturn(Collections.emptyList());

        // Act
        String resultado = buscarImagemPetPrincipalUseCase.buscarImagemPrincipal(petId);

        // Assert
        assertNull(resultado);
        verify(armazenamentoImagemGateway, never()).buscarPorKey(anyString());
    }

    @Test
    @DisplayName("Deve retornar null quando lista de imagens é null")
    void deveRetornarNullQuandoListaDeImagensNull() {
        // Arrange
        when(imagemPetRepository.findByPetId(petId)).thenReturn(null);

        // Act
        String resultado = buscarImagemPetPrincipalUseCase.buscarImagemPrincipal(petId);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve retornar null quando imagem não encontrada no S3")
    void deveRetornarNullQuandoImagemNaoEncontradaNoS3() {
        // Arrange
        ImagemPetEntity imagemEntity = mock(ImagemPetEntity.class);
        when(imagemEntity.getKeyS3()).thenReturn("key-s3");

        when(imagemPetRepository.findByPetId(petId)).thenReturn(Arrays.asList(imagemEntity));
        when(armazenamentoImagemGateway.buscarPorKey("key-s3")).thenReturn(null);

        // Act
        String resultado = buscarImagemPetPrincipalUseCase.buscarImagemPrincipal(petId);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve retornar null quando dados da imagem são null")
    void deveRetornarNullQuandoDadosDaImagemSaoNull() {
        // Arrange
        ImagemPetEntity imagemEntity = mock(ImagemPetEntity.class);
        when(imagemEntity.getKeyS3()).thenReturn("key-s3");

        ImagemPet imagemSemDados = new ImagemPet(UUID.randomUUID(), "imagem.jpg", null, "key-s3");

        when(imagemPetRepository.findByPetId(petId)).thenReturn(Arrays.asList(imagemEntity));
        when(armazenamentoImagemGateway.buscarPorKey("key-s3")).thenReturn(imagemSemDados);

        // Act
        String resultado = buscarImagemPetPrincipalUseCase.buscarImagemPrincipal(petId);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve retornar null quando ocorre exceção ao buscar imagem")
    void deveRetornarNullQuandoOcorreExcecaoAoBuscarImagem() {
        // Arrange
        ImagemPetEntity imagemEntity = mock(ImagemPetEntity.class);
        when(imagemEntity.getKeyS3()).thenReturn("key-s3");

        when(imagemPetRepository.findByPetId(petId)).thenReturn(Arrays.asList(imagemEntity));
        when(armazenamentoImagemGateway.buscarPorKey("key-s3")).thenThrow(new RuntimeException("Erro S3"));

        // Act
        String resultado = buscarImagemPetPrincipalUseCase.buscarImagemPrincipal(petId);

        // Assert
        assertNull(resultado);
    }
}
