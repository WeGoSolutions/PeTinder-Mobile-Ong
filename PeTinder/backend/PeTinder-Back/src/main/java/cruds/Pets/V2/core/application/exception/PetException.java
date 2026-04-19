package cruds.Pets.V2.core.application.exception;

public class PetException extends RuntimeException {

    public PetException(String message) {
        super(message);
    }

    public PetException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class PetNaoEncontradoException extends PetException {
        public PetNaoEncontradoException(String message) {
            super(message);
        }
    }

    public static class OngNaoEncontradaException extends PetException {
        public OngNaoEncontradaException(String message) {
            super(message);
        }
    }

    public static class PetJaAdotadoException extends PetException {
        public PetJaAdotadoException(String message) {
            super(message);
        }
    }


    public static class DadosInvalidosException extends PetException {
        public DadosInvalidosException(String message) {
            super(message);
        }
    }

    public static class ImagemInvalidaException extends PetException {
        public ImagemInvalidaException(String message) {
            super(message);
        }
    }

    public static class ImagemNaoEncontradaException extends PetException {
        public ImagemNaoEncontradaException(String message) {
            super(message);
        }
    }
}