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
class CurtirPetStatusUseCaseTest {

    @Mock
    private PetStatusGateway petStatusGateway;

    @Mock
    private PetGateway petGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    private CurtirPetStatusUseCase curtirPetStatusUseCase;

    private UUID petId;
    private UUID userId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        curtirPetStatusUseCase = new CurtirPetStatusUseCase(petStatusGateway, petGateway, usuarioGateway);
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
    @DisplayName("Deve curtir pet com sucesso quando status não existe")
    void deveCurtirPetComSucessoQuandoStatusNaoExiste() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(usuarioGateway.existePorId(userId)).thenReturn(true);
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.empty());
        when(petStatusGateway.salvar(any(PetStatus.class))).thenAnswer(invocation -> {
            PetStatus status = invocation.getArgument(0);
            status.setId(UUID.randomUUID());
            return status;
        });
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        PetStatus resultado = curtirPetStatusUseCase.curtir(petId, userId);

        // Assert
        assertNotNull(resultado);
        assertEquals(PetStatusEnum.LIKED, resultado.getStatus());
        verify(petStatusGateway).salvar(any(PetStatus.class));
        verify(petGateway).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Deve atualizar para LIKED quando status existente não é LIKED")
    void deveAtualizarParaLikedQuandoStatusExistenteNaoELiked() {
        // Arrange
        PetStatus statusExistente = new PetStatus(petId, userId, PetStatusEnum.PENDING);
        statusExistente.setId(UUID.randomUUID());

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(usuarioGateway.existePorId(userId)).thenReturn(true);
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.of(statusExistente));
        when(petStatusGateway.atualizar(any(PetStatus.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        PetStatus resultado = curtirPetStatusUseCase.curtir(petId, userId);

        // Assert
        assertNotNull(resultado);
        assertEquals(PetStatusEnum.LIKED, resultado.getStatus());
        verify(petStatusGateway).atualizar(any(PetStatus.class));
    }

    @Test
    @DisplayName("Deve descurtir quando já está LIKED (toggle)")
    void deveDescurtirQuandoJaEstaLiked() {
        // Arrange
        PetStatus statusExistente = new PetStatus(petId, userId, PetStatusEnum.LIKED);
        UUID statusId = UUID.randomUUID();
        statusExistente.setId(statusId);

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(usuarioGateway.existePorId(userId)).thenReturn(true);
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.of(statusExistente));
        doNothing().when(petStatusGateway).remover(statusId);
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        PetStatus resultado = curtirPetStatusUseCase.curtir(petId, userId);

        // Assert
        assertNull(resultado);
        verify(petStatusGateway).remover(statusId);
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
                () -> curtirPetStatusUseCase.curtir(idInexistente, userId)
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
                () -> curtirPetStatusUseCase.curtir(petId, userIdInexistente)
        );

        assertTrue(exception.getMessage().contains(userIdInexistente.toString()));
    }

    @Test
    @DisplayName("Deve descurtir pet com sucesso")
    void deveDescurtirPetComSucesso() {
        // Arrange
        PetStatus statusExistente = new PetStatus(petId, userId, PetStatusEnum.LIKED);
        UUID statusId = UUID.randomUUID();
        statusExistente.setId(statusId);
        petExistente.setCurtidas(5);

        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.of(statusExistente));
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        doNothing().when(petStatusGateway).remover(statusId);
        when(petGateway.atualizar(any(Pet.class))).thenReturn(petExistente);

        // Act
        curtirPetStatusUseCase.descurtir(petId, userId);

        // Assert
        verify(petStatusGateway).remover(statusId);
        verify(petGateway).atualizar(any(Pet.class));
    }

    @Test
    @DisplayName("Não deve fazer nada ao descurtir quando status não existe")
    void naoDeveFazerNadaAoDescurtirQuandoStatusNaoExiste() {
        // Arrange
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.empty());

        // Act
        curtirPetStatusUseCase.descurtir(petId, userId);

        // Assert
        verify(petStatusGateway, never()).remover(any(UUID.class));
        verify(petGateway, never()).atualizar(any(Pet.class));
    }
}
