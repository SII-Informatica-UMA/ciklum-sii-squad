package siisquad.rutinas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import siisquad.rutinas.dtos.AsignacionEntrenamientoDTO;
import siisquad.rutinas.mapper.Mapper;
import siisquad.rutinas.dtos.EjercicioDTO;
import siisquad.rutinas.dtos.EjercicioNuevoDTO;
import siisquad.rutinas.entities.Ejercicio;
import siisquad.rutinas.excepciones.EntidadExistenteException;
import siisquad.rutinas.excepciones.EntidadNoEncontradaException;
import siisquad.rutinas.security.JwtUtil;
import siisquad.rutinas.servicios.ServicioEjercicio;
import siisquad.rutinas.servicios.ServicioEntrena;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ejercicio")
public class EjercicioController {
    private final ServicioEjercicio servicio;
    private final ServicioEntrena servicioEntrena;

    public EjercicioController(ServicioEjercicio servicio, ServicioEntrena servicioEntrena) {
        this.servicio = servicio;
        this.servicioEntrena = servicioEntrena;
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
    public List <Long> listaEntrenadoresDeCliente (List<AsignacionEntrenamientoDTO> l)
    {
        List<Long> idsEntrenadores = new ArrayList<>(); //Lista de los ids de los entrenadores del cliente
        for(AsignacionEntrenamientoDTO asig: l){
            idsEntrenadores.add(asig.getIdEntrenador());
        }
        return idsEntrenadores;
    }


    @GetMapping("/{id}")
    public EjercicioDTO obtenerEjercicio(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader, UriComponentsBuilder uriBuilder, Authentication auth) {
        var ejercicio = servicio.getEjercicio(id);
        Long idEoC = JwtUtil.getIdFromToken(auth); //Id del entrenador o del cliente
        Long idEntrenadorCreador = ejercicio.getEntrenador().longValue();
        if (!idEoC.equals(idEntrenadorCreador) && servicioEntrena.getEntrenaPorCliente(idEoC, authorizationHeader).isPresent() && !listaEntrenadoresDeCliente(servicioEntrena.getEntrenaPorCliente(idEoC, authorizationHeader).get()).contains(idEntrenadorCreador))
            throw new BadCredentialsException("No tienes permisos para ver los ejercicios de otro entrenador");
        return Mapper.toEjercicioDTO(ejercicio, ejercicioUriBuilder(uriBuilder.build()));
    }

    @PutMapping("/{id}")
    public void actualizarEjercicio(@PathVariable Long id, @RequestBody EjercicioNuevoDTO ejercicioNuevoDTO, Authentication auth) {
        Long idEntreador = JwtUtil.getIdFromToken(auth);
        if (!idEntreador.equals(servicio.getEjercicio(id).getEntrenador().longValue()))
            throw new BadCredentialsException("No tienes permisos para actualizar los ejercicios de otro entrenador");
        Ejercicio ejercicio = Mapper.toEjercicio(ejercicioNuevoDTO, idEntreador.intValue());
        ejercicio.setId(id);
        servicio.updateEjercicio(ejercicio);
    }

    @DeleteMapping("/{id}")
    public void eliminarEjercicio(@PathVariable Long id, Authentication auth) {
        Long idEntreador = JwtUtil.getIdFromToken(auth);
        if (!idEntreador.equals(servicio.getEjercicio(id).getEntrenador().longValue()))
            throw new BadCredentialsException("No tienes permisos para borrar los ejercicios de otro entrenador");
        servicio.deleteEjercicio(id);
    }


    @GetMapping
    public List<EjercicioDTO> obtenerEjercicios(@RequestParam("entrenador") Long id, UriComponentsBuilder uriBuilder, Authentication auth){
        Long idEntreador = JwtUtil.getIdFromToken(auth);
        if (!idEntreador.equals(id))
            throw new BadCredentialsException("No tienes permisos para ver los ejercicios de otro entrenador");
            
        var ejercicios = servicio.getEjerciciosPorEntrenador(id);
        return ejercicios.stream().map(e -> Mapper.toEjercicioDTO(e, ejercicioUriBuilder(uriBuilder.build()))).toList();
    }

    @PostMapping
    public ResponseEntity<?> aniadirEjercicio(@RequestParam("entrenador") Long id, @RequestBody EjercicioNuevoDTO ejercicioNuevoDTO, UriComponentsBuilder uriBuilder, Authentication auth){
        Long idEoC = JwtUtil.getIdFromToken(auth);
        if (!idEoC.equals(id))
            throw new BadCredentialsException("No tienes permisos para crear ejercicios de otros entrenadores");
        Long idEjercicio = servicio.addEjercicio(id, Mapper.toEjercicio(ejercicioNuevoDTO,idEoC.intValue()));
        return ResponseEntity.created(ejercicioUriBuilder(uriBuilder.build()).apply(idEjercicio))
                .build();
    }


    @ExceptionHandler(EntidadNoEncontradaException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void noEncontrado() {}

    @ExceptionHandler(EntidadExistenteException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public void existente() {}
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public void errorAutenticacion() {}
}
