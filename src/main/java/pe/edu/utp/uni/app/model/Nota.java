package pe.edu.utp.uni.app.model;

public class Nota {
    public Long id;
    public Long alumno_curso_id;
    public Long criterio_id;
    public Double nota;
    public Double nota_alumno;

    public Nota() {}

    public Nota(Long id, Long alumno_curso_id, Long criterio_id, Double nota, Double nota_alumno) {
        this.id = id;
        this.alumno_curso_id = alumno_curso_id;
        this.criterio_id = criterio_id;
        this.nota = nota;
        this.nota_alumno = nota_alumno;
    }
}
