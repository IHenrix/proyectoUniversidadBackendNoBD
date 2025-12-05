package pe.edu.utp.uni.app.model;

import java.time.LocalDateTime;

public class Usuario {
    public Long id;
    public String username;
    public String passw;
    public String nombre;
    public String paterno;
    public String materno;
    public String sexo;
    public String correo;
    public String codigo;
    public Long rol_id;
    public Boolean activo;

    // Timestamps
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Usuario() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Usuario(Long id, String username, String passw, String nombre, String paterno, String materno,
                   String sexo, String correo, String codigo, Long rol_id, Boolean activo) {
        this.id = id;
        this.username = username;
        this.passw = passw;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.sexo = sexo;
        this.correo = correo;
        this.codigo = codigo;
        this.rol_id = rol_id;
        this.activo = activo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getNombreCompleto() {
        return nombre + " " + paterno + " " + materno;
    }
}
