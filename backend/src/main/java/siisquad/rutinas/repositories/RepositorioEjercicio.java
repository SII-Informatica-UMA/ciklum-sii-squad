package siisquad.rutinas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import siisquad.rutinas.entities.Ejercicio;

public interface RepositorioEjercicio extends JpaRepository<Ejercicio, Long> {
    List<Ejercicio> findByEntrenador(Integer entrenador);
}
