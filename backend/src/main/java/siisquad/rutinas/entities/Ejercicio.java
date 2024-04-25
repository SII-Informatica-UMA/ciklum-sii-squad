package siisquad.rutinas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import java.util.List;
import java.util.Objects;


@Entity
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nombre; 
    private String descripcion;
    private String observaciones;
    private String tipo;
    private String musculosTrabajados;
    private String material;
    private String dificultad;
    
    @ElementCollection
    @CollectionTable(name = "multimedia")
    @Column(name = "url")
    private List<String> multimedia;

    private Integer entrenador; 

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

    public String getTipo() {
        return tipo;
    }

    public String getMusculosTrabajados() {
        return musculosTrabajados;
    }

    public String getMaterial() {
        return material;
    }

    public String getDificultad() {
        return dificultad;
    }

    public List<String> getMultimedia() {
        return multimedia;
    }

    public Integer getEntrenador() {
        return entrenador;
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

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setMusculosTrabajados(String musculosTrabajados) {
        this.musculosTrabajados = musculosTrabajados;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public void setMultimedia(List<String> multimedia) {
        this.multimedia = multimedia;
    }

    public void setEntrenador(Integer entrenador) {
        this.entrenador = entrenador;
    }

    // toString
    @Override
    public String toString() {
        return "Ejercicio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", tipo='" + tipo + '\'' +
                ", musculosTrabajados='" + musculosTrabajados + '\'' +
                ", material='" + material + '\'' +
                ", dificultad='" + dificultad + '\'' +
                ", multimedia=" + multimedia +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        return o instanceof Ejercicio e && e.getId().equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

