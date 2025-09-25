package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.Usuario;

public interface UsuarioRepository {
    Usuario findByUsername(String username);
    Usuario save(Usuario u);
}
