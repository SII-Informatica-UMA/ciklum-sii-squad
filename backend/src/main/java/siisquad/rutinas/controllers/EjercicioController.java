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
import siisquad.rutinas.repositories.RepositorioEjercicio;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ejercicio")
public class EjercicioController {

    private final RepositorioEjercicio repositorioEjercicio;

    public EjercicioController(RepositorioEjercicio ejercicioRepository) {
        this.repositorioEjercicio = ejercicioRepository;
    }

    @GetMapping
    public List<Ejercicio> getEjerciciosByEntrenador(@RequestParam Integer entrenador) {
        return repositorioEjercicio.findByEntrenador(entrenador);
    }

    @GetMapping("/{id}")
    public Ejercicio getEjercicioById(@PathVariable Long id) {
        return repositorioEjercicio.findById(id).orElse(null);
    }

    @GetMapping("/ejercicios")
    public List<Ejercicio> getAllEjercicios() {
        return repositorioEjercicio.findAll();
    }

    @PostMapping
    public Ejercicio createEjercicio(@RequestBody Ejercicio newEjercicio, @RequestParam Integer entrenador) {
        newEjercicio.setEntrenador(entrenador);
        return repositorioEjercicio.save(newEjercicio);
    }

    @PutMapping("/{id}")
    public Ejercicio updateEjercicio(@RequestBody Ejercicio newEjercicio, @PathVariable Long id) {
        return repositorioEjercicio.findById(id)
                .map(ejercicio -> {
                    ejercicio.setNombre(newEjercicio.getNombre());
                    ejercicio.setDescripcion(newEjercicio.getDescripcion());
                    return repositorioEjercicio.save(ejercicio);
                })
                .orElseGet(() -> {
                    newEjercicio.setId(id);
                    return repositorioEjercicio.save(newEjercicio);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteEjercicio(@PathVariable Long id) {
        repositorioEjercicio.deleteById(id);
    }
}
