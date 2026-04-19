package cruds.Dashboard.V2.core.application.exception;

public class DashboardException extends RuntimeException {

    public DashboardException(String message) {
        super(message);
    }

    public DashboardException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class NenhumPetEncontradoException extends DashboardException {
        public NenhumPetEncontradoException(String message) {
            super(message);
        }
    }

    public static class DashboardNaoEncontradoException extends DashboardException {
        public DashboardNaoEncontradoException(String message) {
            super(message);
        }
    }
}

