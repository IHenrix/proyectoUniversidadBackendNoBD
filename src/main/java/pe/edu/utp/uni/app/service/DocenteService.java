package pe.edu.utp.uni.app.service;

import pe.edu.utp.uni.app.response.AlumnoListaCursoResponse;
import pe.edu.utp.uni.app.response.CursoDocenteResponse;
import pe.edu.utp.uni.app.request.NotaItem;
import java.util.List;

public interface DocenteService {
    List<CursoDocenteResponse> listarCursosDeDocente(Long docenteId);
    List<AlumnoListaCursoResponse> listarAlumnosPorCurso(Long cursoId);
    void registrarOEditarNotas(Long alumnoCursoId, Long cursoId, List<NotaItem> notas);
}
