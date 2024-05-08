package siisquad.rutinas.excepciones;

public class EntidadNoEncontradaException extends RuntimeException {
    public EntidadNoEncontradaException() {
        super("Entidad no encontrada");
    }
}
