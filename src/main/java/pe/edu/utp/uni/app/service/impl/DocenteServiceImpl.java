package pe.edu.utp.uni.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pe.edu.utp.uni.app.model.*;
import pe.edu.utp.uni.app.repository.*;
import pe.edu.utp.uni.app.request.NotaItem;
import pe.edu.utp.uni.app.response.AlumnoListaCursoResponse;
import pe.edu.utp.uni.app.response.CursoDocenteResponse;
import pe.edu.utp.uni.app.service.DocenteService;
import pe.edu.utp.uni.app.struct.MinHeap;
import pe.edu.utp.uni.app.struct.SimpleStack;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class DocenteServiceImpl implements DocenteService {
    private final DocenteSeccionRepository docenteSeccionRepository;
    private final CursoRepository cursoRepository;
    private final SeccionRepository seccionRepository;
    private final MatriculaRepository matriculaRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotaRepository notaRepository;
    private final CriterioEvaluacionRepository criterioEvaluacionRepository;

    @Override
    public List<CursoDocenteResponse> listarSeccionesDeDocente(Long docenteId) {
        List<DocenteSeccion> asignaciones = docenteSeccionRepository.findByDocenteId(docenteId);
        List<CursoDocenteResponse> out = new ArrayList<>();
        for (DocenteSeccion ds : asignaciones) {
            if (ds.activo == null || !ds.activo) continue;
            Seccion seccion = ds.seccion_id == null ? null : seccionRepository.findById(ds.seccion_id);
            if (seccion == null) continue;
            Curso c = cursoRepository.findById(seccion.curso_id);
            if (c == null) continue;

            // Contar alumnos matriculados en la sección
            List<Matricula> inscripciones = matriculaRepository.findBySeccionId(seccion.id);
            int alumnos = 0;
            for (Matricula m : inscripciones) {
                if (Boolean.TRUE.equals(m.activo)) alumnos++;
            }

            out.add(new CursoDocenteResponse(
                    seccion.id,
                    seccion.codigo,
                    c.id,
                    c.nombre,
                    c.horas_semanales,
                    c.creditos,
                    seccion.modalidad,
                    seccion.horarios,
                    alumnos));
        }
        out.sort(Comparator.comparing(a -> a.curso == null ? "" : a.curso));
        return out;
    }

    @Override
    public List<AlumnoListaCursoResponse> listarAlumnosPorSeccion(Long seccionId) {
        List<Matricula> inscripciones = matriculaRepository.findBySeccionId(seccionId);

        MinHeap<AlumnoListaCursoResponse> heap = new MinHeap<>(Comparator.comparing(
                        (AlumnoListaCursoResponse x) -> x.paterno == null ? "" : x.paterno)
                .thenComparing(x -> x.nombre == null ? "" : x.nombre)
                .thenComparing(x -> x.materno == null ? "" : x.materno)
                .thenComparing(x -> x.codigo == null ? "" : x.codigo)
        );

        for (Matricula m : inscripciones) {
            Usuario u = usuarioRepository.findById(m.alumno_id);

            // Calcular nota final a partir de los criterios
            Double notaFinal = calcularNotaFinal(m.id, seccionId);
            Double notaAlumnoReal = calcularNotaAlumnoReal(m.id, seccionId);
            String notaAlumnoFinalStr = notaAlumnoReal == null ? null
                    : notaFavorAlumno(notaAlumnoReal);
            String estado = calcularEstado(notaAlumnoReal);

            AlumnoListaCursoResponse dto = new AlumnoListaCursoResponse(
                    m.id,
                    u == null ? null : u.nombre,
                    u == null ? null : u.paterno,
                    u == null ? null : u.materno,
                    u == null ? null : u.codigo,
                    notaFinal,
                    notaAlumnoFinalStr,
                    estado,
                    notaAlumnoReal
            );
            heap.push(dto);
        }

        List<AlumnoListaCursoResponse> out = new ArrayList<>();
        while (!heap.isEmpty()) out.add(heap.pop());
        return out;
    }

    @Override
    public void registrarOEditarNotas(Long matriculaId, Long seccionId, List<NotaItem> notas) {
        Matricula m = matriculaRepository.findById(matriculaId);
        if (m == null || (m.seccion_id != null && !m.seccion_id.equals(seccionId))) {
            throw new IllegalArgumentException("El alumno no pertenece a la sección");
        }
        SimpleStack<Runnable> undo = new SimpleStack<>();
        List<Nota> afectadas = new ArrayList<>();

        try {
            for (NotaItem it : notas) {
                Long criterioId = it.id;
                Double notaValor = toValidNumber(it.nota);
                Double notaAlumno = toValidNumber(it.notaAlumno);

                Nota existente = notaRepository.findByMatriculaAndCriterio(matriculaId, criterioId);

                if ((notaValor != null && (notaValor < 0.0 || notaValor > 20.0)) || (notaAlumno != null && (notaAlumno < 0.0 || notaAlumno > 20.0))) {
                    throw new IllegalArgumentException("Nota fuera de rango");
                }

                if (existente != null) {
                    if (notaValor == null || notaAlumno == null) {
                        Nota snapshot = new Nota(existente.id, existente.matricula_id, existente.criterio_id, existente.nota, existente.nota_alumno);
                        notaRepository.deleteByMatriculaAndCriterio(matriculaId, criterioId);
                        undo.push(() -> notaRepository.save(snapshot));
                    } else {
                        Nota before = new Nota(existente.id, existente.matricula_id, existente.criterio_id, existente.nota, existente.nota_alumno);
                        existente.nota = notaValor;
                        existente.nota_alumno = notaAlumno;
                        notaRepository.save(existente);
                        undo.push(() -> notaRepository.save(before));
                        afectadas.add(existente);
                    }
                } else {
                    if (notaValor != null && notaAlumno != null) {
                        Nota nueva = new Nota(null, matriculaId, criterioId, notaValor, notaAlumno);
                        notaRepository.save(nueva);
                        undo.push(() -> notaRepository.deleteByMatriculaAndCriterio(matriculaId, criterioId));
                        afectadas.add(nueva);
                    }
                }
            }

            // En la nueva arquitectura, las notas finales se calculan dinámicamente
            // No necesitamos actualizar campos de AlumnoCurso porque ya no existen en Matricula
            // La lógica de cálculo ahora está en los métodos helper

        } catch (Exception ex) {
            while (!undo.isEmpty()) undo.pop().run();
            throw ex;
        }
    }

    @Override
    public int eliminarNota(Long notaId, Long matriculaId, Long seccionId) {
        Nota n = notaRepository.findById(notaId);
        if (n == null) return 0;
        if (!n.matricula_id.equals(matriculaId)) return 0;

        notaRepository.deleteById(notaId);

        Matricula m = matriculaRepository.findById(matriculaId);
        if (m != null && seccionId != null && m.seccion_id != null && !m.seccion_id.equals(seccionId)) return 0;

        // En la nueva arquitectura, no hay campos de nota final en Matricula
        // Las notas se calculan dinámicamente cuando se necesitan

        return 1;
    }

    /**
     * Calcula la nota final (promedio de nota base ponderado)
     */
    private Double calcularNotaFinal(Long matriculaId, Long seccionId) {
        if (seccionId == null) return null;

        List<CriterioEvaluacion> criterios = criterioEvaluacionRepository.listBySeccionId(seccionId);
        if (criterios.isEmpty()) return null;

        double sumaTotal = 0.0;
        double sumaPonderacion = 0.0;

        for (CriterioEvaluacion ce : criterios) {
            Nota n = notaRepository.findByMatriculaAndCriterio(matriculaId, ce.id);
            if (n != null && n.nota != null && ce.porcentaje != null) {
                sumaTotal += (n.nota * ce.porcentaje / 100.0);
                sumaPonderacion += ce.porcentaje;
            }
        }

        return sumaPonderacion > 0 ? sumaTotal : null;
    }

    /**
     * Calcula la nota real del alumno (promedio de nota_alumno ponderado)
     */
    private Double calcularNotaAlumnoReal(Long matriculaId, Long seccionId) {
        if (seccionId == null) return null;

        List<CriterioEvaluacion> criterios = criterioEvaluacionRepository.listBySeccionId(seccionId);
        if (criterios.isEmpty()) return null;

        double sumaTotal = 0.0;
        double sumaPonderacion = 0.0;

        for (CriterioEvaluacion ce : criterios) {
            Nota n = notaRepository.findByMatriculaAndCriterio(matriculaId, ce.id);
            if (n != null && n.nota_alumno != null && ce.porcentaje != null) {
                sumaTotal += (n.nota_alumno * ce.porcentaje / 100.0);
                sumaPonderacion += ce.porcentaje;
            }
        }

        return sumaPonderacion > 0 ? sumaTotal : null;
    }

    /**
     * Calcula el estado basado en la nota real del alumno
     */
    private String calcularEstado(Double notaAlumnoReal) {
        if (notaAlumnoReal == null) return "E";  // En proceso
        return notaAlumnoReal >= 11.6 ? "A" : "D";  // Aprobado / Desaprobado
    }

    private static Double toValidNumber(Object v) {
        if (v == null) return null;
        if (v instanceof Number) return ((Number) v).doubleValue();
        String s = String.valueOf(v).trim();
        if (s.isEmpty()) return null;
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isValidNota(Object v) {
        Double d = toValidNumber(v);
        return d != null && d >= 0.0;
    }

    private static double toDouble(Object v) {
        Double d = toValidNumber(v);
        return d == null ? 0.0 : d;
    }

    private static String notaFavorAlumno(double value) {
        double decimal = Math.round((value % 1) * 100.0) / 100.0;
        int rounded = (int) value + (decimal >= 0.6 ? 1 : 0);
        return (rounded < 10 ? "0" + rounded : String.valueOf(rounded));
    }

    private static boolean sumaPorcentajesCorrecta(List<NotaItem> notas) {
        if (CollectionUtils.isEmpty(notas)) return false;
        double suma = 0.0;
        for (NotaItem it : notas) {
            suma += it.porcentaje == null ? 0.0 : it.porcentaje;
        }
        return Math.abs(suma - 100.0) < 0.01;
    }

}
