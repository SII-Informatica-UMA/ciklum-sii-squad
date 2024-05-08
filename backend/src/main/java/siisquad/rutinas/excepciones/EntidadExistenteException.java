package siisquad.rutinas.excepciones;

public class EntidadExistenteException extends RuntimeException {
    private static final int STATUS_CODE = 409;

    public EntidadExistenteException(String message) {
        super(message);
    }

    public int getStatusCode() {
        return STATUS_CODE;
    }
}
