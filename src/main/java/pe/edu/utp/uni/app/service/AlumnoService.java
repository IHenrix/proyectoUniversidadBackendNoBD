package pe.edu.utp.uni.app.service;

import pe.edu.utp.uni.app.response.CursoAlumnoResponse;

import java.util.List;

public interface AlumnoService {
    List<CursoAlumnoResponse> listarCursosPorUsuario(Long usuarioId);
}
