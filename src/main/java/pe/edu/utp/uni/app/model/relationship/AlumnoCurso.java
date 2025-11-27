package pe.edu.utp.uni.app.model.relationship;

public class AlumnoCurso {
    public Long id;
    public Long usuario_id;
    public Long curso_id;
    public Long seccion_id;
    public String estado;
    public Double nota_final;
    public Double nota_alumno_real;
    public Double nota_alumno_final;
    public Boolean activo;

    public AlumnoCurso() {}
    public AlumnoCurso(Long id, Long usuario_id, Long curso_id, Long seccion_id, String estado, Double nota_final, Double nota_alumno_real, Double nota_alumno_final, Boolean activo) {
        this.id = id; this.usuario_id = usuario_id; this.curso_id = curso_id; this.seccion_id = seccion_id; this.estado = estado; this.nota_final = nota_final; this.nota_alumno_real = nota_alumno_real; this.nota_alumno_final = nota_alumno_final; this.activo = activo;
    }
}
