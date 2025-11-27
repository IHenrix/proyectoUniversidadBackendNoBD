package pe.edu.utp.uni.app.repository.impl;
import org.springframework.stereotype.Repository;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.repository.UsuarioRepository;
import pe.edu.utp.uni.app.struct.AVLTree;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final Map<Long, Usuario> store = new HashMap<>();

    private final AVLTree<String, Usuario> indexByUsername = new AVLTree<>();

    private final AtomicLong seq = new AtomicLong(0L);

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    public Usuario buscarPorUsuario(String username) {
        lock.readLock().lock();
        try {
            return indexByUsername.get(username);
        }
        finally {
            lock.readLock().unlock();
        }
    }
    public Usuario guardarUsuario(Usuario u) {
        lock.writeLock().lock();
        try {
            if (u.id == null) u.id = seq.incrementAndGet();
            store.put(u.id, u);
            indexByUsername.put(u.username, u);
            return u;
        } finally { lock.writeLock().unlock(); }
    }

    @Override
    public Usuario findById(Long id) {
        lock.readLock().lock();
        try { return store.get(id); } finally { lock.readLock().unlock(); }
    }

    @Override
    public java.util.List<Usuario> findAll() {
        lock.readLock().lock();
        try { return new java.util.ArrayList<>(store.values()); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public void deleteById(Long id) {
        lock.writeLock().lock();
        try {
            Usuario removed = store.remove(id);
            if (removed != null && removed.username != null) {
                indexByUsername.remove(removed.username);
            }
        } finally { lock.writeLock().unlock(); }
    }
}
