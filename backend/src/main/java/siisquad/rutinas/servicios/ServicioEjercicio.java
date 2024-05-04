package siisquad.rutinas.servicios;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import siisquad.rutinas.entities.Ejercicio;
import siisquad.rutinas.excepciones.EntidadNoEncontradaException;
import siisquad.rutinas.repositories.RepositorioEjercicio;

import java.util.List;

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

    public Long aniadirEjercicio(Long idEntrenador, Ejercicio ejercicio){
        return null;
    }


    public Ejercicio updateEjercicio(Ejercicio ejercicio) {
        if (repositorioEjercicio.findById(ejercicio.getId()).isEmpty()) {
            throw new EntidadNoEncontradaException();
        }
        return repositorioEjercicio.save(ejercicio);
    }

    public void deleteEjercicio(Long id) {
        if (repositorioEjercicio.existsById(id)) {
            throw new EntidadNoEncontradaException();
        }
        repositorioEjercicio.deleteById(id);
    }

    public List<Ejercicio> obtenerEjerciciosPorEntrenador(Long id){
        return null;
    }


}
