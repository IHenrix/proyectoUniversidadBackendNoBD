package pe.edu.utp.uni.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pe.edu.utp.uni.app.model.Curso;
import pe.edu.utp.uni.app.model.Nota;
import pe.edu.utp.uni.app.model.Seccion;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.model.relationship.AlumnoCurso;
import pe.edu.utp.uni.app.model.relationship.DocenteCurso;
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
    private final DocenteCursoRepository docenteCursoRepository;
    private final CursoRepository cursoRepository;
    private final SeccionRepository seccionRepository;
    private final AlumnoCursoRepository alumnoCursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotaRepository notaRepository;
    @Override
    public List<CursoDocenteResponse> listarSeccionesDeDocente(Long docenteId) {
        List<DocenteCurso> asignaciones = docenteCursoRepository.listByDocenteId(docenteId);
        List<CursoDocenteResponse> out = new ArrayList<>();
        for (DocenteCurso dc : asignaciones) {
            if (dc.activo == null || !dc.activo) continue;
            Seccion seccion = dc.seccion_id == null ? null : seccionRepository.findById(dc.seccion_id);
            Curso c = cursoRepository.findById(dc.curso_id != null ? dc.curso_id : (seccion == null ? null : seccion.curso_id));
            if (c == null) continue;
            int alumnos = 0;
            List<AlumnoCurso> inscripciones = dc.seccion_id != null
                    ? alumnoCursoRepository.listBySeccionId(dc.seccion_id)
                    : alumnoCursoRepository.listByCursoId(c.id);
            for (AlumnoCurso ac : inscripciones) {
                if (Boolean.TRUE.equals(ac.activo)) alumnos++;
            }
            out.add(new CursoDocenteResponse(
                    seccion == null ? c.id : seccion.id,
                    seccion == null ? null : seccion.codigo,
                    c.id,
                    c.nombre,
                    c.horas_semanales,
                    c.creditos,
                    seccion != null && seccion.modalidad != null ? seccion.modalidad : c.modalidad,
                    seccion == null ? java.util.List.of() : seccion.horarios,
                    alumnos));
        }
        out.sort(Comparator.comparing(a -> a.curso == null ? "" : a.curso));
        return out;
    }

    @Override
    public List<AlumnoListaCursoResponse> listarAlumnosPorSeccion(Long seccionId) {
        List<AlumnoCurso> inscripciones = alumnoCursoRepository.listBySeccionId(seccionId);

        MinHeap<AlumnoListaCursoResponse> heap = new MinHeap<>(Comparator.comparing(
                        (AlumnoListaCursoResponse x) -> x.paterno == null ? "" : x.paterno)
                .thenComparing(x -> x.nombre == null ? "" : x.nombre)
                .thenComparing(x -> x.materno == null ? "" : x.materno)
                .thenComparing(x -> x.codigo == null ? "" : x.codigo)
        );
        for (AlumnoCurso ac : inscripciones) {
            Usuario u = usuarioRepository.findById(ac.usuario_id);
            String notaAlumnoFinalStr = ac.nota_alumno_final == null ? null
                    : String.format("%02d", ac.nota_alumno_final.intValue());

            AlumnoListaCursoResponse dto = new AlumnoListaCursoResponse(
                    ac.id,
                    u == null ? null : u.nombre,
                    u == null ? null : u.paterno,
                    u == null ? null : u.materno,
                    u == null ? null : u.codigo,
                    ac.nota_final,
                    notaAlumnoFinalStr,
                    ac.estado,
                    ac.nota_alumno_real
            );
            heap.push(dto);
        }

        List<AlumnoListaCursoResponse> out = new ArrayList<>();
        while (!heap.isEmpty()) out.add(heap.pop());
        return out;
    }

    @Override
    public void registrarOEditarNotas(Long alumnoCursoId, Long seccionId, List<NotaItem> notas) {
        AlumnoCurso ac = alumnoCursoRepository.findById(alumnoCursoId);
        if (ac == null || (ac.seccion_id != null && !ac.seccion_id.equals(seccionId))) {
            throw new IllegalArgumentException("El alumno no pertenece a la sección");
        }
        SimpleStack<Runnable> undo = new SimpleStack<>();
        List<Nota> afectadas = new ArrayList<>();

        try {
            for (NotaItem it : notas) {
                Long criterioId = it.id;
                Double notaValor = toValidNumber(it.nota);
                Double notaAlumno = toValidNumber(it.notaAlumno);

                Nota existente = notaRepository.findByAlumnoCursoAndCriterio(alumnoCursoId, criterioId);

                if ((notaValor != null && (notaValor < 0.0 || notaValor > 20.0)) || (notaAlumno != null && (notaAlumno < 0.0 || notaAlumno > 20.0))) {
                    throw new IllegalArgumentException("Nota fuera de rango");
                }

                if (existente != null) {
                    if (notaValor == null || notaAlumno == null) {
                        Nota snapshot = new Nota(existente.id, existente.alumno_curso_id, existente.criterio_id, existente.nota, existente.nota_alumno);
                        notaRepository.deleteByAlumnoCursoAndCriterio(alumnoCursoId, criterioId);
                        undo.push(() -> notaRepository.save(snapshot));
                    } else {
                        Nota before = new Nota(existente.id, existente.alumno_curso_id, existente.criterio_id, existente.nota, existente.nota_alumno);
                        existente.nota = notaValor;
                        existente.nota_alumno = notaAlumno;
                        notaRepository.save(existente);
                        undo.push(() -> notaRepository.save(before));
                        afectadas.add(existente);
                    }
                } else {
                    if (notaValor != null && notaAlumno != null) {
                        Nota nueva = new Nota(null, alumnoCursoId, criterioId, notaValor, notaAlumno);
                        notaRepository.save(nueva);
                        undo.push(() -> notaRepository.deleteByAlumnoCursoAndCriterio(alumnoCursoId, criterioId));
                        afectadas.add(nueva);
                    }
                }
            }

            boolean todasValidas = true;
            for (NotaItem it : notas) {
                if (!isValidNota(it.nota) || !isValidNota(it.notaAlumno)) {
                    todasValidas = false;
                    break;
                }
            }

            AlumnoCurso acRef = alumnoCursoRepository.findById(alumnoCursoId);
            if (acRef == null) throw new IllegalStateException("AlumnoCurso no existe");

            boolean porcentajesValidos = sumaPorcentajesCorrecta(notas);

            if (todasValidas && porcentajesValidos) {
                double promedioNota = 0.0;
                double promedioNotaAlumno = 0.0;
                for (NotaItem it : notas) {
                    double porc = (it.porcentaje == null ? 0.0 : it.porcentaje) / 100.0;
                    promedioNota += toDouble(it.nota) * porc;
                    promedioNotaAlumno += toDouble(it.notaAlumno) * porc;
                }
                String transform = notaFavorAlumno(promedioNotaAlumno);
                String estado = promedioNotaAlumno >= 11.6 ? "A" : "D";

                Double prevNotaFinal = acRef.nota_final;
                Double prevNotaAlumnoFinal = acRef.nota_alumno_final;
                Double prevNotaAlumnoReal = acRef.nota_alumno_real;
                String prevEstado = acRef.estado;

                acRef.nota_final = promedioNota;
                acRef.nota_alumno_final = toDouble(transform);
                acRef.nota_alumno_real = promedioNotaAlumno;
                acRef.estado = estado;

                alumnoCursoRepository.save(acRef);
                undo.push(() -> {
                    acRef.nota_final = prevNotaFinal;
                    acRef.nota_alumno_final = prevNotaAlumnoFinal;
                    acRef.nota_alumno_real = prevNotaAlumnoReal;
                    acRef.estado = prevEstado;
                    alumnoCursoRepository.save(acRef);
                });
            } else {
                Double prevNotaFinal = acRef.nota_final;
                Double prevNotaAlumnoFinal = acRef.nota_alumno_final;
                Double prevNotaAlumnoReal = acRef.nota_alumno_real;
                String prevEstado = acRef.estado;

                acRef.nota_final = null;
                acRef.nota_alumno_final = null;
                acRef.nota_alumno_real = null;
                acRef.estado = "E";
                alumnoCursoRepository.save(acRef);

                undo.push(() -> {
                    acRef.nota_final = prevNotaFinal;
                    acRef.nota_alumno_final = prevNotaAlumnoFinal;
                    acRef.nota_alumno_real = prevNotaAlumnoReal;
                    acRef.estado = prevEstado;
                    alumnoCursoRepository.save(acRef);
                });
            }
        } catch (Exception ex) {
            while (!undo.isEmpty()) undo.pop().run();
            throw ex;
        }
    }

    @Override
    public int eliminarNota(Long notaId, Long alumnoCursoId, Long seccionId) {
        Nota n = notaRepository.findById(notaId);
        if (n == null) return 0;
        if (!n.alumno_curso_id.equals(alumnoCursoId)) return 0;

        notaRepository.deleteById(notaId);

        AlumnoCurso ac = alumnoCursoRepository.findById(alumnoCursoId);
        if (ac != null && seccionId != null && ac.seccion_id != null && !ac.seccion_id.equals(seccionId)) return 0;
        if (ac != null && (ac.estado == null || !"E".equals(ac.estado))) {
            ac.nota_final = null;
            ac.nota_alumno_final = null;
            ac.nota_alumno_real = null;
            ac.estado = "E";
            alumnoCursoRepository.save(ac);
        }
        return 1;
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
