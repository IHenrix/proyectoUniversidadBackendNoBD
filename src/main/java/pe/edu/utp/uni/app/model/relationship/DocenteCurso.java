package pe.edu.utp.uni.app.model.relationship;

public class DocenteCurso {
    public Long id;
    public Long usuario_id;
    public Long curso_id;
    public Long seccion_id;
    public Boolean activo;

    public DocenteCurso() {}
    public DocenteCurso(Long id, Long usuario_id, Long curso_id, Long seccion_id, Boolean activo) {
        this.id = id; this.usuario_id = usuario_id; this.curso_id = curso_id; this.seccion_id = seccion_id; this.activo = activo;
    }
}
