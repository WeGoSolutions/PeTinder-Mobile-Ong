package cruds.Ong.V2.core.application.exception;

public class OngException extends RuntimeException {

    public OngException(String message) {
        super(message);
    }

    public OngException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class EmailJaExisteException extends OngException {
        public EmailJaExisteException(String message) {
            super(message);
        }
    }

    public static class OngNaoEncontradaException extends OngException {
        public OngNaoEncontradaException(String message) {
            super(message);
        }
    }

    public static class SenhaInvalidaException extends OngException {
        public SenhaInvalidaException(String message) {
            super(message);
        }
    }

    public static class ImagemInvalidaException extends OngException {
        public ImagemInvalidaException(String message) {
            super(message);
        }
    }

    public static class ErroArmazenamentoException extends OngException {
        public ErroArmazenamentoException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

