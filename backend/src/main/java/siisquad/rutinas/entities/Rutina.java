package siisquad.rutinas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;
import java.util.Objects;


@Entity
public class Rutina {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre;
    private String descripcion;
    private String observaciones;
    private Integer entrenador;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EjercicioEnRutina> ejercicios;

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Integer getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Integer entrenador) {
        this.entrenador = entrenador;
    }

    public List<EjercicioEnRutina> getEjercicios() {
        return ejercicios;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setEjercicios(List<EjercicioEnRutina> ejercicios) {
        this.ejercicios = ejercicios;
    }

    // toString
    @Override
    public String toString() {
        return "Rutina{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", ejercicios=" + ejercicios +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        return o instanceof Rutina r && r.getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

