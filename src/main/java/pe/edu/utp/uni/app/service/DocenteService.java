package pe.edu.utp.uni.app.service;

import pe.edu.utp.uni.app.response.CursoDocenteResponse;

import java.util.List;

public interface DocenteService {
    List<CursoDocenteResponse> listarCursosDeDocente(Long docenteId);
}
