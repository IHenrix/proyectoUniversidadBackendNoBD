package pe.edu.utp.uni.app.response;

public class CursoDocenteResponse {
    public Long id;
    public String curso;
    public Double horas_semanales;
    public Double creditos;
    public String modalidad;
    public Integer alumnos;

    public CursoDocenteResponse() {}

    public CursoDocenteResponse(Long id, String curso, Double horas_semanales, Double creditos, String modalidad, Integer alumnos) {
        this.id = id;
        this.curso = curso;
        this.horas_semanales = horas_semanales;
        this.creditos = creditos;
        this.modalidad = modalidad;
        this.alumnos = alumnos;
    }
}
