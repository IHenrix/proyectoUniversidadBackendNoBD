package pe.edu.utp.uni.app.response;

public class NotasAlumnosResponse {
    public Long id;
    public String criterio;
    public Integer orden;
    public Integer porcentaje;
    public Double nota;
    public String notaAlumno;
    public Long notaId;

    public NotasAlumnosResponse() {}

    public NotasAlumnosResponse(Long id, String criterio, Integer orden, Integer porcentaje,
                                Double nota, String notaAlumno, Long notaId) {
        this.id = id;
        this.criterio = criterio;
        this.orden = orden;
        this.porcentaje = porcentaje;
        this.nota = nota;
        this.notaAlumno = notaAlumno;
        this.notaId = notaId;
    }
}
