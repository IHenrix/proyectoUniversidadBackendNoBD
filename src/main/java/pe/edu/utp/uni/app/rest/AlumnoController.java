package pe.edu.utp.uni.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.uni.app.response.CursoAlumnoResponse;
import pe.edu.utp.uni.app.response.NotasAlumnosResponse;
import pe.edu.utp.uni.app.service.AlumnoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alumno")
public class AlumnoController {
    private final AlumnoService alumnoService;

    @GetMapping("/cursos")
    public ResponseEntity<?> listarCursos(@RequestParam(name = "usuarioId", required = false) Long usuarioId) {
        if (usuarioId == null) return ResponseEntity.ok(java.util.List.of());
        List<CursoAlumnoResponse> cursos = alumnoService.listarCursosPorUsuario(usuarioId);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/notas")
    public ResponseEntity<?> listarNotasAlumno(@RequestParam("cursoId") Long cursoId,
                                               @RequestParam("alumnoCursoId") Long alumnoCursoId) {
        List<NotasAlumnosResponse> notas = alumnoService.listarNotasAlumnos(cursoId, alumnoCursoId);
        return ResponseEntity.ok(notas);
    }
}
