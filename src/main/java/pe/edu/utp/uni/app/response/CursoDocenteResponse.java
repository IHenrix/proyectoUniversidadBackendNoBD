package pe.edu.utp.uni.app.response;

public class CursoDocenteResponse {
    public Long id; // seccion id
    public String seccion_codigo;
    public Long curso_id;
    public String curso;
    public Double horas_semanales;
    public Double creditos;
    public String modalidad;
    public java.util.List<pe.edu.utp.uni.app.model.Horario> horarios;
    public Integer alumnos;

    public CursoDocenteResponse() {}

    public CursoDocenteResponse(Long id, String seccion_codigo, Long curso_id, String curso, Double horas_semanales, Double creditos,
                                String modalidad, java.util.List<pe.edu.utp.uni.app.model.Horario> horarios, Integer alumnos) {
        this.id = id; this.seccion_codigo = seccion_codigo; this.curso_id = curso_id;
        this.curso = curso; this.horas_semanales = horas_semanales; this.creditos = creditos; this.modalidad = modalidad; this.horarios = horarios; this.alumnos = alumnos;
    }
}
