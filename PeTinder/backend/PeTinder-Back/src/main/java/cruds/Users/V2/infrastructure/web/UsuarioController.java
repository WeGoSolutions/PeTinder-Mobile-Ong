package cruds.Users.V2.infrastructure.web;

import cruds.Users.V2.core.application.usecase.*;
import cruds.Users.V2.infrastructure.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuario v2", description = "Endpoints Clean Architecture para gerenciamento de usuários")
@Validated
public class UsuarioController {

    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final LoginUsuarioUseCase loginUsuarioUseCase;
    private final BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final AtualizarInformacoesOpcionaisUseCase atualizarInformacoesOpcionaisUseCase;
    private final AtualizarSenhaUseCase atualizarSenhaUseCase;
    private final UploadImagemPerfilUseCase uploadImagemPerfilUseCase;
    private final MarcarUsuarioExperienteUseCase marcarUsuarioExperienteUseCase;
    private final RemoverUsuarioUseCase removerUsuarioUseCase;
    private final ValidarEmailUseCase validarEmailUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final RemoverTodosUsuariosUseCase removerTodosUsuariosUseCase;

    public UsuarioController(CriarUsuarioUseCase criarUsuarioUseCase,
                           LoginUsuarioUseCase loginUsuarioUseCase,
                           BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase,
                           ListarUsuariosUseCase listarUsuariosUseCase,
                           AtualizarInformacoesOpcionaisUseCase atualizarInformacoesOpcionaisUseCase,
                           AtualizarSenhaUseCase atualizarSenhaUseCase,
                           UploadImagemPerfilUseCase uploadImagemPerfilUseCase,
                           MarcarUsuarioExperienteUseCase marcarUsuarioExperienteUseCase,
                           RemoverUsuarioUseCase removerUsuarioUseCase,
                           ValidarEmailUseCase validarEmailUseCase,
                           AtualizarUsuarioUseCase atualizarUsuarioUseCase,
                           RemoverTodosUsuariosUseCase removerTodosUsuariosUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.loginUsuarioUseCase = loginUsuarioUseCase;
        this.buscarUsuarioPorIdUseCase = buscarUsuarioPorIdUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.atualizarInformacoesOpcionaisUseCase = atualizarInformacoesOpcionaisUseCase;
        this.atualizarSenhaUseCase = atualizarSenhaUseCase;
        this.uploadImagemPerfilUseCase = uploadImagemPerfilUseCase;
        this.marcarUsuarioExperienteUseCase = marcarUsuarioExperienteUseCase;
        this.removerUsuarioUseCase = removerUsuarioUseCase;
        this.validarEmailUseCase = validarEmailUseCase;
        this.atualizarUsuarioUseCase = atualizarUsuarioUseCase;
        this.removerTodosUsuariosUseCase = removerTodosUsuariosUseCase;
    }

    @Operation(summary = "Cria um novo usuário")
    @PostMapping
    public ResponseEntity<UsuarioResponseWebDTO> criarUsuario(@Valid @RequestBody CriarUsuarioWebDTO request) {
        var usuario = criarUsuarioUseCase.cadastrar(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioResponseWebDTO.fromDomain(usuario));
    }

    @Operation(summary = "Realiza login do usuário")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseWebDTO> login(@Valid @RequestBody LoginUsuarioWebDTO request) {
        var result = loginUsuarioUseCase.logar(request.toCommand());
        return ResponseEntity.ok(LoginResponseWebDTO.fromResult(result));
    }

    @Operation(summary = "Busca usuário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseWebDTO> buscarUsuarioPorId(@PathVariable UUID id) {
        var usuario = buscarUsuarioPorIdUseCase.buscar(id);
        return ResponseEntity.ok(UsuarioResponseWebDTO.fromDomain(usuario));
    }

    @Operation(summary = "Lista todos os usuários")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseWebDTO>> listarUsuarios() {
        var usuarios = listarUsuariosUseCase.listar();
        var response = usuarios.stream()
                .map(UsuarioResponseWebDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Valida e-mail do usuário")
    @GetMapping("/{email}/validar-email")
    public ResponseEntity<UsuarioResponseWebDTO> validarEmail(@PathVariable String email) {
        var usuario = validarEmailUseCase.validar(email);
        return ResponseEntity.ok(UsuarioResponseWebDTO.fromDomain(usuario));
    }

    @Operation(summary = "Atualiza informações do usuário")
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponseWebDTO> atualizarUsuario(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarUsuarioWebDTO request) {
        var usuario = atualizarUsuarioUseCase.atualizar(request.toCommand(id));
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(UsuarioResponseWebDTO.fromDomain(usuario));
    }

    @Operation(summary = "Marca usuário como experiente")
    @PatchMapping("/{id}/user-novo")
    public ResponseEntity<UsuarioResponseWebDTO> marcarUsuarioExperiente(@PathVariable UUID id) {
        var usuario = marcarUsuarioExperienteUseCase.executar(id);
        return ResponseEntity.ok(UsuarioResponseWebDTO.fromDomain(usuario));
    }

    @Operation(summary = "Atualiza informações opcionais do usuário")
    @PutMapping("/{id}/optional")
    public ResponseEntity<UsuarioResponseWebDTO> atualizarInformacoesOpcionais(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarInformacoesOpcionaisWebDTO request) {
        var usuario = atualizarInformacoesOpcionaisUseCase.adicionarInfos(request.toCommand(id));
        return ResponseEntity.ok(UsuarioResponseWebDTO.fromDomain(usuario));
    }

    @Operation(summary = "Atualiza senha do usuário")
    @PatchMapping("/{email}/senha")
    public ResponseEntity<UsuarioResponseWebDTO> atualizarSenha(
            @PathVariable String email,
            @Valid @RequestBody AtualizarSenhaWebDTO request) {
        System.out.println("Request recebido: " + request); // Log de debug
        var usuario = atualizarSenhaUseCase.atualizarSenha(request.toCommand(email));
        return ResponseEntity.ok(UsuarioResponseWebDTO.fromDomain(usuario));
    }

    @Operation(summary = "Upload de imagem de perfil")
    @PostMapping("/{id}/imagem")
    public ResponseEntity<UsuarioResponseWebDTO> uploadImagemPerfil(
            @PathVariable UUID id,
            @Valid @RequestBody UploadImagemWebDTO request) {
        var usuario = uploadImagemPerfilUseCase.executar(request.toCommand(id));
        return ResponseEntity.ok(UsuarioResponseWebDTO.fromDomain(usuario));
    }

    @Operation(summary = "Remove usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerUsuario(@PathVariable UUID id) {
        removerUsuarioUseCase.apagarUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove todos os usuários (apenas para teste)")
    @DeleteMapping("/teste")
    public ResponseEntity<Void> removerTodosUsuarios() {
        removerTodosUsuariosUseCase.removerTodos();
        return ResponseEntity.noContent().build();
    }
}
