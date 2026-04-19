package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.core.domain.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadImagemPetUseCaseTest {

    @Mock
    private PetGateway petGateway;

    @Mock
    private ImagemPetGateway imagemPetGateway;

    @Mock
    private ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway;

    private UploadImagemPetUseCase uploadImagemPetUseCase;

    private UUID petId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        uploadImagemPetUseCase = new UploadImagemPetUseCase(petGateway, imagemPetGateway, armazenamentoImagemPetGateway);
        petId = UUID.randomUUID();

        petExistente = new Pet(
                petId,
                "Rex",
                2.5,
                "Médio",
                Arrays.asList("Brincalhão"),
                "Descrição",
                true,
                true,
                true,
                "MACHO",
                UUID.randomUUID()
        );
    }

    @Test
    @DisplayName("Deve fazer upload de imagens com sucesso")
    void deveFazerUploadDeImagensComSucesso() {
        // Arrange
        List<byte[]> imagensBytes = Arrays.asList(
                new byte[]{1, 2, 3, 4, 5},
                new byte[]{6, 7, 8, 9, 10}
        );
        List<String> nomesArquivos = Arrays.asList("imagem1.jpg", "imagem2.jpg");

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(armazenamentoImagemPetGateway.salvarImagem(any(ImagemPet.class), any(UUID.class)))
                .thenReturn("key-s3-1", "key-s3-2");
        when(imagemPetGateway.salvar(any(ImagemPet.class), any(UUID.class)))
                .thenReturn(new ImagemPet(UUID.randomUUID(), "imagem.jpg", null, "key"));
        when(imagemPetGateway.buscarKeysPorPetId(petId)).thenReturn(Arrays.asList("key-s3-1", "key-s3-2"));
        when(imagemPetGateway.buscarPorPetId(any(UUID.class), anyList()))
                .thenReturn(Arrays.asList(new ImagemPet(UUID.randomUUID(), "imagem.jpg", new byte[]{1}, "key")));
        when(petGateway.atualizar(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pet resultado = uploadImagemPetUseCase.uploadImagens(petId, imagensBytes, nomesArquivos);

        // Assert
        assertNotNull(resultado);
        verify(armazenamentoImagemPetGateway, times(2)).salvarImagem(any(ImagemPet.class), any(UUID.class));
        verify(imagemPetGateway, times(2)).salvar(any(ImagemPet.class), any(UUID.class));
        verify(petGateway).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet não encontrado")
    void deveLancarExcecaoQuandoPetNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(petGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        PetException.PetNaoEncontradoException exception = assertThrows(
                PetException.PetNaoEncontradoException.class,
                () -> uploadImagemPetUseCase.uploadImagens(idInexistente, 
                        Arrays.asList(new byte[]{1, 2, 3}), Arrays.asList("imagem.jpg"))
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de imagens é nula")
    void deveLancarExcecaoQuandoListaDeImagensNula() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act & Assert
        PetException.ImagemInvalidaException exception = assertThrows(
                PetException.ImagemInvalidaException.class,
                () -> uploadImagemPetUseCase.uploadImagens(petId, null, Arrays.asList("imagem.jpg"))
        );

        assertTrue(exception.getMessage().contains("pelo menos uma imagem"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de imagens é vazia")
    void deveLancarExcecaoQuandoListaDeImagensVazia() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act & Assert
        PetException.ImagemInvalidaException exception = assertThrows(
                PetException.ImagemInvalidaException.class,
                () -> uploadImagemPetUseCase.uploadImagens(petId, new ArrayList<>(), Arrays.asList("imagem.jpg"))
        );

        assertTrue(exception.getMessage().contains("pelo menos uma imagem"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando número de nomes não confere com número de imagens")
    void deveLancarExcecaoQuandoNumeroDeNomesNaoConfere() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act & Assert
        PetException.ImagemInvalidaException exception = assertThrows(
                PetException.ImagemInvalidaException.class,
                () -> uploadImagemPetUseCase.uploadImagens(petId, 
                        Arrays.asList(new byte[]{1, 2, 3}, new byte[]{4, 5, 6}), 
                        Arrays.asList("imagem.jpg"))
        );

        assertTrue(exception.getMessage().contains("não confere"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando imagem está vazia")
    void deveLancarExcecaoQuandoImagemVazia() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act & Assert
        PetException.ImagemInvalidaException exception = assertThrows(
                PetException.ImagemInvalidaException.class,
                () -> uploadImagemPetUseCase.uploadImagens(petId, 
                        Arrays.asList(new byte[]{}), 
                        Arrays.asList("imagem.jpg"))
        );

        assertTrue(exception.getMessage().contains("vazia"));
    }
}
