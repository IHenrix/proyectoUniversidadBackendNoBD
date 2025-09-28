package pe.edu.utp.uni.app.response;

public class AlumnoListaCursoResponse {
    public Long id;
    public String nombre;
    public String paterno;
    public String materno;
    public String codigo;
    public Double notaFinal;
    public String notaAlumnoFinal;
    public String estado;
    public Double notaAlumnoReal;

    public AlumnoListaCursoResponse() {}

    public AlumnoListaCursoResponse(Long id, String nombre, String paterno, String materno, String codigo,
                                    Double notaFinal, String notaAlumnoFinal, String estado, Double notaAlumnoReal) {
        this.id = id; this.nombre = nombre; this.paterno = paterno; this.materno = materno; this.codigo = codigo;
        this.notaFinal = notaFinal; this.notaAlumnoFinal = notaAlumnoFinal; this.estado = estado; this.notaAlumnoReal = notaAlumnoReal;
    }
}
