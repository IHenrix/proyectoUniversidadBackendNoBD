package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.Nota;

public interface NotaRepository {
    Nota save(Nota n);
    Nota findByMatriculaAndCriterio(Long matriculaId, Long criterioId);
    void deleteByMatriculaAndCriterio(Long matriculaId, Long criterioId);
    Nota findById(Long id);
    void deleteById(Long id);
}
