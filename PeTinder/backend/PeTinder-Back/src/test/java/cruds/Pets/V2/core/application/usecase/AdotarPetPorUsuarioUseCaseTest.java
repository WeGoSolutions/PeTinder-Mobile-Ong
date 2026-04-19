package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdotarPetPorUsuarioUseCaseTest {

    @Mock
    private PetStatusGateway petStatusGateway;

    @Mock
    private PetGateway petGateway;

    private AdotarPetPorUsuarioUseCase adotarPetPorUsuarioUseCase;

    private UUID petId;
    private UUID userId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        adotarPetPorUsuarioUseCase = new AdotarPetPorUsuarioUseCase(petStatusGateway, petGateway);
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
    @DisplayName("Deve adotar pet por usuário com sucesso quando não existe status prévio")
    void deveAdotarPetPorUsuarioComSucessoQuandoNaoExisteStatusPrevio() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petStatusGateway.buscarPorPet(petId)).thenReturn(Collections.emptyList());
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.empty());
        when(petStatusGateway.salvar(any(PetStatus.class))).thenAnswer(invocation -> {
            PetStatus status = invocation.getArgument(0);
            status.setId(UUID.randomUUID());
            return status;
        });
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        PetStatus resultado = adotarPetPorUsuarioUseCase.executar(petId, userId);

        // Assert
        assertNotNull(resultado);
        assertEquals(PetStatusEnum.ADOPTED, resultado.getStatus());
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
        when(petStatusGateway.buscarPorPet(petId)).thenReturn(Arrays.asList(statusExistente));
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.of(statusExistente));
        when(petStatusGateway.atualizar(any(PetStatus.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        PetStatus resultado = adotarPetPorUsuarioUseCase.executar(petId, userId);

        // Assert
        assertNotNull(resultado);
        assertEquals(PetStatusEnum.ADOPTED, resultado.getStatus());
        verify(petStatusGateway).atualizar(any(PetStatus.class));
    }

    @Test
    @DisplayName("Deve remover status de outros usuários ao adotar")
    void deveRemoverStatusDeOutrosUsuariosAoAdotar() {
        // Arrange
        UUID outroUserId = UUID.randomUUID();
        PetStatus statusOutroUsuario = new PetStatus(petId, outroUserId, PetStatusEnum.LIKED);
        UUID statusId = UUID.randomUUID();
        statusOutroUsuario.setId(statusId);

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petStatusGateway.buscarPorPet(petId)).thenReturn(Arrays.asList(statusOutroUsuario));
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.empty());
        doNothing().when(petStatusGateway).remover(statusId);
        when(petStatusGateway.salvar(any(PetStatus.class))).thenAnswer(invocation -> {
            PetStatus status = invocation.getArgument(0);
            status.setId(UUID.randomUUID());
            return status;
        });
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        PetStatus resultado = adotarPetPorUsuarioUseCase.executar(petId, userId);

        // Assert
        assertNotNull(resultado);
        verify(petStatusGateway).remover(statusId);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pet não encontrado")
    void deveLancarExcecaoQuandoPetNaoEncontrado() {
        // Arrange
        UUID idInexistente = UUID.randomUUID();
        when(petGateway.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        PetException exception = assertThrows(
                PetException.class,
                () -> adotarPetPorUsuarioUseCase.executar(idInexistente, userId)
        );

        assertEquals("Pet não encontrado", exception.getMessage());
    }
}
