package siisquad.rutinas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import siisquad.rutinas.repositories.RepositorioRutina;
import siisquad.rutinas.repositories.RepositorioEjercicio;
import siisquad.rutinas.repositories.RepositorioEjercicioEnRutina;

@Component
public class LineaComandos implements CommandLineRunner {
    private RepositorioRutina repositorioRutina;
    private RepositorioEjercicio repositorioEjercicio;
    private RepositorioEjercicioEnRutina repositorioEjercicioEnRutina;

    public LineaComandos(RepositorioRutina repositorioRutina, RepositorioEjercicio repositorioEjercicio, RepositorioEjercicioEnRutina repositorioEjercicioEnRutina) {
        this.repositorioRutina = repositorioRutina;
        this.repositorioEjercicio = repositorioEjercicio;
        this.repositorioEjercicioEnRutina = repositorioEjercicioEnRutina;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        for (String s: args) {
            System.out.println(s);
        }

        // Ejemplo de uso de los repositorios
        // Ejemplo de uso de los repositorios
        if (args.length > 0) {
            // Buscar rutinas por nombre
            Long id = Long.parseLong(args[0]);
            repositorioRutina.findById(id).ifPresent(r -> System.out.println(r));
        }

        // Ejemplo de uso de los repositorios
        if (args.length > 0) {
            // Buscar ejercicios por nombre
            Long id = Long.parseLong(args[0]);
            repositorioEjercicio.findById(id).ifPresent(e -> System.out.println(e));
        }
        
    }
}