package pe.edu.utp.uni.app.service;

import pe.edu.utp.uni.app.response.CursoAlumnoResponse;
import pe.edu.utp.uni.app.response.NotasAlumnosResponse;

import java.util.List;

public interface AlumnoService {
    List<CursoAlumnoResponse> listarSeccionesPorUsuario(Long usuarioId);
    List<NotasAlumnosResponse> listarNotasAlumnos(Long seccionId, Long alumnoSeccionId);
    CursoAlumnoResponse matricularEnSeccion(Long usuarioId, Long seccionId);
    List<CursoAlumnoResponse> listarSeccionesDisponibles(Long usuarioId);
}
