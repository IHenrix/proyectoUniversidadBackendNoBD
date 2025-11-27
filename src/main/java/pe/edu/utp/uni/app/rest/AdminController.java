package pe.edu.utp.uni.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.uni.app.model.Curso;
import pe.edu.utp.uni.app.model.Seccion;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.repository.CursoRepository;
import pe.edu.utp.uni.app.repository.SeccionRepository;
import pe.edu.utp.uni.app.repository.UsuarioRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final CursoRepository cursoRepository;
    private final SeccionRepository seccionRepository;
    private final UsuarioRepository usuarioRepository;

    // Cursos
    @GetMapping("/cursos")
    public List<Curso> listarCursos() { return cursoRepository.findAll(); }

    @PostMapping("/cursos")
    public Curso crearCurso(@RequestBody Curso c) { return cursoRepository.save(c); }

    @PutMapping("/cursos/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Long id, @RequestBody Curso c) {
        c.id = id;
        return ResponseEntity.ok(cursoRepository.save(c));
    }

    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<?> eliminarCurso(@PathVariable Long id) {
        cursoRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message","Curso eliminado"));
    }

    // Secciones
    @GetMapping("/secciones")
    public List<Seccion> listarSecciones() { return seccionRepository.findAll(); }

    @PostMapping("/secciones")
    public Seccion crearSeccion(@RequestBody Seccion s) { return seccionRepository.save(s); }

    @PutMapping("/secciones/{id}")
    public ResponseEntity<Seccion> actualizarSeccion(@PathVariable Long id, @RequestBody Seccion s) {
        s.id = id;
        return ResponseEntity.ok(seccionRepository.save(s));
    }

    @DeleteMapping("/secciones/{id}")
    public ResponseEntity<?> eliminarSeccion(@PathVariable Long id) {
        seccionRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message","Seccion eliminada"));
    }

    // Usuarios
    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() { return usuarioRepository.findAll(); }

    @PostMapping("/usuarios")
    public Usuario crearUsuario(@RequestBody Usuario u) { return usuarioRepository.guardarUsuario(u); }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario u) {
        u.id = id;
        return ResponseEntity.ok(usuarioRepository.guardarUsuario(u));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message","Usuario eliminado"));
    }
}
