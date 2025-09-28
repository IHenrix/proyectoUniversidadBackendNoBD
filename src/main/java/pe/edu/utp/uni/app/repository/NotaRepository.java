package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.Nota;

public interface NotaRepository {
    Nota save(Nota n);
    Nota findByAlumnoCursoAndCriterio(Long alumnoCursoId, Long criterioId);
    void deleteByAlumnoCursoAndCriterio(Long alumnoCursoId, Long criterioId);
    Nota findById(Long id);
    void deleteById(Long id);
}
