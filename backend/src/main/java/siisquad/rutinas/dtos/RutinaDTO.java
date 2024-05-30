package siisquad.rutinas.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
    private List<EjercicioEnRutinaDTO> ejercicios;
    @JsonProperty("_links")
    private Links links;
}
