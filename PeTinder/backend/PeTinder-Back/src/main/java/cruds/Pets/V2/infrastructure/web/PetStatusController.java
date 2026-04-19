package cruds.Pets.V2.infrastructure.web;

import cruds.Pets.V2.core.application.usecase.*;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetJpaRepository;
import cruds.Pets.V2.infrastructure.web.dto.*;
import cruds.Pets.V2.infrastructure.web.service.PetStatusQueryService;
import cruds.notificacoes.service.NotificacaoFanoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController("petStatusControllerV2")
@RequestMapping("/api/status")
@RequiredArgsConstructor
@Tag(name = "Pet Status v2", description = "Endpoints Clean Architecture para gerenciamento de status de pets")
public class PetStatusController {

    private final CriarOuAtualizarPetStatusUseCase criarOuAtualizarPetStatusUseCase;
    private final RemoverPetStatusUseCase removerPetStatusUseCase;
    private final BuscarPetStatusPorUsuarioEStatusUseCase buscarPetStatusPorUsuarioEStatusUseCase;
    private final ListarStatusDePetUseCase listarStatusDePetUseCase;
    private final AdotarPetPorUsuarioUseCase adotarPetPorUsuarioUseCase;
    private final ListarPetStatusLikedUseCase listarPetStatusLikedUseCase;
    private final CurtirPetUseCase curtirPetUseCase;
    private final PetStatusGateway petStatusGateway;
    private final PetGateway petGateway;
    private final PetJpaRepository petJpaRepository;
    private final PetStatusQueryService petStatusQueryService;
    private final NotificacaoFanoutService notificacaoFanoutService;

