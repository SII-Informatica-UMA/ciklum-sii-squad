package siisquad.rutinas.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import siisquad.rutinas.dtos.RutinaDTO;
import siisquad.rutinas.entities.Rutina;
import siisquad.rutinas.excepciones.EntidadExistenteException;
import siisquad.rutinas.excepciones.EntidadNoEncontradaException;
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

        return ResponseEntity.ok(Mapper.toRutinaDTO(servicio.getRutina(id)));

    }



    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {}

    @ExceptionHandler(EntidadExistenteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void existente() {}

}
