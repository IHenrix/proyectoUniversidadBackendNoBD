package pe.edu.utp.uni.app.repository.impl;

import org.springframework.stereotype.Repository;
import pe.edu.utp.uni.app.model.relationship.DocenteCurso;
import pe.edu.utp.uni.app.repository.DocenteCursoRepository;
import pe.edu.utp.uni.app.struct.CircularSinglyLinkedList;
import pe.edu.utp.uni.app.struct.SinglyLinkedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class DocenteCursoRepositoryImpl implements DocenteCursoRepository {
    private final Map<Long, DocenteCurso> store = new HashMap<>();
    private final Map<Long, SinglyLinkedList<DocenteCurso>> docentesPorCurso = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<Long, CircularSinglyLinkedList<DocenteCurso>> cursosPorDocente = new HashMap<>();

    @Override
    public DocenteCurso save(DocenteCurso dc) {
        lock.writeLock().lock();
        try {
            boolean isNew = (dc.id == null) || !store.containsKey(dc.id);
            if (dc.id == null) dc.id = seq.incrementAndGet();
            store.put(dc.id, dc);
            if (isNew) {
                docentesPorCurso.computeIfAbsent(dc.curso_id, k -> new SinglyLinkedList<>()).addLast(dc);
                cursosPorDocente.computeIfAbsent(dc.usuario_id, k -> new CircularSinglyLinkedList<>()).addLast(dc);
            }
            return dc;
        } finally { lock.writeLock().unlock(); }
    }
    @Override
    public List<DocenteCurso> listByCursoId(Long cursoId) {
        lock.readLock().lock();
        try {
            var list = docentesPorCurso.get(cursoId);
            var out = new ArrayList<DocenteCurso>();
            if (list == null) return out;
            for (var n = list.head(); n != null; n = n.next) out.add(n.value);
            return out;
        } finally { lock.readLock().unlock(); }
    }

    @Override
    public List<DocenteCurso> listByDocenteId(Long docenteId) {
        lock.readLock().lock();
        try {
            var clist = cursosPorDocente.get(docenteId);
            var out = new ArrayList<DocenteCurso>();
            if (clist == null || clist.head() == null) return out;
            var start = clist.head();
            var n = start;
            do { out.add(n.value); n = n.next; } while (n != null && n != start);
            return out;
        } finally { lock.readLock().unlock(); }
    }
}
