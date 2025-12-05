package pe.edu.utp.uni.app.model;

import java.time.LocalDateTime;

public class Nota {
    public Long id;
    public Long matricula_id;  // ⭐ Cambio: ahora apunta a Matricula
    public Long criterio_id;
    public Double nota;
    public Double nota_alumno;

    // Timestamps
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Nota() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Nota(Long id, Long matricula_id, Long criterio_id, Double nota, Double nota_alumno) {
        this.id = id;
        this.matricula_id = matricula_id;
        this.criterio_id = criterio_id;
        this.nota = nota;
        this.nota_alumno = nota_alumno;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
