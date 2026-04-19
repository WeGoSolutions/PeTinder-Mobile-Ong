package cruds.Users.V2.core.application.exception;

public class UsuarioException extends RuntimeException {
    
    public UsuarioException(String message) {
        super(message);
    }
    
    public UsuarioException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static class UsuarioNaoEncontradoException extends UsuarioException {
        public UsuarioNaoEncontradoException(String message) {
            super(message);
        }
    }
    
    public static class EmailJaExisteException extends UsuarioException {
        public EmailJaExisteException(String message) {
            super(message);
        }
    }
    
    public static class CpfJaExisteException extends UsuarioException {
        public CpfJaExisteException(String message) {
            super(message);
        }
    }
    
    public static class SenhaInvalidaException extends UsuarioException {
        public SenhaInvalidaException(String message) {
            super(message);
        }
    }
    
    public static class IdadeInsuficienteException extends UsuarioException {
        public IdadeInsuficienteException(String message) {
            super(message);
        }
    }
    
    public static class CredenciaisInvalidasException extends UsuarioException {
        public CredenciaisInvalidasException(String message) {
            super(message);
        }
    }

    public static class ErroArmazenamentoException extends RuntimeException {
        public ErroArmazenamentoException(String message) {
            super(message);
        }

        public ErroArmazenamentoException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
