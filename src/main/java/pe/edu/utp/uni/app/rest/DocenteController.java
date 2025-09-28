package pe.edu.utp.uni.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.uni.app.request.RegistrarEditarNotasRequest;
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

    @PostMapping("/registrar-editar-notas")
    public ResponseEntity<?> registrarEditarNotas(@RequestBody RegistrarEditarNotasRequest req) {
        docenteService.registrarOEditarNotas(req.alumnoCursoId, req.cursoId, req.notas);
        return ResponseEntity.ok(java.util.Map.of("message","Notas registradas o editadas con éxito"));
    }
    @DeleteMapping("/nota/{notaId}/alumno/{alumnoCursoId}")
    public ResponseEntity<?> eliminarNota(@PathVariable Long notaId,
                                          @PathVariable Long alumnoCursoId) {
        int filas = docenteService.eliminarNota(notaId, alumnoCursoId);
        if (filas > 0) return ResponseEntity.ok(java.util.Map.of("message","Nota eliminada con éxito"));
        return ResponseEntity.status(404).body(java.util.Map.of("message","Nota no encontrada"));
    }
}
