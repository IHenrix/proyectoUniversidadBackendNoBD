package pe.edu.utp.uni.app.service;

import pe.edu.utp.uni.app.model.Usuario;

public interface UsuarioService {
    Usuario login(String username, String pass);
}
