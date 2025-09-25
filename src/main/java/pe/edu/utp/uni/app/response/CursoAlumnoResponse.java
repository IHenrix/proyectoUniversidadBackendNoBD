package pe.edu.utp.uni.app.response;

public class CursoAlumnoResponse {
    public Long curso_id;
    public String curso;
    public Double horas_semanales;
    public Double creditos;
    public String modalidad;
    public String alumno;
    public String docente;
    public Long alumno_curso_id;
    public Long usuario_id;
    public Double notaFinal;
    public String notaAlumnoFinal;
    public String estado;

    public CursoAlumnoResponse() {}
    public CursoAlumnoResponse(Long curso_id, String curso, Double horas_semanales, Double creditos, String modalidad,
                               String alumno, String docente, Long alumno_curso_id, Long usuario_id,
                               Double notaFinal, String notaAlumnoFinal, String estado) {
        this.curso_id = curso_id; this.curso = curso; this.horas_semanales = horas_semanales; this.creditos = creditos; this.modalidad = modalidad;
        this.alumno = alumno; this.docente = docente; this.alumno_curso_id = alumno_curso_id; this.usuario_id = usuario_id;
        this.notaFinal = notaFinal; this.notaAlumnoFinal = notaAlumnoFinal; this.estado = estado;
    }
}
