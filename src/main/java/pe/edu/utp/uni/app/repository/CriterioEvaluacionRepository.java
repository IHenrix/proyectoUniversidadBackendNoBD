package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.CriterioEvaluacion;

import java.util.List;

public interface CriterioEvaluacionRepository {
    CriterioEvaluacion save(CriterioEvaluacion ce);
    List<CriterioEvaluacion> listByCursoId(Long cursoId);
}
