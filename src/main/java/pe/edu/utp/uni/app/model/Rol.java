package pe.edu.utp.uni.app.model;

import java.time.LocalDateTime;

public class Rol {
    public Long id;
    public String nombre;

    // Timestamps
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Rol() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Rol(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
