package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.Seccion;
import java.util.List;

public interface SeccionRepository {
    Seccion save(Seccion s);
    Seccion findById(Long id);
    List<Seccion> findAll();
    List<Seccion> listByCursoId(Long cursoId);
    List<Seccion> listByDocenteId(Long docenteId);
}
