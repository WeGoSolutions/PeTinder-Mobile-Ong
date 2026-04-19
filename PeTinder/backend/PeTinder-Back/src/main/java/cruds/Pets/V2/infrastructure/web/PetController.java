package cruds.Pets.V2.infrastructure.web;

import cruds.Pets.V2.core.application.usecase.*;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.infrastructure.web.dto.*;
import cruds.notificacoes.service.NotificacaoFanoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController("petControllerV2")
@RequestMapping("/api/pets")
@Tag(name = "Pet v2", description = "Endpoints Clean Architecture para gerenciamento de pets")
@Validated
public class PetController {

    private final CriarPetUseCase criarPetUseCase;
    private final BuscarPetPorIdUseCase buscarPetPorIdUseCase;
    private final ListarPetsUseCase listarPetsUseCase;
    private final AtualizarPetUseCase atualizarPetUseCase;
    private final RemoverPetUseCase removerPetUseCase;
    private final AdotarPetUseCase adotarPetUseCase;
    private final CurtirPetUseCase curtirPetUseCase;
    private final ListarPetsDisponivelParaUsuarioUseCase listarPetsDisponivelParaUsuarioUseCase;
    private final UploadImagemPetUseCase uploadImagemPetUseCase;
    private final BuscarImagemPetUseCase buscarImagemPetUseCase;
    private final RemoverImagemPetUseCase removerImagemPetUseCase;
    private final PetStatusGateway petStatusGateway;
    private final CriarOuAtualizarPetStatusUseCase criarOuAtualizarPetStatusUseCase;
    private final RemoverPetStatusUseCase removerPetStatusUseCase;
    private final NotificacaoFanoutService notificacaoFanoutService;

    public PetController(CriarPetUseCase criarPetUseCase,
                         BuscarPetPorIdUseCase buscarPetPorIdUseCase,
                         ListarPetsUseCase listarPetsUseCase,
                         AtualizarPetUseCase atualizarPetUseCase,
                         RemoverPetUseCase removerPetUseCase,
                         AdotarPetUseCase adotarPetUseCase,
                         CurtirPetUseCase curtirPetUseCase,
                         ListarPetsDisponivelParaUsuarioUseCase listarPetsDisponivelParaUsuarioUseCase,
                         UploadImagemPetUseCase uploadImagemPetUseCase,
                         BuscarImagemPetUseCase buscarImagemPetUseCase,
                         RemoverImagemPetUseCase removerImagemPetUseCase,
                         PetStatusGateway petStatusGateway,
                         CriarOuAtualizarPetStatusUseCase criarOuAtualizarPetStatusUseCase,
                         RemoverPetStatusUseCase removerPetStatusUseCase,
                         NotificacaoFanoutService notificacaoFanoutService) {
        this.criarPetUseCase = criarPetUseCase;
        this.buscarPetPorIdUseCase = buscarPetPorIdUseCase;
        this.listarPetsUseCase = listarPetsUseCase;
        this.atualizarPetUseCase = atualizarPetUseCase;
        this.removerPetUseCase = removerPetUseCase;
        this.adotarPetUseCase = adotarPetUseCase;
        this.curtirPetUseCase = curtirPetUseCase;
        this.listarPetsDisponivelParaUsuarioUseCase = listarPetsDisponivelParaUsuarioUseCase;
        this.uploadImagemPetUseCase = uploadImagemPetUseCase;
        this.buscarImagemPetUseCase = buscarImagemPetUseCase;
        this.removerImagemPetUseCase = removerImagemPetUseCase;
        this.petStatusGateway = petStatusGateway;
        this.criarOuAtualizarPetStatusUseCase = criarOuAtualizarPetStatusUseCase;
        this.removerPetStatusUseCase = removerPetStatusUseCase;
        this.notificacaoFanoutService = notificacaoFanoutService;
    }

