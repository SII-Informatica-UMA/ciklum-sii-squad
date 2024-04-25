package siisquad.rutinas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.Objects;


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

    // Getters
    public Long getId() {
        return id;
    }

    public int getSeries() {
        return series;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

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

