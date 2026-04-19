package cruds.Ong.V2.core.application.usecase;

import cruds.Ong.V2.core.adapter.PetOngGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarPetsOngUseCaseTest {

    @Mock
    private PetOngGateway petOngGateway;

    private ListarPetsOngUseCase listarPetsOngUseCase;

    private UUID ongId;

    @BeforeEach
    void setUp() {
        listarPetsOngUseCase = new ListarPetsOngUseCase(petOngGateway);
        ongId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Deve listar pets da ONG com sucesso")
    void deveListarPetsDaOngComSucesso() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        PetOngGateway.PetOngInfo petInfo = new PetOngGateway.PetOngInfo(
                ongId,
                UUID.randomUUID(),
                "Rex",
                2.5,
                "Médio",
                5,
                Arrays.asList("Brincalhão"),
                "Descrição",
                true,
                true,
                true,
                Arrays.asList("url-imagem"),
                "MACHO",
                Arrays.asList("LIKED")
        );

        Page<PetOngGateway.PetOngInfo> pagePets = new PageImpl<>(Arrays.asList(petInfo));
        when(petOngGateway.listarPetsPorOng(ongId, pageable)).thenReturn(pagePets);

        // Act
        Page<PetOngGateway.PetOngInfo> resultado = listarPetsOngUseCase.listarPets(ongId, pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Rex", resultado.getContent().get(0).getPetNome());
        verify(petOngGateway).listarPetsPorOng(ongId, pageable);
    }

    @Test
    @DisplayName("Deve retornar página vazia quando ONG não tem pets")
    void deveRetornarPaginaVaziaQuandoOngNaoTemPets() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<PetOngGateway.PetOngInfo> emptyPage = new PageImpl<>(Collections.emptyList());
        when(petOngGateway.listarPetsPorOng(ongId, pageable)).thenReturn(emptyPage);

        // Act
        Page<PetOngGateway.PetOngInfo> resultado = listarPetsOngUseCase.listarPets(ongId, pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve paginar resultados corretamente")
    void devePaginarResultadosCorretamente() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 5);
        List<PetOngGateway.PetOngInfo> pets = Arrays.asList(
                new PetOngGateway.PetOngInfo(ongId, UUID.randomUUID(), "Rex", 2.5, "Médio", 5, 
                        Arrays.asList("Brincalhão"), "Descrição", true, true, true, null, "MACHO", null),
                new PetOngGateway.PetOngInfo(ongId, UUID.randomUUID(), "Luna", 1.5, "Pequeno", 3, 
                        Arrays.asList("Dócil"), "Descrição", false, true, true, null, "FEMEA", null)
        );

        Page<PetOngGateway.PetOngInfo> pagePets = new PageImpl<>(pets, pageable, 10);
        when(petOngGateway.listarPetsPorOng(ongId, pageable)).thenReturn(pagePets);

        // Act
        Page<PetOngGateway.PetOngInfo> resultado = listarPetsOngUseCase.listarPets(ongId, pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        assertEquals(10, resultado.getTotalElements());
        assertEquals(2, resultado.getTotalPages());
    }
}
