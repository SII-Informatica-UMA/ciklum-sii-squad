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
    private Integer series;
    private Integer repeticiones;
    private Integer duracionMinutos;
    private EjercicioDTO ejercicio;
}
