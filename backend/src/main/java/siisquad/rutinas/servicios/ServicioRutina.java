package siisquad.rutinas.servicios;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siisquad.rutinas.excepciones.RutinaExistenteException;
import siisquad.rutinas.excepciones.RutinaNoEncontrada;
import siisquad.rutinas.repositories.RepositorioRutina;
import siisquad.rutinas.entities.Rutina;

@Service
@Transactional
public class ServicioRutina {
    private final RepositorioRutina repositorioRutina;

    @Autowired
    public ServicioRutina(RepositorioRutina repositorioRutina) {
        this.repositorioRutina = repositorioRutina;
    }

    /**
     * Obtiene Rutina por id
     * @param id Id de la Rutina
     * @Throws RutinaNoEncontrada si la rutina no existe
     * @return Rutina
     */
    public Rutina getRutina(Long id) {
        Optional<Rutina> rutina = repositorioRutina.findById(id);
        return rutina.orElseThrow(() -> new RutinaNoEncontrada());
    }

    /**
     * Crea Rutina en la base de datos
     * @param rutina Rutina a crear
     * @Throws RutinaExistenteException si la rutina ya existe previamente
     * @return Rutina creada
     */
    public Rutina createRutina(Rutina rutina) {
        if(repositorioRutina.findById(rutina.getId()).isPresent()) {
            throw new RutinaExistenteException();
        }
        return repositorioRutina.save(rutina);
    }

    /**
     * Actualiza Rutina en la base de datos
     * @param rutina Rutina a actualizar
     * @Throws RutinaNoEncontrada si la rutina no existe previamente
     * @return Rutina actualizada
     */
    public Rutina updateRutina(Rutina rutina) {
        if (repositorioRutina.findById(rutina.getId()).isEmpty()) {
            throw new RutinaNoEncontrada();
        }
        return repositorioRutina.save(rutina);
    }

    /**
     * Elimina Rutina de la base de datos
     * @param id Id de la Rutina
     * @Throws RutinaNoEncontrada si la rutina no existe previamente
     */
    public void deleteRutina(Long id) {
        if(!repositorioRutina.existsById(id)) {
            throw new RutinaNoEncontrada();
        }
        repositorioRutina.deleteById(id);
    }

    /**
     * Obtiene todas las Rutinas de un Entrenador
     * @param entrenadorId Id del Entrenador
     * @throws
     * @return Lista de Rutinas asociadas al Entrenador
     */
    public List<Rutina> getRutinasEntrenador(Long entrenadorId) {
        return repositorioRutina.findByEntrenador(entrenadorId);
    }
}