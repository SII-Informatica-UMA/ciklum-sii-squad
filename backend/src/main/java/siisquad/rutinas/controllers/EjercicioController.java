package siisquad.rutinas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import siisquad.rutinas.dtos.EjercicioDTO;
import siisquad.rutinas.dtos.EjercicioNuevoDTO;
import siisquad.rutinas.entities.Ejercicio;
import siisquad.rutinas.excepciones.EntidadExistenteException;
import siisquad.rutinas.excepciones.EntidadNoEncontradaException;
import siisquad.rutinas.servicios.ServicioEjercicio;

import java.net.URI;
import java.util.function.Function;


@RestController
@RequestMapping("/ejercicio")
public class EjercicioController {
    private final ServicioEjercicio servicio;

    public EjercicioController(ServicioEjercicio servicio) {
        this.servicio = servicio;
    }

    /**
     * @param uriComponents URI incompleta
     * @return funcion que dado un id nos devuelve una URI que termina con ese id
     */
    public static Function<Long, URI> ejercicioUriBuilder(UriComponents uriComponents) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().uriComponents(uriComponents);
        return id -> uriBuilder.path("/ejercicio")
                .path(String.format("/%d", id)) //anade path al builder
                .build()    //Crea UriComponents con los components previos contenidos en el builder
                .toUri();   //Crea URI
    }


    @GetMapping("{id}")
    public EjercicioDTO obtenerEjercicio(@PathVariable Long id, UriComponentsBuilder uriBuilder) {
        var ejercicio = servicio.obtenerEjercicio(id);
        return Mapper.toEjercicioDTO(ejercicio, ejercicioUriBuilder(uriBuilder.build()));
    }

    @PostMapping
    public ResponseEntity<?> aniadirEjercicio(@RequestBody EjercicioNuevoDTO ejercicioNuevoDTO, UriComponentsBuilder uriBuilder) {
        Long id = servicio.aniadirEjercicio(Mapper.toEjercicio(ejercicioNuevoDTO));
        return ResponseEntity.created(ejercicioUriBuilder(uriBuilder.build()).apply(id))
                .build();
    }

    @PutMapping("{id}")
    public void actualizarEjercicio(@PathVariable Long id, @RequestBody EjercicioNuevoDTO ejercicioNuevoDTO) {
        Ejercicio ejercicio = Mapper.toEjercicio(ejercicioNuevoDTO);
        ejercicio.setId(id);
        servicio.actualizarEjercicio(ejercicio);
    }

    @DeleteMapping("{id}")
    public void eliminarEjercicio(@PathVariable Long id) {
        servicio.eliminarEjercicio(id);
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {}

    @ExceptionHandler(EntidadExistenteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void existente() {}
}
