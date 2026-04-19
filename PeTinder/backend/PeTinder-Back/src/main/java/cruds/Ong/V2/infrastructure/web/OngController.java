package cruds.Ong.V2.infrastructure.web;

import cruds.Ong.V2.core.application.usecase.*;
import cruds.Ong.V2.infrastructure.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController("ongControllerV2")
@RequestMapping("/api/ongs")
@Tag(name = "Ong v2", description = "Endpoints Clean Architecture para gerenciamento de ONGs")
@Validated
public class OngController {

    private final CriarOngUseCase criarOngUseCase;
    private final LoginOngUseCase loginOngUseCase;
    private final BuscarOngPorIdUseCase buscarOngPorIdUseCase;
    private final AtualizarOngUseCase atualizarOngUseCase;
    private final AtualizarSenhaOngUseCase atualizarSenhaOngUseCase;
    private final UploadImagemOngUseCase uploadImagemOngUseCase;
    private final BuscarImagemOngUseCase buscarImagemOngUseCase;
    private final ListarPetsOngUseCase listarPetsOngUseCase;
    private final ListarMensagensPendentesUseCase listarMensagensPendentesUseCase;
    private final RemoverOngUseCase removerOngUseCase;

    public OngController(CriarOngUseCase criarOngUseCase,
                        LoginOngUseCase loginOngUseCase,
                        BuscarOngPorIdUseCase buscarOngPorIdUseCase,
                        AtualizarOngUseCase atualizarOngUseCase,
                        AtualizarSenhaOngUseCase atualizarSenhaOngUseCase,
                        UploadImagemOngUseCase uploadImagemOngUseCase,
                        BuscarImagemOngUseCase buscarImagemOngUseCase,
                        ListarPetsOngUseCase listarPetsOngUseCase,
                        ListarMensagensPendentesUseCase listarMensagensPendentesUseCase,
                        RemoverOngUseCase removerOngUseCase) {
        this.criarOngUseCase = criarOngUseCase;
        this.loginOngUseCase = loginOngUseCase;
        this.buscarOngPorIdUseCase = buscarOngPorIdUseCase;
        this.atualizarOngUseCase = atualizarOngUseCase;
        this.atualizarSenhaOngUseCase = atualizarSenhaOngUseCase;
        this.uploadImagemOngUseCase = uploadImagemOngUseCase;
        this.buscarImagemOngUseCase = buscarImagemOngUseCase;
        this.listarPetsOngUseCase = listarPetsOngUseCase;
        this.listarMensagensPendentesUseCase = listarMensagensPendentesUseCase;
        this.removerOngUseCase = removerOngUseCase;
    }

    @Operation(summary = "Criar ONG")
    @PostMapping
    public ResponseEntity<OngResponseWebDTO> criarOng(@Valid @RequestBody CriarOngWebDTO request) {
        var ong = criarOngUseCase.cadastrar(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OngResponseWebDTO.fromDomain(ong));
    }

    @Operation(summary = "Realiza login da ONG")
    @PostMapping("/login")
    public ResponseEntity<LoginOngResponseWebDTO> login(@Valid @RequestBody LoginOngWebDTO request) {
        var ong = loginOngUseCase.logar(request.toCommand());
        return ResponseEntity.ok(LoginOngResponseWebDTO.fromDomain(ong));
    }

    @Operation(summary = "Retorna a ONG pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<OngResponseWebDTO> getOng(@PathVariable UUID id) {
        var ong = buscarOngPorIdUseCase.buscar(id);
        return ResponseEntity.ok(OngResponseWebDTO.fromDomain(ong));
    }

    @Operation(summary = "Atualiza a ONG")
    @PatchMapping("/{id}")
    public ResponseEntity<OngResponseWebDTO> updateOng(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarOngWebDTO request) {
        var ong = atualizarOngUseCase.atualizar(request.toCommand(id));
        return ResponseEntity.ok(OngResponseWebDTO.fromDomain(ong));
    }

    @Operation(summary = "Atualiza a senha da ONG")
    @PatchMapping("/{id}/senha")
    public ResponseEntity<OngResponseWebDTO> updatePassword(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarSenhaOngWebDTO request) {
        var ong = atualizarSenhaOngUseCase.atualizarSenha(request.toCommand(id));
        return ResponseEntity.ok(OngResponseWebDTO.fromDomain(ong));
    }

    @Operation(summary = "Atualiza a imagem da ONG")
    @PutMapping("/{id}/imagem")
    public ResponseEntity<OngUrlResponseWebDTO> updateImageOng(
            @PathVariable UUID id,
            @Valid @RequestBody UploadImagemOngWebDTO request) {
        var ong = uploadImagemOngUseCase.executar(request.toCommand(id));
        return ResponseEntity.ok(OngUrlResponseWebDTO.fromDomain(ong));
    }

    @Operation(summary = "Exibe a imagem da ONG")
    @GetMapping("/{id}/imagem/arquivo")
    public ResponseEntity<OngUrlResponseWebDTO> getOngImage(@PathVariable UUID id) {
        var ong = buscarOngPorIdUseCase.buscar(id);
        return ResponseEntity.ok(OngUrlResponseWebDTO.fromDomain(ong));
    }

    @Operation(summary = "Retorna imagem por índice")
    @GetMapping("/{id}/imagens/{indice}")
    public ResponseEntity<byte[]> getImagemPorIndice(
            @PathVariable UUID id,
            @PathVariable int indice) {
        byte[] dados = buscarImagemOngUseCase.buscarPorIndice(id, indice);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(dados, headers, HttpStatus.OK);
    }

    @Operation(summary = "Lista todos os pets da ONG")
    @GetMapping("/{id}/pets")
    public ResponseEntity<Page<PetOngResponseWebDTO>> listarPets(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        var petsPage = listarPetsOngUseCase.listarPets(id, pageable);

        if (petsPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Page.empty());
        }

        var response = petsPage.map(PetOngResponseWebDTO::fromPetInfo);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista mensagens pendentes para a ONG")
    @GetMapping("/{id}/mensagens-pendentes")
    public ResponseEntity<List<MensagemPendenteResponseWebDTO>> listarMensagensPendentes(
            @PathVariable UUID id) {
        var mensagens = listarMensagensPendentesUseCase.listar(id);
        var response = mensagens.stream()
                .map(MensagemPendenteResponseWebDTO::fromMensagem)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deletando a ONG")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOng(@PathVariable UUID id) {
        removerOngUseCase.remover(id);
        return ResponseEntity.noContent().build();
    }
}
