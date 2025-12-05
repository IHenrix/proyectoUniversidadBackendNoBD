package pe.edu.utp.uni.app.model;

import java.time.LocalDateTime;

/**
 * Entidad Curso - Representa un curso académico
 * SRP: Solo maneja información del curso
 */
public class Curso {
    public Long id;
    public String nombre;
    public Double horas_semanales;
    public Double creditos;
    public String modalidad;

    // Timestamps
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Curso() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Curso(Long id, String nombre, Double horas_semanales, Double creditos, String modalidad) {
        this.id = id;
        this.nombre = nombre;
        this.horas_semanales = horas_semanales;
        this.creditos = creditos;
        this.modalidad = modalidad;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
