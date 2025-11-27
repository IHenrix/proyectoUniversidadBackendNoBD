package pe.edu.utp.uni.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.uni.app.response.CursoAlumnoResponse;
import pe.edu.utp.uni.app.response.NotasAlumnosResponse;
import pe.edu.utp.uni.app.service.AlumnoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alumno")
public class AlumnoController {
    private final AlumnoService alumnoService;

    @GetMapping("/secciones")
    public ResponseEntity<?> listarSecciones(@RequestParam(name = "usuarioId", required = false) Long usuarioId) {
        if (usuarioId == null) return ResponseEntity.ok(java.util.List.of());
        List<CursoAlumnoResponse> cursos = alumnoService.listarSeccionesPorUsuario(usuarioId);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/notas")
    public ResponseEntity<?> listarNotasAlumno(@RequestParam("seccionId") Long seccionId,
                                               @RequestParam("alumnoCursoId") Long alumnoCursoId) {
        List<NotasAlumnosResponse> notas = alumnoService.listarNotasAlumnos(seccionId, alumnoCursoId);
        return ResponseEntity.ok(notas);
    }

    @GetMapping("/secciones/disponibles")
    public ResponseEntity<?> listarSeccionesDisponibles(@RequestParam("usuarioId") Long usuarioId) {
        if (usuarioId == null) return ResponseEntity.ok(java.util.List.of());
        return ResponseEntity.ok(alumnoService.listarSeccionesDisponibles(usuarioId));
    }

    @PostMapping("/matricular")
    public ResponseEntity<?> matricular(@RequestParam("usuarioId") Long usuarioId,
                                        @RequestParam("seccionId") Long seccionId) {
        var resp = alumnoService.matricularEnSeccion(usuarioId, seccionId);
        if (resp == null) return ResponseEntity.badRequest().body(java.util.Map.of("message","No se pudo matricular"));
        return ResponseEntity.ok(resp);
    }
}
