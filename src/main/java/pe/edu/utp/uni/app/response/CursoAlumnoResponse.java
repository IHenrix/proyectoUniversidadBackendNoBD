package pe.edu.utp.uni.app.response;

public class CursoAlumnoResponse {
    public Long curso_id;
    public Long seccion_id;
    public String seccion_codigo;
    public String curso;
    public Double horas_semanales;
    public Double creditos;
    public String modalidad;
    public java.util.List<pe.edu.utp.uni.app.model.Horario> horarios;
    public String alumno;
    public String docente;
    public Long alumno_curso_id;
    public Long usuario_id;
    public Double notaFinal;
    public String notaAlumnoFinal;
    public String estado;

    public CursoAlumnoResponse() {}
    public CursoAlumnoResponse(Long curso_id, Long seccion_id, String seccion_codigo, String curso, Double horas_semanales, Double creditos, String modalidad,
                               java.util.List<pe.edu.utp.uni.app.model.Horario> horarios,
                               String alumno, String docente, Long alumno_curso_id, Long usuario_id,
                               Double notaFinal, String notaAlumnoFinal, String estado) {
        this.curso_id = curso_id; this.seccion_id = seccion_id; this.seccion_codigo = seccion_codigo; this.curso = curso; this.horas_semanales = horas_semanales; this.creditos = creditos; this.modalidad = modalidad; this.horarios = horarios;
        this.alumno = alumno; this.docente = docente; this.alumno_curso_id = alumno_curso_id; this.usuario_id = usuario_id;
        this.notaFinal = notaFinal; this.notaAlumnoFinal = notaAlumnoFinal; this.estado = estado;
    }
}
