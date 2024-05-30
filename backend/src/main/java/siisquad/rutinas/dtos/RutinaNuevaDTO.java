package siisquad.rutinas.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class RutinaNuevaDTO {
    private String nombre;
    private String descripcion;
    private String observaciones;
    private List<EjercicioEnRutinaDTO> ejercicios;
}
