package siisquad.rutinas.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import siisquad.rutinas.entities.Rutina;

public interface RepositorioRutina extends JpaRepository<Rutina, Long> {
    Optional<List<Rutina>> findByEntrenador(Long entrenador);

}
