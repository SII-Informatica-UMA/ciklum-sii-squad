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
                "id=" + id +
                ", series=" + series +
                ", repeticiones=" + repeticiones +
                ", duracionMinutos=" + duracionMinutos +
                ", ejercicio=" + ejercicio +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EjercicioEnRutina that = (EjercicioEnRutina) o;
        return series == that.series &&
                repeticiones == that.repeticiones &&
                duracionMinutos == that.duracionMinutos &&
                Objects.equals(id, that.id) &&
                Objects.equals(ejercicio, that.ejercicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, series, repeticiones, duracionMinutos, ejercicio);
    }
}

