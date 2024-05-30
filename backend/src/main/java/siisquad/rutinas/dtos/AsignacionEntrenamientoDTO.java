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
public class AsignacionEntrenamientoDTO {
    private Long idEntrenador;
    private Long idCliente;
    private String especialidad;
    private Long id;
    private List<PlanDTO> planes;

    public AsignacionEntrenamientoDTO(Long id, Long idEntrenador, Long idCliente) {
        this.idEntrenador = idEntrenador;
        this.idCliente = idCliente;
        this.especialidad = especialidad;
        this.id = id;
    }
}
