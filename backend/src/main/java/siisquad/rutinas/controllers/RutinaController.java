package siisquad.rutinas.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import siisquad.rutinas.mapper.Mapper;
import siisquad.rutinas.dtos.RutinaDTO;
import siisquad.rutinas.dtos.RutinaNuevaDTO;
import siisquad.rutinas.entities.Rutina;
import siisquad.rutinas.excepciones.EntidadExistenteException;
import siisquad.rutinas.excepciones.EntidadNoEncontradaException;
import siisquad.rutinas.security.JwtUtil;
import siisquad.rutinas.servicios.GestionEntrenamientoService;
import siisquad.rutinas.servicios.ServicioRutina;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rutina")
public class RutinaController {
    private final ServicioRutina servicio;

    private final GestionEntrenamientoService gestionEntrenamientoService;

    public RutinaController(ServicioRutina servicio, GestionEntrenamientoService gestionEntrenamientoService){
        this.servicio = servicio;
        this.gestionEntrenamientoService = gestionEntrenamientoService;
    }

    /**
     * @param uriComponents URI incompleta
     * @return funcion que dado un id nos devuelve una URI que termina con ese id
     */
    public static Function<Long, URI> rutinaUriBuilder(UriComponents uriComponents) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().uriComponents(uriComponents);
        return id -> uriBuilder.path("/rutina")
                .path(String.format("/%d", id)) //anade path al builder
                .build()    //Crea UriComponents con los components previos contenidos en el builder
                .toUri();   //Crea URI
    }


    @GetMapping("/{id}")
    public RutinaDTO obtenerRutina(@PathVariable Long id, UriComponentsBuilder uriBuilder, Authentication auth) {
        Integer idUsuario = JwtUtil.getIdFromToken(auth).intValue();
        var rutina = servicio.getRutina(id);
        if (!idUsuario.equals(rutina.getEntrenador()))
            if(gestionEntrenamientoService.comprubaEntrenaCliente(rutina.getEntrenador().longValue(),idUsuario.longValue()))
                throw new BadCredentialsException("No tienes permisos para ver las rutinas de otro entrenador");
        return Mapper.toRutinaDTO(rutina, rutinaUriBuilder(uriBuilder.build()), EjercicioController.ejercicioUriBuilder(uriBuilder.build()));
    }

    @PutMapping("/{id}")
    public void actualizarRutina(@PathVariable Long id, @RequestBody RutinaNuevaDTO rutinaNuevaDTO, Authentication auth) {
        Integer idEntrenador = JwtUtil.getIdFromToken(auth).intValue();
        if (!idEntrenador.equals(servicio.getRutina(id).getEntrenador()))
            throw new BadCredentialsException("No tienes permisos para modificar las rutinas de otro entrenador");
        Rutina rutina = Mapper.toRutina(rutinaNuevaDTO,idEntrenador);
        rutina.setId(id);
        servicio.updateRutina(rutina);
    }

    @DeleteMapping("/{id}")
    public void eliminarRutina(@PathVariable Long id, Authentication auth) {
        Integer idEntrenador = JwtUtil.getIdFromToken(auth).intValue();
        if (!idEntrenador.equals(servicio.getRutina(id).getEntrenador()))
            throw new BadCredentialsException("No tienes permisos para borrar las rutinas de otro entrenador");
        servicio.deleteRutina(id);
    }


    @GetMapping
    public List<RutinaDTO> obtenerRutinas(@RequestParam("entrenador") Long id, UriComponentsBuilder uriBuilder, Authentication auth){
        Long idEntrenador = JwtUtil.getIdFromToken(auth);
        if (!idEntrenador.equals(id))
            throw new BadCredentialsException("No tienes permisos para ver las rutinas de otro entrenador");
        var rutina = servicio.getRutinasPorEntrenador(id);
        return rutina.stream().map(e -> Mapper.toRutinaDTO(e, rutinaUriBuilder(uriBuilder.build()), EjercicioController.ejercicioUriBuilder(uriBuilder.build()))).toList();
    }

    @PostMapping
    public ResponseEntity<?> aniadirRutina(@RequestParam("entrenador") Long id, @RequestBody RutinaNuevaDTO rutinaNuevaDTO, UriComponentsBuilder uriBuilder, Authentication auth){
        Integer idEntreador = JwtUtil.getIdFromToken(auth).intValue();
        Long idRutina = servicio.addRutina(id, Mapper.toRutina(rutinaNuevaDTO,idEntreador));
        return ResponseEntity.created(rutinaUriBuilder(uriBuilder.build()).apply(idRutina))
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
