package siisquad.rutinas.servicios;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import siisquad.rutinas.entities.Ejercicio;
import siisquad.rutinas.excepciones.EntidadExistenteException;
import siisquad.rutinas.excepciones.EntidadNoEncontradaException;
import siisquad.rutinas.repositories.RepositorioEjercicio;

@Service
@Transactional
public class ServicioEjercicio {
    private final RepositorioEjercicio repositorioEjercicio;

    @Autowired
    public ServicioEjercicio(RepositorioEjercicio repositorioEjercicio) {
        this.repositorioEjercicio = repositorioEjercicio;
    }
    public Ejercicio getEjercicio(Long id){
        Optional<Ejercicio> ejercicio = repositorioEjercicio.findById(id);
        return ejercicio.orElseThrow(() -> new EntidadNoEncontradaException());
    }

    public Long addEjercicio(Long idEntrenador, Ejercicio ejercicio){
        if (repositorioEjercicio.existsByNombre(ejercicio.getNombre())) {
            throw new EntidadExistenteException("Ejercicio ya existe");
        }
        ejercicio.setEntrenador(idEntrenador.intValue());
		repositorioEjercicio.save(ejercicio);
		return ejercicio.getId();
    }

    public Ejercicio updateEjercicio(Ejercicio ejercicio) {
        return repositorioEjercicio.save(ejercicio);
    }

    public void deleteEjercicio(Long id) {
        repositorioEjercicio.deleteById(id);
    }

    public List<Ejercicio> getEjerciciosPorEntrenador(Long id){
        return repositorioEjercicio.findByEntrenador(Long.valueOf(id)).get();
    }


}
