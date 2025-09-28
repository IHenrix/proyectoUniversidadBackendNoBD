package pe.edu.utp.uni.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.uni.app.response.AlumnoListaCursoResponse;
import pe.edu.utp.uni.app.response.CursoDocenteResponse;
import pe.edu.utp.uni.app.service.DocenteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docente")
public class DocenteController {
    private final DocenteService docenteService;

    @GetMapping("/cursos")
    public ResponseEntity<?> listarCursosDocente(@RequestParam("docenteId") Long docenteId) {
        List<CursoDocenteResponse> cursos = docenteService.listarCursosDeDocente(docenteId);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/cursos/alumnos")
    public ResponseEntity<?> listarAlumnosPorCursos(@RequestParam("cursoId") Long cursoId) {
        List<AlumnoListaCursoResponse> usuarios = docenteService.listarAlumnosPorCurso(cursoId);
        return ResponseEntity.ok(usuarios);
    }
}
