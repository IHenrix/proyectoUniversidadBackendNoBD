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
    private final Map<Long, Map<Long, Nota>> byAlumnoCursoThenCriterio = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Nota save(Nota n) {
        lock.writeLock().lock();
        try {
            if (n.id == null) n.id = seq.incrementAndGet();
            store.put(n.id, n);
            byAlumnoCursoThenCriterio
                    .computeIfAbsent(n.alumno_curso_id, k -> new HashMap<>())
                    .put(n.criterio_id, n);
            return n;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Nota findByAlumnoCursoAndCriterio(Long alumnoCursoId, Long criterioId) {
        lock.readLock().lock();
        try {
            Map<Long, Nota> m = byAlumnoCursoThenCriterio.get(alumnoCursoId);
            return m == null ? null : m.get(criterioId);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void deleteByAlumnoCursoAndCriterio(Long alumnoCursoId, Long criterioId) {
        lock.writeLock().lock();
        try {
            Map<Long, Nota> m = byAlumnoCursoThenCriterio.get(alumnoCursoId);
            if (m != null) {
                Nota removed = m.remove(criterioId);
                if (removed != null) store.remove(removed.id);
                if (m.isEmpty()) byAlumnoCursoThenCriterio.remove(alumnoCursoId);
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
                Map<Long, Nota> m = byAlumnoCursoThenCriterio.get(n.alumno_curso_id);
                if (m != null) {
                    m.remove(n.criterio_id);
                    if (m.isEmpty()) byAlumnoCursoThenCriterio.remove(n.alumno_curso_id);
                }
            }
        } finally { lock.writeLock().unlock(); }
    }

}
