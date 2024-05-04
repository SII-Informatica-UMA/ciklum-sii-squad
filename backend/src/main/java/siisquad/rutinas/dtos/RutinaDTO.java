package siisquad.rutinas.dtos;

import lombok.*;
import siisquad.rutinas.entities.EjercicioEnRutina;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class RutinaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String observaciones;
    private List<EjercicioEnRutina> ejercicios;
}
