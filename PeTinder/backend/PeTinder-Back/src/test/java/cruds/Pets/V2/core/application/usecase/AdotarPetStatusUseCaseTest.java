package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdotarPetStatusUseCaseTest {

    @Mock
    private PetStatusGateway petStatusGateway;

    @Mock
    private PetGateway petGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    private AdotarPetStatusUseCase adotarPetStatusUseCase;

    private UUID petId;
    private UUID userId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        adotarPetStatusUseCase = new AdotarPetStatusUseCase(petStatusGateway, petGateway, usuarioGateway);
        petId = UUID.randomUUID();
        userId = UUID.randomUUID();

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
    @DisplayName("Deve adotar pet com sucesso quando status não existe")
    void deveAdotarPetComSucessoQuandoStatusNaoExiste() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(usuarioGateway.existePorId(userId)).thenReturn(true);
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.empty());
        doNothing().when(petStatusGateway).removerPorPet(petId);
        when(petStatusGateway.salvar(any(PetStatus.class))).thenAnswer(invocation -> {
            PetStatus status = invocation.getArgument(0);
            status.setId(UUID.randomUUID());
            return status;
        });
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        PetStatus resultado = adotarPetStatusUseCase.adotar(petId, userId);

        // Assert
        assertNotNull(resultado);
        assertEquals(PetStatusEnum.ADOPTED, resultado.getStatus());
        verify(petStatusGateway).removerPorPet(petId);
        verify(petStatusGateway).salvar(any(PetStatus.class));
        verify(petGateway).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve atualizar status existente para ADOPTED")
    void deveAtualizarStatusExistenteParaAdopted() {
        // Arrange
        PetStatus statusExistente = new PetStatus(petId, userId, PetStatusEnum.LIKED);
        statusExistente.setId(UUID.randomUUID());

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(usuarioGateway.existePorId(userId)).thenReturn(true);
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.of(statusExistente));
        doNothing().when(petStatusGateway).removerPorPet(petId);
        when(petStatusGateway.atualizar(any(PetStatus.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        PetStatus resultado = adotarPetStatusUseCase.adotar(petId, userId);

        // Assert
        assertNotNull(resultado);
        assertEquals(PetStatusEnum.ADOPTED, resultado.getStatus());
        verify(petStatusGateway).atualizar(any(PetStatus.class));
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
                () -> adotarPetStatusUseCase.adotar(idInexistente, userId)
        );

        assertTrue(exception.getMessage().contains(idInexistente.toString()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        UUID userIdInexistente = UUID.randomUUID();
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(usuarioGateway.existePorId(userIdInexistente)).thenReturn(false);

        // Act & Assert
        PetException.PetNaoEncontradoException exception = assertThrows(
                PetException.PetNaoEncontradoException.class,
                () -> adotarPetStatusUseCase.adotar(petId, userIdInexistente)
        );

        assertTrue(exception.getMessage().contains(userIdInexistente.toString()));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet já foi adotado")
    void deveLancarExcecaoQuandoPetJaAdotado() {
        // Arrange
        petExistente.adotar();
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(usuarioGateway.existePorId(userId)).thenReturn(true);

        // Act & Assert
        PetException.PetJaAdotadoException exception = assertThrows(
                PetException.PetJaAdotadoException.class,
                () -> adotarPetStatusUseCase.adotar(petId, userId)
        );

        assertTrue(exception.getMessage().contains(petId.toString()));
    }

    @Test
    @DisplayName("Deve cancelar adoção com sucesso")
    void deveCancelarAdocaoComSucesso() {
        // Arrange
        petExistente.adotar();
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        doNothing().when(petStatusGateway).removerPorPet(petId);
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        adotarPetStatusUseCase.cancelarAdocao(petId);

        // Assert
        verify(petStatusGateway).removerPorPet(petId);
        verify(petGateway).atualizar(any(Pet.class));
    }
}
