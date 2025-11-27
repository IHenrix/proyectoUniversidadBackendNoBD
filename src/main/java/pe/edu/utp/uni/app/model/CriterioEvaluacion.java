package pe.edu.utp.uni.app.model;

public class CriterioEvaluacion {
    public Long id;
    public Long curso_id;
    public Long seccion_id;
    public String nombre_criterio;
    public Integer orden;
    public Double porcentaje;

    public CriterioEvaluacion() {}
    public CriterioEvaluacion(Long id, Long curso_id, Long seccion_id, String nombre_criterio, Integer orden, Double porcentaje) {
        this.id = id; this.curso_id = curso_id; this.seccion_id = seccion_id; this.nombre_criterio = nombre_criterio; this.orden = orden; this.porcentaje = porcentaje;
    }
}
