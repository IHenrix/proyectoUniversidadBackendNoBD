package pe.edu.utp.uni.app.repository.impl;

import org.springframework.stereotype.Repository;
import pe.edu.utp.uni.app.model.relationship.AlumnoCurso;
import pe.edu.utp.uni.app.repository.AlumnoCursoRepository;
import pe.edu.utp.uni.app.struct.DoublyLinkedList;
import pe.edu.utp.uni.app.struct.SinglyLinkedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class AlumnoCursoRepositoryImpl implements AlumnoCursoRepository {

    private final Map<Long, AlumnoCurso> store = new HashMap<>();
    private final Map<Long, DoublyLinkedList<AlumnoCurso>> byUsuario = new HashMap<>();
    private final Map<Long, SinglyLinkedList<AlumnoCurso>> byCurso = new HashMap<>();
    private final AtomicLong seq = new AtomicLong(0);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public AlumnoCurso save(AlumnoCurso ac) {
        lock.writeLock().lock();
        try {
            boolean isNew = (ac.id == null) || !store.containsKey(ac.id);
            if (ac.id == null) ac.id = seq.incrementAndGet();
            store.put(ac.id, ac);

            if (isNew) {
                byUsuario.computeIfAbsent(ac.usuario_id, k -> new DoublyLinkedList<>()).addLast(ac);
                byCurso.computeIfAbsent(ac.curso_id, k -> new SinglyLinkedList<>()).addLast(ac);
            }
            return ac;
        } finally { lock.writeLock().unlock(); }
    }

    @Override
    public List<AlumnoCurso> listByUsuarioId(Long usuarioId) {
        lock.readLock().lock();
        try {
            DoublyLinkedList<AlumnoCurso> list = byUsuario.get(usuarioId);
            List<AlumnoCurso> out = new ArrayList<>();
            if (list == null) return out;
            for (var n = list.head(); n != null; n = n.next) out.add(n.value);
            return out;
        } finally { lock.readLock().unlock(); }
    }

    @Override
    public List<AlumnoCurso> listByCursoId(Long cursoId) {
        lock.readLock().lock();
        try {
            SinglyLinkedList<AlumnoCurso> list = byCurso.get(cursoId);
            List<AlumnoCurso> out = new ArrayList<>();
            if (list == null) return out;
            for (var n = list.head(); n != null; n = n.next) out.add(n.value);
            return out;
        } finally { lock.readLock().unlock(); }
    }

    @Override
    public AlumnoCurso findById(Long id) {
        lock.readLock().lock();
        try {
            return store.get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

}
