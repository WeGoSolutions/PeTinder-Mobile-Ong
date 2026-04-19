package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.ArmazenamentoImagemPetGateway;
import cruds.Pets.V2.core.adapter.ImagemPetGateway;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.ImagemPet;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.infrastructure.persistence.jpa.ImagemPetJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarImagemPetUseCaseTest {

    @Mock
    private PetGateway petGateway;

    @Mock
    private ImagemPetGateway imagemPetGateway;

    @Mock
    private ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway;

    @Mock
    private ImagemPetJpaRepository imagemPetJpaRepository;

    private BuscarImagemPetUseCase buscarImagemPetUseCase;

    private UUID petId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        buscarImagemPetUseCase = new BuscarImagemPetUseCase(petGateway, imagemPetGateway, 
                armazenamentoImagemPetGateway, imagemPetJpaRepository);
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
    @DisplayName("Deve listar imagens do pet com sucesso")
    void deveListarImagensDoPetComSucesso() {
        // Arrange
        List<String> keys = Arrays.asList("key-s3-1", "key-s3-2");
        ImagemPet imagem1 = new ImagemPet(UUID.randomUUID(), "imagem1.jpg", new byte[]{1, 2, 3}, "key-s3-1");
        ImagemPet imagem2 = new ImagemPet(UUID.randomUUID(), "imagem2.jpg", new byte[]{4, 5, 6}, "key-s3-2");

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(imagemPetJpaRepository.findKeysByPetId(petId)).thenReturn(keys);
        when(imagemPetGateway.buscarPorPetId(petId, keys)).thenReturn(Arrays.asList(imagem1, imagem2));

        // Act
        List<ImagemPet> resultado = buscarImagemPetUseCase.listarImagensPet(petId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(petGateway).buscarPorId(petId);
        verify(imagemPetJpaRepository).findKeysByPetId(petId);
        verify(imagemPetGateway).buscarPorPetId(petId, keys);
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
                () -> buscarImagemPetUseCase.listarImagensPet(idInexistente)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet não tem imagens")
    void deveLancarExcecaoQuandoPetNaoTemImagens() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(imagemPetJpaRepository.findKeysByPetId(petId)).thenReturn(Collections.emptyList());

        // Act & Assert
        PetException.ImagemNaoEncontradaException exception = assertThrows(
                PetException.ImagemNaoEncontradaException.class,
                () -> buscarImagemPetUseCase.listarImagensPet(petId)
        );

        assertTrue(exception.getMessage().contains("Nenhuma imagem encontrada"));
    }

    @Test
    @DisplayName("Deve buscar imagem por índice com sucesso")
    void deveBuscarImagemPorIndiceComSucesso() {
        // Arrange
        byte[] dadosImagem = new byte[]{1, 2, 3, 4, 5};
        ImagemPet imagem = new ImagemPet(UUID.randomUUID(), "imagem.jpg", dadosImagem, "key-s3");
        petExistente.setImagens(new ArrayList<>(Arrays.asList(imagem)));

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act
        byte[] resultado = buscarImagemPetUseCase.buscarImagemPorIndice(petId, 0);

        // Assert
        assertNotNull(resultado);
        assertArrayEquals(dadosImagem, resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando índice inválido")
    void deveLancarExcecaoQuandoIndiceInvalido() {
        // Arrange
        ImagemPet imagem = new ImagemPet(UUID.randomUUID(), "imagem.jpg", new byte[]{1, 2, 3}, "key-s3");
        petExistente.setImagens(new ArrayList<>(Arrays.asList(imagem)));

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act & Assert
        PetException.ImagemNaoEncontradaException exception = assertThrows(
                PetException.ImagemNaoEncontradaException.class,
                () -> buscarImagemPetUseCase.buscarImagemPorIndice(petId, 5)
        );

        assertTrue(exception.getMessage().contains("Índice"));
    }

    @Test
    @DisplayName("Deve listar URLs de imagens com sucesso")
    void deveListarUrlsDeImagensComSucesso() {
        // Arrange
        List<String> keys = Arrays.asList("key-s3-1");
        ImagemPet imagem = new ImagemPet(UUID.randomUUID(), "imagem.jpg", new byte[]{1, 2, 3}, "key-s3-1");

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(imagemPetJpaRepository.findKeysByPetId(petId)).thenReturn(keys);
        when(imagemPetGateway.buscarPorPetId(petId, keys)).thenReturn(Arrays.asList(imagem));

        // Act
        List<String> resultado = buscarImagemPetUseCase.listarUrlsImagens(petId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).startsWith("data:image/jpeg;base64,"));
    }
}
