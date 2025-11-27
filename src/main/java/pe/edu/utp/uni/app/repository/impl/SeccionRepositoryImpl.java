package pe.edu.utp.uni.app.repository.impl;

import org.springframework.stereotype.Repository;
import pe.edu.utp.uni.app.model.Seccion;
import pe.edu.utp.uni.app.repository.SeccionRepository;
import pe.edu.utp.uni.app.struct.SinglyLinkedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class SeccionRepositoryImpl implements SeccionRepository {
    private final Map<Long, Seccion> store = new HashMap<>();
    private final Map<Long, SinglyLinkedList<Seccion>> byCurso = new HashMap<>();
    private final Map<Long, SinglyLinkedList<Seccion>> byDocente = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Seccion save(Seccion s) {
        lock.writeLock().lock();
        try {
            boolean isNew = (s.id == null) || !store.containsKey(s.id);
            if (s.id == null) s.id = seq.incrementAndGet();
            store.put(s.id, s);
            if (isNew) {
                byCurso.computeIfAbsent(s.curso_id, k -> new SinglyLinkedList<>()).addLast(s);
                if (s.docente_id != null) {
                    byDocente.computeIfAbsent(s.docente_id, k -> new SinglyLinkedList<>()).addLast(s);
                }
            }
            return s;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Seccion findById(Long id) {
        lock.readLock().lock();
        try {
            return store.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Seccion> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(store.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Seccion> listByCursoId(Long cursoId) {
        lock.readLock().lock();
        try {
            SinglyLinkedList<Seccion> list = byCurso.get(cursoId);
            List<Seccion> out = new ArrayList<>();
            if (list == null) return out;
            for (var n = list.head(); n != null; n = n.next) out.add(n.value);
            return out;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Seccion> listByDocenteId(Long docenteId) {
        lock.readLock().lock();
        try {
            SinglyLinkedList<Seccion> list = byDocente.get(docenteId);
            List<Seccion> out = new ArrayList<>();
            if (list == null) return out;
            for (var n = list.head(); n != null; n = n.next) out.add(n.value);
            return out;
        } finally {
            lock.readLock().unlock();
        }
    }
}
