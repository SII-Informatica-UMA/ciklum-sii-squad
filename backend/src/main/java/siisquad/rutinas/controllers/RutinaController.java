package siisquad.rutinas.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import siisquad.rutinas.entities.Ejercicio;
import siisquad.rutinas.entities.EjercicioEnRutina;
import siisquad.rutinas.entities.Rutina;
import siisquad.rutinas.repositories.RepositorioEjercicio;
import siisquad.rutinas.repositories.RepositorioEjercicioEnRutina;
import siisquad.rutinas.repositories.RepositorioRutina;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rutina")
public class RutinaController {

    private final RepositorioRutina repositorioRutina;
    private final RepositorioEjercicio repositorioEjercicio;
    private final RepositorioEjercicioEnRutina repositorioEjercicioEnRutina;

    public RutinaController(RepositorioRutina repositorioRutina, RepositorioEjercicio repositorioEjercicio, RepositorioEjercicioEnRutina repositorioEjercicioEnRutina) {
        this.repositorioRutina = repositorioRutina;
        this.repositorioEjercicio = repositorioEjercicio;
        this.repositorioEjercicioEnRutina = repositorioEjercicioEnRutina;
    }

    @GetMapping
    public List<Rutina> getRutinasByEntrenador(@RequestParam Integer entrenador) {
        return repositorioRutina.findByEntrenador(entrenador);
    }

    @GetMapping("/{id}")
    public Rutina getRutinaByNombre(@PathVariable Long id) {
        return repositorioRutina.findById(id).orElse(null);
    }

    @GetMapping("/rutinas")
    public List<Rutina> getAllRutinas() {
        return repositorioRutina.findAll();
    }

    @PostMapping
    public Rutina createRutina(@RequestBody Rutina newRutina, @RequestParam Integer entrenador) {
        for (EjercicioEnRutina ejercicioEnRutina : newRutina.getEjercicios()) {
            Ejercicio ejercicio = ejercicioEnRutina.getEjercicio();
            ejercicio = repositorioEjercicio.save(ejercicio); // Primero guarda el Ejercicio
            ejercicioEnRutina.setEjercicio(ejercicio); // Actualiza el Ejercicio en EjercicioEnRutina
            repositorioEjercicioEnRutina.save(ejercicioEnRutina); // Luego guarda el EjercicioEnRutina
        }
        newRutina.setEntrenador(entrenador);
        return repositorioRutina.save(newRutina);
    }

    @PutMapping("/{id}")
    public Rutina updateRutina(@RequestBody Rutina newRutina, @PathVariable Long id) {
        return repositorioRutina.findById(id)
                .map(rutina -> {
                    rutina.setNombre(newRutina.getNombre());
                    rutina.setDescripcion(newRutina.getDescripcion());
                    return repositorioRutina.save(rutina);
                })
                .orElseGet(() -> {
                    newRutina.setId(id);
                    return repositorioRutina.save(newRutina);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteRutina(@PathVariable Long id) {
        repositorioRutina.deleteById(id);
    }
}
