package pe.edu.utp.uni.app.repository.impl;

import org.springframework.stereotype.Repository;
import pe.edu.utp.uni.app.model.CriterioEvaluacion;
import pe.edu.utp.uni.app.repository.CriterioEvaluacionRepository;
import pe.edu.utp.uni.app.struct.SinglyLinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class CriterioEvaluacionRepositoryImpl implements CriterioEvaluacionRepository {
    private final ConcurrentHashMap<Long, SinglyLinkedList<CriterioEvaluacion>> criteriosPorCurso = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    @Override
    public CriterioEvaluacion save(CriterioEvaluacion ce) {
        lock.writeLock().lock();
        try {
            if (ce.id == null) ce.id = seq.incrementAndGet();
            criteriosPorCurso.computeIfAbsent(ce.curso_id, k -> new SinglyLinkedList<>()).addLast(ce);
            return ce;
        } finally { lock.writeLock().unlock(); }
    }

    @Override
    public List<CriterioEvaluacion> listByCursoId(Long cursoId) {
        lock.readLock().lock();
        try {
            var list = criteriosPorCurso.get(cursoId);
            var out = new ArrayList<CriterioEvaluacion>();
            if (list == null) return out;
            for (var n = list.head(); n != null; n = n.next) out.add(n.value);
            return out;
        } finally { lock.readLock().unlock(); }
    }
}
