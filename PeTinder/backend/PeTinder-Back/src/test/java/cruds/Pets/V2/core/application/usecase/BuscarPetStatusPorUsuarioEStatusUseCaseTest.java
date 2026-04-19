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
class BuscarPetStatusPorUsuarioEStatusUseCaseTest {

    @Mock
    private PetStatusGateway petStatusGateway;

    private BuscarPetStatusPorUsuarioEStatusUseCase buscarPetStatusPorUsuarioEStatusUseCase;

    @BeforeEach
    void setUp() {
        buscarPetStatusPorUsuarioEStatusUseCase = new BuscarPetStatusPorUsuarioEStatusUseCase(petStatusGateway);
    }

    @Test
    @DisplayName("Deve buscar status por usuário e status")
    void deveBuscarStatusPorUsuarioEStatus() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID petId = UUID.randomUUID();
        PetStatus status = new PetStatus(petId, userId, PetStatusEnum.LIKED);

        when(petStatusGateway.buscarPorUsuarioEStatus(userId, PetStatusEnum.LIKED))
                .thenReturn(Arrays.asList(status));

        // Act
        List<PetStatus> resultado = buscarPetStatusPorUsuarioEStatusUseCase.executar(userId, PetStatusEnum.LIKED);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(PetStatusEnum.LIKED, resultado.get(0).getStatus());
        assertEquals(userId, resultado.get(0).getUserId());
        verify(petStatusGateway).buscarPorUsuarioEStatus(userId, PetStatusEnum.LIKED);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há status")
    void deveRetornarListaVaziaQuandoNaoHaStatus() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(petStatusGateway.buscarPorUsuarioEStatus(userId, PetStatusEnum.PENDING))
                .thenReturn(Collections.emptyList());

        // Act
        List<PetStatus> resultado = buscarPetStatusPorUsuarioEStatusUseCase.executar(userId, PetStatusEnum.PENDING);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