    @Operation(summary = "Lista os pets curtidos, podendo filtrar por usuário")
    @GetMapping("/liked")
    public ResponseEntity<List<PetStatusResponseWebDTO>> listarCurtidos(@RequestParam(required = false) UUID userId) {
        var statusPets = (userId == null)
                ? listarPetStatusLikedUseCase.listarTodos()
                : listarPetStatusLikedUseCase.listarPorUsuario(userId);
                
        if (statusPets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        
        List<PetStatusResponseWebDTO> response = statusPets.stream()
                .map(petStatus -> {
                    var pet = petJpaRepository.findById(petStatus.getPetId()).orElse(null);
                    return PetStatusResponseWebDTO.fromEntity(
                        convertToEntity(petStatus),
                        pet
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista todos os pets e o status de cada um para cada usuário")
    @GetMapping
    public ResponseEntity<Page<Map<String, Object>>> listarTodos(
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Map<String, Object>> pets = petStatusQueryService.getAllPetsWithUserStatus(pageable);

        if (pets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Cria ou atualiza o status de um pet para um usuário")
    @PostMapping
    public ResponseEntity<PetStatusResponseWebDTO> createOrUpdatePetStatus(@Valid @RequestBody PetStatusRequestWebDTO dto) {
        var petStatus = criarOuAtualizarPetStatusUseCase.executar(dto.getPetId(), dto.getUserId(), dto.getStatus());
        var pet = petJpaRepository.findById(petStatus.getPetId()).orElse(null);
        return ResponseEntity.ok(PetStatusResponseWebDTO.fromEntity(convertToEntity(petStatus), pet));
    }

    @Operation(summary = "Lista os pets disponíveis para um usuário que ainda não foram interagidos")
    @GetMapping("/disponivel/{userId}")
    public ResponseEntity<List<PetResponseGeralWebDTO>> listAvailablePetsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(petStatusQueryService.listAvailablePetsForUser(userId));
    }

    @Operation(summary = "Lista os pets de um usuário com um status específico")
    @GetMapping("/{userId}/{status}")
    public ResponseEntity<List<PetStatusResponseWebDTO>> getPetsByUserAndStatus(
            @PathVariable UUID userId,
            @PathVariable String status) {
        var pets = buscarPetStatusPorUsuarioEStatusUseCase.executar(userId, PetStatusEnum.valueOf(status));
        List<PetStatusResponseWebDTO> response = pets.stream()
                .map(petStatus -> {
                    var pet = petJpaRepository.findById(petStatus.getPetId()).orElse(null);
                    return PetStatusResponseWebDTO.fromEntity(convertToEntity(petStatus), pet);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove o status de um pet para um usuário específico")
    @DeleteMapping("/{petId}/{userId}")
    public ResponseEntity<Void> deletePetStatus(
            @PathVariable UUID petId,
            @PathVariable UUID userId) {
        removerPetStatusUseCase.executar(petId, userId);
        
        // Marcar pet como não adotado
        var pet = petGateway.buscarPorId(petId).orElse(null);
        if (pet != null) {
            pet.cancelarAdocao();
            petGateway.atualizar(pet);
        }
        
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lista os pets com status padrão para ONGs")
    @GetMapping("/default/{userId}")
    public ResponseEntity<Page<PetResponseGeralWebDTO>> listDefaultPets(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        var response = petStatusQueryService.listDefaultPets(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Define o status de um pet como LIKED para um usuário")
    @PostMapping("/liked/{petId}/{userId}")
    public ResponseEntity<?> setLikedStatus(
            @PathVariable UUID petId,
            @PathVariable UUID userId) {

        var existingStatusOpt = petStatusGateway.buscarPorPetEUsuario(petId, userId);

        if (existingStatusOpt.isPresent() && existingStatusOpt.get().getStatus() == PetStatusEnum.LIKED) {
            curtirPetUseCase.descurtir(petId);
            removerPetStatusUseCase.executar(petId, userId);
            return ResponseEntity.noContent().build();
        }

        curtirPetUseCase.curtir(petId);

        var petStatus = criarOuAtualizarPetStatusUseCase.executar(petId, userId, PetStatusEnum.LIKED);
        var pet = petJpaRepository.findById(petStatus.getPetId()).orElse(null);
        return ResponseEntity.ok(PetStatusResponseWebDTO.fromEntity(convertToEntity(petStatus), pet));
    }

    @Operation(summary = "Define o status de um pet como ADOPTED para um usuário")
    @PostMapping("/adopted/{petId}/{userId}")
    public ResponseEntity<PetStatusResponseWebDTO> adoptPet(
            @PathVariable UUID petId,
            @PathVariable UUID userId) {

        var petStatus = adotarPetPorUsuarioUseCase.executar(petId, userId);

        notificacaoFanoutService.notificarUsuarioSelecionado(petId, userId);
        notificacaoFanoutService.notificarDemaisInteressados(petId);

        var pet = petJpaRepository.findById(petStatus.getPetId()).orElse(null);
        return ResponseEntity.ok(PetStatusResponseWebDTO.fromEntity(convertToEntity(petStatus), pet));
    }

    @Operation(summary = "Define o status de um pet como PENDING para um usuário")
    @PostMapping("/pending/{petId}/{userId}")
    public ResponseEntity<PetStatusResponseWebDTO> setPendingStatus(
            @PathVariable UUID petId,
            @PathVariable UUID userId) {

        var petStatus = criarOuAtualizarPetStatusUseCase.executar(petId, userId, PetStatusEnum.PENDING);

        notificacaoFanoutService.inscreverUsuarioNoPet(petId, userId);

        var pet = petJpaRepository.findById(petStatus.getPetId()).orElse(null);
        return ResponseEntity.ok(PetStatusResponseWebDTO.fromEntity(convertToEntity(petStatus), pet));
    }

    @Operation(summary = "Lista todos os status de um pet específico")
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<PetStatusEnum>> getPetStatusList(@PathVariable UUID petId) {
        List<PetStatusEnum> statusList = listarStatusDePetUseCase.executar(petId);
        if (statusList.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(statusList);
    }

    @Operation(summary = "Lista os pets com status PENDING com informações das ONGs para um usuário específico")
    @GetMapping("/pending/ong/{userId}")
    public ResponseEntity<List<PetResponsePendingOngWebDTO>> listPendingPetsWithOngForUser(
            HttpServletRequest request,
            @PathVariable UUID userId) {
        List<PetResponsePendingOngWebDTO> pendingPets = petStatusQueryService.listPendingPetsWithOngForUser(userId);
        if (pendingPets.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(pendingPets);
    }

    @Operation(summary = "Lista os usuarios que estao com o status PENDING de um pet especifico")
    @GetMapping("/pending/user/{petId}")
    public ResponseEntity<List<PetResponseUserPendenteWebDTO>> listPendingUsersByPetId(@PathVariable UUID petId) {
        List<PetResponseUserPendenteWebDTO> userIds = petStatusQueryService.listPendingUsersByPetId(petId);
        if (userIds.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.ok(userIds);
    }

    @Operation(summary = "Pega todas as informações do pet e do adotante")
    @GetMapping("/adopted/{petId}")
    public ResponseEntity<PetResponseAdotanteWebDTO> getAdoptedInfoByPetId(@PathVariable UUID petId) {
        PetResponseAdotanteWebDTO dto = petStatusQueryService.getAdoptedInfoByPetId(petId);
        return ResponseEntity.ok(dto);
    }
    
    // Helper method to convert domain to entity for DTO conversion
    private PetStatusEntity convertToEntity(cruds.Pets.V2.core.domain.PetStatus petStatus) {
        return new PetStatusEntity(
            petStatus.getId(),
            petStatus.getPetId(),
            petStatus.getUserId(),
            petStatus.getStatus(),
            petStatus.getAlteradoParaPending(),
            petStatus.getDataCriacao()
        );
    }
}
