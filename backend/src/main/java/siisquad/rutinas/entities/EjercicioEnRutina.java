package siisquad.rutinas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Objects;

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
    private int series;
    private int repeticiones;
    private int duracionMinutos;

    @ManyToOne
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

