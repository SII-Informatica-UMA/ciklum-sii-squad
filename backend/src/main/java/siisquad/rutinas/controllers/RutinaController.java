package siisquad.rutinas.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import siisquad.rutinas.dtos.EjercicioDTO;
import siisquad.rutinas.dtos.EjercicioNuevoDTO;
import siisquad.rutinas.dtos.RutinaDTO;
import siisquad.rutinas.dtos.RutinaNuevaDTO;
import siisquad.rutinas.entities.Ejercicio;
import siisquad.rutinas.entities.Rutina;
import siisquad.rutinas.excepciones.EntidadExistenteException;
import siisquad.rutinas.excepciones.EntidadNoEncontradaException;
import siisquad.rutinas.servicios.ServicioRutina;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rutina")
public class RutinaController {
    private final ServicioRutina servicio;

    public RutinaController(ServicioRutina servicio) {
        this.servicio = servicio;
    }

    /**
     * @param uriComponents URI incompleta
     * @return funcion que dado un id nos devuelve una URI que termina con ese id
     */
    public static Function<Long, URI> rutinaUriBuilder(UriComponents uriComponents) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().uriComponents(uriComponents);
        return id -> uriBuilder.path("/ejercicio")
                .path(String.format("/%d", id)) //anade path al builder
                .build()    //Crea UriComponents con los components previos contenidos en el builder
                .toUri();   //Crea URI
    }


    @GetMapping("/{id}")
    public RutinaDTO obtenerRutina(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        var rutina = servicio.getRutina(id);
        return Mapper.toRutinaDTO(rutina, rutinaUriBuilder(uriBuilder.build()), EjercicioController.ejercicioUriBuilder(uriBuilder.build()));
    }

    @PutMapping("/{id}")
    public void actualizarRutina(@PathVariable Long id, @RequestBody RutinaNuevaDTO rutinaNuevaDTO) {
        Rutina rutina = Mapper.toRutina(rutinaNuevaDTO);
        rutina.setId(id);
        servicio.updateRutina(rutina);
    }

    @DeleteMapping("/{id}")
    public void eliminarRutina(@PathVariable Long id) {
        servicio.deleteRutina(id);
    }


    @GetMapping
    public List<RutinaDTO> obtenerRutinas(@RequestParam("entrenador") Long id, UriComponentsBuilder uriBuilder){
        var rutina = servicio.getRutinasPorEntrenador(id);
        return rutina.stream().map(e -> Mapper.toRutinaDTO(e, rutinaUriBuilder(uriBuilder.build()), EjercicioController.ejercicioUriBuilder(uriBuilder.build()))).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> aniadirRutina(@RequestParam("entrenador") Long id, @RequestBody RutinaNuevaDTO rutinaNuevaDTO, UriComponentsBuilder uriBuilder){
        Rutina rutina = servicio.addRutina(Mapper.toRutina(rutinaNuevaDTO));
        return ResponseEntity.created(rutinaUriBuilder(uriBuilder.build()).apply(rutina.getId()))
                .build();
    }


    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {}

    @ExceptionHandler(EntidadExistenteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void existente() {}

}
