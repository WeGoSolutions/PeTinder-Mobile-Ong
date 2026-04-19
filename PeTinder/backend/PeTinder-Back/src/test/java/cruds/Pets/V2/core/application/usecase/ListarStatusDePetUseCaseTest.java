package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarStatusDePetUseCaseTest {

    @Mock
    private PetStatusGateway petStatusGateway;

    private ListarStatusDePetUseCase listarStatusDePetUseCase;

    @BeforeEach
    void setUp() {
        listarStatusDePetUseCase = new ListarStatusDePetUseCase(petStatusGateway);
    }

    @Test
    @DisplayName("Deve listar status de um pet")
    void deveListarStatusDeUmPet() {
        // Arrange
        UUID petId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        PetStatus status = new PetStatus(petId, userId, PetStatusEnum.LIKED);

        when(petStatusGateway.buscarPorPet(petId)).thenReturn(Arrays.asList(status));

        // Act
        List<PetStatusEnum> resultado = listarStatusDePetUseCase.executar(petId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(PetStatusEnum.LIKED, resultado.get(0));
        verify(petStatusGateway).buscarPorPet(petId);
    }

    @Test
    @DisplayName("Deve retornar ADOPTED quando não há status")
    void deveRetornarAdoptedQuandoNaoHaStatus() {
        // Arrange
        UUID petId = UUID.randomUUID();
        when(petStatusGateway.buscarPorPet(petId)).thenReturn(Collections.emptyList());

        // Act
        List<PetStatusEnum> resultado = listarStatusDePetUseCase.executar(petId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(PetStatusEnum.ADOPTED, resultado.get(0));
    }

    @Test
    @DisplayName("Deve listar múltiplos status")
    void deveListarMultiplosStatus() {
        // Arrange
        UUID petId = UUID.randomUUID();
        PetStatus status1 = new PetStatus(petId, UUID.randomUUID(), PetStatusEnum.LIKED);
        PetStatus status2 = new PetStatus(petId, UUID.randomUUID(), PetStatusEnum.PENDING);

        when(petStatusGateway.buscarPorPet(petId)).thenReturn(Arrays.asList(status1, status2));

        // Act
        List<PetStatusEnum> resultado = listarStatusDePetUseCase.executar(petId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(PetStatusEnum.LIKED));
        assertTrue(resultado.contains(PetStatusEnum.PENDING));
    }
}
