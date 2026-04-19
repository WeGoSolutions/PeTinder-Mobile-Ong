package cruds.Users.V2.infrastructure.web;

import cruds.Users.V2.core.application.exception.UsuarioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "cruds.Users.V2.infrastructure.web")
public class UsuarioExceptionHandler {

    @ExceptionHandler(UsuarioException.UsuarioNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioNaoEncontrado(UsuarioException.UsuarioNaoEncontradoException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex.getMessage());
    }

    @ExceptionHandler(UsuarioException.ErroArmazenamentoException.class)
    public ResponseEntity<Map<String, Object>> handleErroArmazenamento(UsuarioException.ErroArmazenamentoException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro de armazenamento", ex.getMessage());
    }

    @ExceptionHandler(UsuarioException.EmailJaExisteException.class)
    public ResponseEntity<Map<String, Object>> handleEmailJaExiste(UsuarioException.EmailJaExisteException ex) {
        return createErrorResponse(HttpStatus.CONFLICT, "Email já existe", ex.getMessage());
    }

    @ExceptionHandler(UsuarioException.CpfJaExisteException.class)
    public ResponseEntity<Map<String, Object>> handleCpfJaExiste(UsuarioException.CpfJaExisteException ex) {
        return createErrorResponse(HttpStatus.CONFLICT, "CPF já existe", ex.getMessage());
    }

    @ExceptionHandler(UsuarioException.SenhaInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleSenhaInvalida(UsuarioException.SenhaInvalidaException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Senha inválida", ex.getMessage());
    }

    @ExceptionHandler(UsuarioException.IdadeInsuficienteException.class)
    public ResponseEntity<Map<String, Object>> handleIdadeInsuficiente(UsuarioException.IdadeInsuficienteException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Idade insuficiente", ex.getMessage());
    }

    @ExceptionHandler(UsuarioException.CredenciaisInvalidasException.class)
    public ResponseEntity<Map<String, Object>> handleCredenciaisInvalidas(UsuarioException.CredenciaisInvalidasException ex) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Credenciais inválidas", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Dados inválidos", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Dados de entrada inválidos");
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        body.put("validationErrors", errors);
        
        return ResponseEntity.badRequest().body(body);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
