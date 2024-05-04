package siisquad.rutinas.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import siisquad.rutinas.dtos.RutinaDTO;
import siisquad.rutinas.entities.Rutina;
import siisquad.rutinas.excepciones.RutinaNoEncontrada;
import siisquad.rutinas.servicios.ServicioRutina;

import java.util.List;

@RestController
@RequestMapping("/rutina")
public class RutinaController {
    private final ServicioRutina servicio;

    public RutinaController(ServicioRutina servicio) {
        this.servicio = servicio;
    }
    @GetMapping
    public ResponseEntity<List<RutinaDTO>> getRutina() {
        List<RutinaDTO> resultado = servicio.getRutinasEntrenador(Long.valueOf(1)).stream()
                .map(Mapper::toRutinaDTO).toList();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaDTO> getRutina(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Mapper.toRutinaDTO(servicio.getRutina(id)));
        }catch (RutinaNoEncontrada e) {
            return ResponseEntity.notFound().build();
        }
    }

}
