package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.relationship.AlumnoCurso;

import java.util.List;

public interface AlumnoCursoRepository {
    AlumnoCurso save(AlumnoCurso ac);
    List<AlumnoCurso> listByUsuarioId(Long usuarioId);
    List<AlumnoCurso> listByCursoId(Long cursoId);
}
