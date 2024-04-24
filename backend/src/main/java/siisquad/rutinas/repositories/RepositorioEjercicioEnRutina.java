package siisquad.rutinas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import siisquad.rutinas.entities.EjercicioEnRutina;

public interface RepositorioEjercicioEnRutina extends JpaRepository<EjercicioEnRutina, Long> {
}
