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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoverImagemPetUseCaseTest {

    @Mock
    private PetGateway petGateway;

    @Mock
    private ImagemPetGateway imagemPetGateway;

    @Mock
    private ArmazenamentoImagemPetGateway armazenamentoImagemPetGateway;

    private RemoverImagemPetUseCase removerImagemPetUseCase;

    private UUID petId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        removerImagemPetUseCase = new RemoverImagemPetUseCase(petGateway, imagemPetGateway, armazenamentoImagemPetGateway);
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
    @DisplayName("Deve remover imagem com sucesso")
    void deveRemoverImagemComSucesso() {
        // Arrange
        UUID imagemId = UUID.randomUUID();
        ImagemPet imagem = new ImagemPet(imagemId, "imagem.jpg", new byte[]{1, 2, 3}, "key-s3");
        petExistente.setImagens(new ArrayList<>(Arrays.asList(imagem)));

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        doNothing().when(armazenamentoImagemPetGateway).removerImagem(anyString(), any(UUID.class));
        doNothing().when(imagemPetGateway).remover(anyString());
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        removerImagemPetUseCase.removerImagem(petId, 0);

        // Assert
        verify(armazenamentoImagemPetGateway).removerImagem("imagem.jpg", imagemId);
        verify(imagemPetGateway).remover("key-s3");
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
                () -> removerImagemPetUseCase.removerImagem(idInexistente, 0)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet não tem imagens")
    void deveLancarExcecaoQuandoPetNaoTemImagens() {
        // Arrange
        petExistente.setImagens(new ArrayList<>());
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act & Assert
        PetException.ImagemNaoEncontradaException exception = assertThrows(
                PetException.ImagemNaoEncontradaException.class,
                () -> removerImagemPetUseCase.removerImagem(petId, 0)
        );

        assertTrue(exception.getMessage().contains("Nenhuma imagem encontrada"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando índice é negativo")
    void deveLancarExcecaoQuandoIndiceNegativo() {
        // Arrange
        ImagemPet imagem = new ImagemPet(UUID.randomUUID(), "imagem.jpg", new byte[]{1, 2, 3}, "key-s3");
        petExistente.setImagens(new ArrayList<>(Arrays.asList(imagem)));
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act & Assert
        PetException.ImagemNaoEncontradaException exception = assertThrows(
                PetException.ImagemNaoEncontradaException.class,
                () -> removerImagemPetUseCase.removerImagem(petId, -1)
        );

        assertTrue(exception.getMessage().contains("Índice"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando índice é maior que total de imagens")
    void deveLancarExcecaoQuandoIndiceMaiorQueTotalDeImagens() {
        // Arrange
        ImagemPet imagem = new ImagemPet(UUID.randomUUID(), "imagem.jpg", new byte[]{1, 2, 3}, "key-s3");
        petExistente.setImagens(new ArrayList<>(Arrays.asList(imagem)));
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));

        // Act & Assert
        PetException.ImagemNaoEncontradaException exception = assertThrows(
                PetException.ImagemNaoEncontradaException.class,
                () -> removerImagemPetUseCase.removerImagem(petId, 5)
        );

        assertTrue(exception.getMessage().contains("Índice"));
        assertTrue(exception.getMessage().contains("Total de imagens"));
    }
}
