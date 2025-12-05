package pe.edu.utp.uni.app.repository.impl;

import org.springframework.stereotype.Repository;
import pe.edu.utp.uni.app.model.Nota;
import pe.edu.utp.uni.app.repository.NotaRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class NotaRepositoryImpl implements NotaRepository {
    private final Map<Long, Nota> store = new HashMap<>();
    private final Map<Long, Map<Long, Nota>> byMatriculaThenCriterio = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Nota save(Nota n) {
        lock.writeLock().lock();
        try {
            if (n.id == null) n.id = seq.incrementAndGet();
            store.put(n.id, n);
            byMatriculaThenCriterio
                    .computeIfAbsent(n.matricula_id, k -> new HashMap<>())
                    .put(n.criterio_id, n);
            return n;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Nota findByMatriculaAndCriterio(Long matriculaId, Long criterioId) {
        lock.readLock().lock();
        try {
            Map<Long, Nota> m = byMatriculaThenCriterio.get(matriculaId);
            return m == null ? null : m.get(criterioId);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void deleteByMatriculaAndCriterio(Long matriculaId, Long criterioId) {
        lock.writeLock().lock();
        try {
            Map<Long, Nota> m = byMatriculaThenCriterio.get(matriculaId);
            if (m != null) {
                Nota removed = m.remove(criterioId);
                if (removed != null) store.remove(removed.id);
                if (m.isEmpty()) byMatriculaThenCriterio.remove(matriculaId);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Nota findById(Long id) {
        lock.readLock().lock();
        try { return store.get(id); }
        finally { lock.readLock().unlock(); }
    }

    @Override
    public void deleteById(Long id) {
        lock.writeLock().lock();
        try {
            Nota n = store.remove(id);
            if (n != null) {
                Map<Long, Nota> m = byMatriculaThenCriterio.get(n.matricula_id);
                if (m != null) {
                    m.remove(n.criterio_id);
                    if (m.isEmpty()) byMatriculaThenCriterio.remove(n.matricula_id);
                }
            }
        } finally { lock.writeLock().unlock(); }
    }

}
