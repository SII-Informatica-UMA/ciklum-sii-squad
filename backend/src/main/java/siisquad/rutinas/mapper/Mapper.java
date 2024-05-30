package siisquad.rutinas.mapper;

import siisquad.rutinas.dtos.*;
import siisquad.rutinas.entities.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Mapper {
    /**
     * Convierte una Rutina a un DTO RutinaDTO
     * @param rutina
     * @param rutinaUriBuilder
     * @param ejercicioUriBuilder
     * @return RutinaDTO
     */
    public static RutinaDTO toRutinaDTO(Rutina rutina, Function<Long, URI> rutinaUriBuilder,
                                        Function<Long, URI> ejercicioUriBuilder) {
        return RutinaDTO.builder()
                .id(rutina.getId())
                .nombre(rutina.getNombre())
                .descripcion(rutina.getDescripcion())
                .observaciones(rutina.getObservaciones())
                .ejercicios(rutina.getEjercicios().stream()
                        .map(i->toEjercicioEnRutinaDTO(i, ejercicioUriBuilder))
                        .toList())
                .links(Links.builder()
                        .self(rutinaUriBuilder.apply(rutina.getId()))
                        .build())
                .build();
    }

    /**
     * Convierte EjercicicioEnRutina a EjercicioEnRutinaDTO, conviertiendo su Ejercicio a EjercicioDTO
     * @param ejercicioEnRutina
     * @param ejercicioUriBuilder
     * @return
     */
    public static EjercicioEnRutinaDTO toEjercicioEnRutinaDTO (EjercicioEnRutina ejercicioEnRutina, Function<Long, URI> ejercicioUriBuilder){
        return EjercicioEnRutinaDTO.builder()
                .series(ejercicioEnRutina.getSeries())
                .repeticiones(ejercicioEnRutina.getRepeticiones())
                .duracionMinutos(ejercicioEnRutina.getDuracionMinutos())
                .ejercicio(toEjercicioDTO(ejercicioEnRutina.getEjercicio(), ejercicioUriBuilder))
                .build();
    }

    /**
     * Convierte EjercicicioEnRutina a EjercicioEnRutinaDTO, conviertiendo su Ejercicio a EjercicioDTO
     * @param ejercicioEnRutina
     * @return
     */
    public static EjercicioEnRutina toEjercicioEnRutina (EjercicioEnRutinaDTO ejercicioEnRutina, Integer idEntrenador){
        return EjercicioEnRutina.builder()
                .series(ejercicioEnRutina.getSeries())
                .repeticiones(ejercicioEnRutina.getRepeticiones())
                .duracionMinutos(ejercicioEnRutina.getDuracionMinutos())
                .ejercicio(toEjercicio(ejercicioEnRutina.getEjercicio(), idEntrenador))
                .build();
    }

    /**
     * Convierte una RutinaNuevaDTO a una Rutina
     * @param rutinaNuevaDTO
     * @return Rutina
     */
    public  static Rutina toRutina(RutinaNuevaDTO rutinaNuevaDTO, Integer idEntrenador) {
        List<EjercicioEnRutina> ejercicios = null;
        if (rutinaNuevaDTO.getEjercicios() != null)
            rutinaNuevaDTO.getEjercicios().stream().map(e -> Mapper.toEjercicioEnRutina(e,idEntrenador)).toList();
        else
            ejercicios = new ArrayList<>(5);
        return Rutina.builder()
                .nombre(rutinaNuevaDTO.getNombre())
                .descripcion(rutinaNuevaDTO.getDescripcion())
                .observaciones(rutinaNuevaDTO.getObservaciones())
                .ejercicios(ejercicios)
                .build();
    }

    /**
     * Convierte un Ejercicio a un EjercicioDTO
     * @param ejercicio
     * @param uriBuilder
     * @return EjercicioDTO
     */
    public static EjercicioDTO toEjercicioDTO(Ejercicio ejercicio, Function<Long, URI> uriBuilder) {
        return EjercicioDTO.builder()
                .id(ejercicio.getId())
                .nombre(ejercicio.getNombre())
                .descripcion(ejercicio.getDescripcion())
                .observaciones(ejercicio.getObservaciones())
                .tipo(ejercicio.getTipo())
                .musculosTrabajados(ejercicio.getMusculosTrabajados())
                .material(ejercicio.getMaterial())
                .dificultad(ejercicio.getDificultad())
                .multimedia(ejercicio.getMultimedia())
                .links(Links.builder()
                            .self(uriBuilder.apply(ejercicio.getId()))
                            .build())  //Basicamente crea un nuevo Links añadiendole la URI obtenido tras aplicar la funcion uriBuilder
                .build();
    }
    /**
     * Convierte un EjercicioNuevoDTO a un Ejercicio
     * @param ejercicioNuevoDTO
     * @return Ejercicio
     */
    public static Ejercicio toEjercicio(EjercicioNuevoDTO ejercicioNuevoDTO, Integer idEntrenador) {
        return Ejercicio.builder()
                .nombre(ejercicioNuevoDTO.getNombre())
                .descripcion(ejercicioNuevoDTO.getDescripcion())
                .observaciones(ejercicioNuevoDTO.getObservaciones())
                .tipo(ejercicioNuevoDTO.getTipo())
                .musculosTrabajados(ejercicioNuevoDTO.getMusculosTrabajados())
                .material(ejercicioNuevoDTO.getMaterial())
                .dificultad(ejercicioNuevoDTO.getDificultad())
                .multimedia(ejercicioNuevoDTO.getMultimedia())
                .entrenador(idEntrenador)
                .build();
    }
    public static Ejercicio toEjercicio(EjercicioDTO ejercicioNuevoDTO, Integer idEntrenador) {
        return Ejercicio.builder()
                .nombre(ejercicioNuevoDTO.getNombre())
                .descripcion(ejercicioNuevoDTO.getDescripcion())
                .observaciones(ejercicioNuevoDTO.getObservaciones())
                .tipo(ejercicioNuevoDTO.getTipo())
                .musculosTrabajados(ejercicioNuevoDTO.getMusculosTrabajados())
                .material(ejercicioNuevoDTO.getMaterial())
                .dificultad(ejercicioNuevoDTO.getDificultad())
                .multimedia(ejercicioNuevoDTO.getMultimedia())
                .entrenador(idEntrenador)
                .build();
    }

}
