package siisquad.rutinas.dtos;

import lombok.*;
import siisquad.rutinas.entities.Ejercicio;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class EjercicioEnRutinaDTO {
    private int series;
    private int repeticiones;
    private int duracionMinutos;
    private EjercicioDTO ejercicio;
}
