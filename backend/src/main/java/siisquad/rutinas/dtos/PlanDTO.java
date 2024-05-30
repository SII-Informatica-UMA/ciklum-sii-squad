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
public class PlanDTO {
    private String fechaInicio;
    private String fechaFin;
    private String reglaRecurrencia;
    private Long idRutina;
    private Long id;
}
