package pe.edu.utp.uni.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.repository.UsuarioRepository;
import pe.edu.utp.uni.app.service.UsuarioService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository repository;
    @Override
    public Usuario login(String username, String pass) {
        Usuario u = repository.buscarPorUsuario(username);
        if (u == null) return null;
        if (Objects.equals(u.passw, pass) && Boolean.TRUE.equals(u.activo)) return u;
        return null;
    }
}
