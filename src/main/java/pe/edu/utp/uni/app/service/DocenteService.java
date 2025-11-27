package pe.edu.utp.uni.app.service;

import pe.edu.utp.uni.app.response.AlumnoListaCursoResponse;
import pe.edu.utp.uni.app.response.CursoDocenteResponse;
import pe.edu.utp.uni.app.request.NotaItem;
import java.util.List;

public interface DocenteService {
    List<CursoDocenteResponse> listarSeccionesDeDocente(Long docenteId);
    List<AlumnoListaCursoResponse> listarAlumnosPorSeccion(Long seccionId);
    void registrarOEditarNotas(Long alumnoCursoId, Long seccionId, List<NotaItem> notas);
    int eliminarNota(Long notaId, Long alumnoCursoId, Long seccionId);
}
