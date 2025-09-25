package pe.edu.utp.uni.app.repository.impl;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import pe.edu.utp.uni.app.model.Rol;
import pe.edu.utp.uni.app.repository.RolRepository;

@Repository
public class RolRepositoryImpl implements RolRepository {
    private final Map<Long, Rol> store = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);
    @Override
    public Rol save(Rol rol) {
        if (rol.id == null) rol.id = seq.incrementAndGet();
        store.put(rol.id, rol);
        return rol;
    }
    @Override
    public Rol findById(Long id) { return store.get(id); }
}
