package cruds.Ong.V2.infrastructure.web;

import cruds.Ong.V2.core.application.exception.OngException;
import cruds.common.exception.BadRequestException;
import cruds.common.exception.ConflictException;
import cruds.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "cruds.Ong.V2.infrastructure.web")
public class OngExceptionHandler {

    @ExceptionHandler(OngException.EmailJaExisteException.class)
    public ResponseEntity<Map<String, String>> handleEmailJaExiste(OngException.EmailJaExisteException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(OngException.OngNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleOngNaoEncontrada(OngException.OngNaoEncontradaException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(OngException.SenhaInvalidaException.class)
    public ResponseEntity<Map<String, String>> handleSenhaInvalida(OngException.SenhaInvalidaException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(OngException.ImagemInvalidaException.class)
    public ResponseEntity<Map<String, String>> handleImagemInvalida(OngException.ImagemInvalidaException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(OngException.ErroArmazenamentoException.class)
    public ResponseEntity<Map<String, String>> handleErroArmazenamento(OngException.ErroArmazenamentoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}

