package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.Usuario;

public interface UsuarioRepository {
    Usuario buscarPorUsuario(String username);
    Usuario guardarUsuario(Usuario u);
    Usuario findById(Long id);
}
