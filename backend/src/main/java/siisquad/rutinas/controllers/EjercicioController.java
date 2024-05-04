package siisquad.rutinas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import siisquad.rutinas.servicios.ServicioEjercicio;


@RestController
@RequestMapping("/ejercicio")
public class EjercicioController {
    private final ServicioEjercicio servicio;

    public EjercicioController(ServicioEjercicio servicio) {
        this.servicio = servicio;
    }
}
