package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.relationship.DocenteCurso;

import java.util.List;

public interface DocenteCursoRepository {
    DocenteCurso save(DocenteCurso dc);
    List<DocenteCurso> listByCursoId(Long cursoId);
    List<DocenteCurso> listByDocenteId(Long docenteId);
}
