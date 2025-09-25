package pe.edu.utp.uni.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.request.LoginRequest;
import pe.edu.utp.uni.app.service.UsuarioService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) LoginRequest req) {
        if (req == null || req.usuario == null || req.usuario.isBlank() || req.pass == null || req.pass.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Usuario u = usuarioService.login(req.usuario.toUpperCase(), req.pass);
        if (u == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(u);
    }
}
