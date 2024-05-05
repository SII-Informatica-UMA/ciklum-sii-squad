package siisquad.rutinas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import siisquad.rutinas.entities.Ejercicio;

public interface RepositorioEjercicio extends JpaRepository<Ejercicio, Long> {
    Optional<List<Ejercicio>> findByEntrenador(Long entrenador);

    boolean existsByNombre(String nombre);
}
