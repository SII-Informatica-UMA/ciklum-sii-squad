package siisquad.rutinas.controllers;

import siisquad.rutinas.dtos.*;
import siisquad.rutinas.entities.*;

public class Mapper {
    /**
     * Convierte una Rutina a un DTO RutinaDTO
     * @param rutina
     * @return RutinaDTO
     */
    public static RutinaDTO toRutinaDTO(Rutina rutina) {
        return RutinaDTO.builder()
                .id(rutina.getId())
                .nombre(rutina.getNombre())
                .descripcion(rutina.getDescripcion())
                .observaciones(rutina.getObservaciones())
                .entrenador(rutina.getEntrenador())
                .ejercicios(rutina.getEjercicios())
                .build();
    }
    /**
     * Convierte una RutinaNuevaDTO a una Rutina
     * @param rutinaNuevaDTO
     * @return Rutina
     */
    public  static Rutina toRutina(RutinaNuevaDTO rutinaNuevaDTO) {
        return Rutina.builder()
                .nombre(rutinaNuevaDTO.getNombre())
                .descripcion(rutinaNuevaDTO.getDescripcion())
                .observaciones(rutinaNuevaDTO.getObservaciones())
                .entrenador(rutinaNuevaDTO.getEntrenador())
                .ejercicios(rutinaNuevaDTO.getEjercicios())
                .build();
    }

    /**
     * Convierte un Ejercicio a un EjercicioDTO
     * @param ejercicio
     * @return EjercicioDTO
     */
    public static EjercicioDTO toEjercioDTO(Ejercicio ejercicio) {
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
                .entrenador(ejercicio.getEntrenador())
                .build();
    }
    /**
     * Convierte un EjercicioNuevoDTO a un Ejercicio
     * @param ejercicioNuevoDTO
     * @return Ejercicio
     */
    public static Ejercicio toEjercicio(EjercicioNuevoDTO ejercicioNuevoDTO) {
        return Ejercicio.builder()
                .nombre(ejercicioNuevoDTO.getNombre())
                .descripcion(ejercicioNuevoDTO.getDescripcion())
                .observaciones(ejercicioNuevoDTO.getObservaciones())
                .tipo(ejercicioNuevoDTO.getTipo())
                .musculosTrabajados(ejercicioNuevoDTO.getMusculosTrabajados())
                .material(ejercicioNuevoDTO.getMaterial())
                .dificultad(ejercicioNuevoDTO.getDificultad())
                .multimedia(ejercicioNuevoDTO.getMultimedia())
                .entrenador(ejercicioNuevoDTO.getEntrenador())
                .build();
    }
}
