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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarOuAtualizarPetStatusUseCaseTest {

    @Mock
    private PetStatusGateway petStatusGateway;

    @Mock
    private PetGateway petGateway;

    private CriarOuAtualizarPetStatusUseCase criarOuAtualizarPetStatusUseCase;

    private UUID petId;
    private UUID userId;
    private Pet petExistente;

    @BeforeEach
    void setUp() {
        criarOuAtualizarPetStatusUseCase = new CriarOuAtualizarPetStatusUseCase(petStatusGateway, petGateway);
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
    @DisplayName("Deve criar novo status quando não existe")
    void deveCriarNovoStatusQuandoNaoExiste() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.empty());
        when(petStatusGateway.salvar(any(PetStatus.class))).thenAnswer(invocation -> {
            PetStatus status = invocation.getArgument(0);
            status.setId(UUID.randomUUID());
            return status;
        });

        // Act
        PetStatus resultado = criarOuAtualizarPetStatusUseCase.executar(petId, userId, PetStatusEnum.LIKED);

        // Assert
        assertNotNull(resultado);
        assertEquals(PetStatusEnum.LIKED, resultado.getStatus());
        verify(petStatusGateway).salvar(any(PetStatus.class));
    }

    @Test
    @DisplayName("Deve atualizar status existente")
    void deveAtualizarStatusExistente() {
        // Arrange
        PetStatus statusExistente = new PetStatus(petId, userId, PetStatusEnum.LIKED);
        statusExistente.setId(UUID.randomUUID());

        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.of(statusExistente));
        when(petStatusGateway.atualizar(any(PetStatus.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        PetStatus resultado = criarOuAtualizarPetStatusUseCase.executar(petId, userId, PetStatusEnum.PENDING);

        // Assert
        assertNotNull(resultado);
        assertEquals(PetStatusEnum.PENDING, resultado.getStatus());
        verify(petStatusGateway).atualizar(any(PetStatus.class));
    }

    @Test
    @DisplayName("Deve definir alteradoParaPending quando status é PENDING")
    void deveDefinirAlteradoParaPendingQuandoStatusEPending() {
        // Arrange
        when(petGateway.buscarPorId(petId)).thenReturn(Optional.of(petExistente));
        when(petStatusGateway.buscarPorPetEUsuario(petId, userId)).thenReturn(Optional.empty());
        when(petStatusGateway.salvar(any(PetStatus.class))).thenAnswer(invocation -> {
            PetStatus status = invocation.getArgument(0);
            status.setId(UUID.randomUUID());
            return status;
        });

        // Act
        PetStatus resultado = criarOuAtualizarPetStatusUseCase.executar(petId, userId, PetStatusEnum.PENDING);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getAlteradoParaPending());
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
                () -> criarOuAtualizarPetStatusUseCase.executar(idInexistente, userId, PetStatusEnum.LIKED)
        );

        assertEquals("Pet não encontrado", exception.getMessage());
    }
}
