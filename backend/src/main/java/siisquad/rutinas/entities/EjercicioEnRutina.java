package siisquad.rutinas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class EjercicioEnRutina {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer series;
    private Integer repeticiones;
    private Integer duracionMinutos;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Ejercicio ejercicio;

    // toString
    @Override
    public String toString() {
        return "EjercicioEnRutina{" +
                ", series=" + series +
                ", repeticiones=" + repeticiones +
                ", duracionMinutos=" + duracionMinutos +
                ", ejercicio=" + ejercicio +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        return o instanceof EjercicioEnRutina e && e.getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

