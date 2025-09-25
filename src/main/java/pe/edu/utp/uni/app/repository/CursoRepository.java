package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.Curso;

import java.util.List;

public interface CursoRepository {
    Curso save(Curso c);
    Curso findById(Long id);
    List<Curso> findAll();
}