    @Operation(summary = "Cria um novo pet")
    @PostMapping
    public ResponseEntity<PetResponseWebDTO> criarPet(@Valid @RequestBody CriarPetWebDTO request) {
        var pet = criarPetUseCase.cadastrar(request.toCommand());

        if (request.getImagensBase64() != null && !request.getImagensBase64().isEmpty()) {
            uploadImagemPetUseCase.uploadImagens(
                    pet.getId(),
                    request.getImagensBytes(),
                    request.getNomesArquivos()
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Busca pet por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PetResponseWebDTO> buscarPetPorId(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID userId) {
        var pet = buscarPetPorIdUseCase.buscar(id);

        // Se userId foi fornecido, buscar o status do pet para este usuário
        if (userId != null) {
            var petStatusOpt = petStatusGateway.buscarPorPetEUsuario(id, userId);
            var status = petStatusOpt.map(ps -> ps.getStatus()).orElse(null);
            return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet, status));
        }

        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Lista todos os pets")
    @GetMapping
    public ResponseEntity<List<PetResponseWebDTO>> listarPets() {
        var pets = listarPetsUseCase.listarTodos();
        var response = pets.stream()
                .map(PetResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista pets por ONG")
    @GetMapping("/ong/{ongId}")
    public ResponseEntity<List<PetResponseWebDTO>> listarPetsPorOng(@PathVariable UUID ongId) {
        var pets = listarPetsUseCase.listarPorOng(ongId);
        var response = pets.stream()
                .map(PetResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista pets disponíveis para adoção")
    @GetMapping("/disponiveis")
    public ResponseEntity<List<PetResponseWebDTO>> listarPetsDisponiveis() {
        var pets = listarPetsUseCase.listarDisponiveis();
        var response = pets.stream()
                .map(PetResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista pets disponíveis para um usuário específico")
    @GetMapping("/disponiveis/usuario/{userId}")
    public ResponseEntity<List<PetResponseWebDTO>> listarPetsDisponiveisParaUsuario(@PathVariable UUID userId) {
        var pets = listarPetsDisponivelParaUsuarioUseCase.listarDisponiveis(userId);


        var response = pets.stream()
                .map(PetResponseWebDTO::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Atualiza informações do pet")
    @PutMapping("/{id}")
    public ResponseEntity<PetResponseWebDTO> atualizarPet(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarPetWebDTO request) {
        var pet = atualizarPetUseCase.atualizar(request.toCommand(id));
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Remove pet")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPet(@PathVariable UUID id) {
        removerPetUseCase.remover(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Marca pet como adotado")
    @PatchMapping("/{id}/adotar")
    public ResponseEntity<PetResponseWebDTO> adotarPet(@PathVariable UUID id) {
        var pet = adotarPetUseCase.adotar(id);
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Cancela adoção do pet")
    @PatchMapping("/{id}/cancelar-adocao")
    public ResponseEntity<PetResponseWebDTO> cancelarAdocao(@PathVariable UUID id) {
        var pet = adotarPetUseCase.cancelarAdocao(id);
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Curte um pet")
    @PatchMapping("/{id}/curtir")
    public ResponseEntity<PetResponseWebDTO> curtirPet(@PathVariable UUID id) {
        var pet = curtirPetUseCase.curtir(id);
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Remove curtida de um pet")
    @PatchMapping("/{id}/descurtir")
    public ResponseEntity<PetResponseWebDTO> descurtirPet(@PathVariable UUID id) {
        var pet = curtirPetUseCase.descurtir(id);
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Curtir um pet (compatibilidade com rota antiga)")
    @PostMapping("/{petId}/curtir/{userId}")
    public ResponseEntity<?> curtirPetComUsuario(
            @PathVariable UUID petId,
            @PathVariable UUID userId) {
        // Buscar status existente
        var existingStatusOpt = petStatusGateway.buscarPorPetEUsuario(petId, userId);

        if (existingStatusOpt.isPresent() && existingStatusOpt.get().getStatus() == PetStatusEnum.LIKED) {
            curtirPetUseCase.descurtir(petId);
            removerPetStatusUseCase.executar(petId, userId);
            return ResponseEntity.noContent().build();
        }

        var pet = curtirPetUseCase.curtir(petId);

        // Criar o status LIKED
        criarOuAtualizarPetStatusUseCase.executar(petId, userId, PetStatusEnum.LIKED);

        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    // ========== ENDPOINTS DE IMAGEM ==========

    @Operation(summary = "Faz upload de imagens para um pet")
    @PostMapping("/{id}/upload-imagens")
    public ResponseEntity<PetResponseWebDTO> uploadImagens(
            @PathVariable UUID id,
            @RequestBody UploadImagemPetWebDTO request) {
        var pet = uploadImagemPetUseCase.uploadImagens(id, request.getImagensBytes(), request.getNomesArquivos());
        return ResponseEntity.ok(PetResponseWebDTO.fromDomain(pet));
    }

    @Operation(summary = "Lista URLs das imagens de um pet")
    @GetMapping("/{id}/imagens")
    public ResponseEntity<List<String>> listarUrlsImagens(
            HttpServletRequest request,
            @PathVariable UUID id) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        var urls = buscarImagemPetUseCase.listarUrlsImagens(id);
        return ResponseEntity.ok(urls);
    }

    @Operation(summary = "Busca imagem específica de um pet por índice")
    @GetMapping("/{id}/imagens/{indice}")
    public ResponseEntity<byte[]> buscarImagemPorIndice(
            @PathVariable UUID id,
            @PathVariable int indice) {
        byte[] imagem = buscarImagemPetUseCase.buscarImagemPorIndice(id, indice);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagem);
    }

    @Operation(summary = "Remove imagem específica de um pet por índice")
    @DeleteMapping("/{id}/imagens/{indice}")
    public ResponseEntity<Void> removerImagem(
            @PathVariable UUID id,
            @PathVariable int indice) {
        removerImagemPetUseCase.removerImagem(id, indice);
        return ResponseEntity.noContent().build();
    }
}

