package pe.edu.utp.uni.app.repository;

import pe.edu.utp.uni.app.model.Rol;

public interface RolRepository {
    Rol save(Rol rol);
    Rol findById(Long id);
}
