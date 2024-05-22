package siisquad.rutinas.dtos;

import lombok.*;

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
