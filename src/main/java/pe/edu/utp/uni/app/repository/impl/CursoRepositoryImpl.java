package pe.edu.utp.uni.app.repository.impl;

import org.springframework.stereotype.Repository;
import pe.edu.utp.uni.app.model.Curso;
import pe.edu.utp.uni.app.repository.CursoRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;
@Repository
public class CursoRepositoryImpl implements CursoRepository {
    private final Map<Long, Curso> store = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    @Override
    public Curso save(Curso c) {
        lock.writeLock().lock();
        try {
            if (c.id == null) c.id = seq.incrementAndGet();
            store.put(c.id, c);
            return c;
        } finally { lock.writeLock().unlock(); }
    }
    @Override
    public Curso findById(Long id) {
        lock.readLock().lock();
        try { return store.get(id); } finally { lock.readLock().unlock(); }
    }

    @Override
    public List<Curso> findAll() {
        lock.readLock().lock();
        try { return new ArrayList<>(store.values()); } finally { lock.readLock().unlock(); }
    }
}
