package pe.edu.utp.uni.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.uni.app.model.*;
import pe.edu.utp.uni.app.repository.*;
import pe.edu.utp.uni.app.response.CursoAlumnoResponse;
import pe.edu.utp.uni.app.response.NotasAlumnosResponse;
import pe.edu.utp.uni.app.service.AlumnoService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumnoServiceImpl implements AlumnoService {

    private final MatriculaRepository matriculaRepository;
    private final CursoRepository cursoRepository;
    private final DocenteSeccionRepository docenteSeccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CriterioEvaluacionRepository criterioEvaluacionRepository;
    private final NotaRepository notaRepository;
    private final SeccionRepository seccionRepository;

    @Override
    public List<CursoAlumnoResponse> listarSeccionesPorUsuario(Long usuarioId) {
        List<Matricula> matriculas = matriculaRepository.findByAlumnoId(usuarioId);
        Usuario alumno = usuarioRepository.findById(usuarioId);
        String alumnoNombre = alumno == null ? null : (alumno.nombre + " " + alumno.paterno + " " + alumno.materno).trim();

        return matriculas.stream()
                .filter(m -> Boolean.TRUE.equals(m.activo))
                .map(m -> {
                    Seccion seccion = m.seccion_id == null ? null : seccionRepository.findById(m.seccion_id);
                    Curso c = seccion == null ? null : cursoRepository.findById(seccion.curso_id);
                    String docenteNombre = seccion != null ? firstDocenteNombreOrdenadoPorSeccion(seccion.id) : null;

                    // Calcular nota final a partir de los criterios
                    Double notaFinal = calcularNotaFinal(m.id, seccion == null ? null : seccion.id);
                    String notaAlumnoFinalStr = notaFinal == null ? null : String.format("%02d", notaFinal.intValue());

                    return new CursoAlumnoResponse(
                            c == null ? null : c.id,
                            seccion == null ? null : seccion.id,
                            seccion == null ? null : seccion.codigo,
                            c == null ? null : c.nombre,
                            c == null ? null : c.horas_semanales,
                            c == null ? null : c.creditos,
                            seccion != null && seccion.modalidad != null ? seccion.modalidad : (c == null ? null : c.modalidad),
                            seccion == null ? java.util.List.of() : seccion.horarios,
                            alumnoNombre,
                            docenteNombre,
                            m.id,
                            m.alumno_id,
                            notaFinal,
                            notaAlumnoFinalStr,
                            m.activo ? "A" : "I"  // Estado basado en activo
                    );
                })
                .sorted(Comparator.comparing(r -> r.curso == null ? "" : r.curso))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotasAlumnosResponse> listarNotasAlumnos(Long seccionId, Long matriculaId) {
        List<CriterioEvaluacion> criterios = seccionId == null
                ? java.util.List.of()
                : criterioEvaluacionRepository.listBySeccionId(seccionId);
        if (criterios.isEmpty()) {
            Matricula m = matriculaRepository.findById(matriculaId);
            if (m != null && m.seccion_id != null) {
                Seccion seccion = seccionRepository.findById(m.seccion_id);
                if (seccion != null && seccion.curso_id != null) {
                    criterios = criterioEvaluacionRepository.listByCursoId(seccion.curso_id);
                }
            }
        }
        return criterios.stream().map(ce -> {
            Nota n = notaRepository.findByMatriculaAndCriterio(matriculaId, ce.id);
            String notaAlumnoStr = (n == null || n.nota_alumno == null)
                    ? null
                    : String.format("%02d", n.nota_alumno.intValue());
            Integer porcInt = ce.porcentaje == null ? null : ce.porcentaje.intValue();
            return new NotasAlumnosResponse(
                    ce.id,
                    ce.nombre_criterio,
                    ce.orden,
                    porcInt,
                    n == null ? null : n.nota,
                    notaAlumnoStr,
                    n == null ? null : n.id
            );
        }).collect(Collectors.toList());
    }

    private String firstDocenteNombreOrdenadoPorSeccion(Long seccionId) {
        List<DocenteSeccion> dss = docenteSeccionRepository.findBySeccionId(seccionId);
        return dss.stream()
                .filter(ds -> Boolean.TRUE.equals(ds.activo))
                .map(ds -> usuarioRepository.findById(ds.docente_id))
                .filter(Objects::nonNull)
                .map(u -> (u.nombre + " " + u.paterno + " " + u.materno).trim())
                .sorted()
                .findFirst().orElse(null);
    }

    /**
     * Calcula la nota final sumando los criterios ponderados
     */
    private Double calcularNotaFinal(Long matriculaId, Long seccionId) {
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

    @Override
    public CursoAlumnoResponse matricularEnSeccion(Long usuarioId, Long seccionId) {
        Usuario alumno = usuarioRepository.findById(usuarioId);
        Seccion seccion = seccionRepository.findById(seccionId);
        if (alumno == null || seccion == null) return null;

        // Verificar que haya vacantes
        if (!seccion.tieneVacantes()) return null;

        // Evitar duplicado
        Matricula existente = matriculaRepository.findByAlumnoAndSeccion(usuarioId, seccionId);
        if (existente != null && existente.activo) return null;

        Matricula nueva = new Matricula(null, usuarioId, seccionId);
        matriculaRepository.save(nueva);

        // Ocupar vacante
        seccion.ocuparVacante();
        seccionRepository.save(seccion);

        String alumnoNombre = (alumno.nombre + " " + alumno.paterno + " " + alumno.materno).trim();
        String docenteNombre = firstDocenteNombreOrdenadoPorSeccion(seccion.id);
        Curso c = cursoRepository.findById(seccion.curso_id);
        return new CursoAlumnoResponse(
                c == null ? null : c.id,
                seccion.id,
                seccion.codigo,
                c == null ? null : c.nombre,
                c == null ? null : c.horas_semanales,
                c == null ? null : c.creditos,
                seccion.modalidad,
                seccion.horarios,
                alumnoNombre,
                docenteNombre,
                nueva.id,
                nueva.alumno_id,
                null,
                null,
                "A"  // Activo
        );
    }

    @Override
    public List<CursoAlumnoResponse> listarSeccionesDisponibles(Long usuarioId) {
        List<Long> inscritos = matriculaRepository.findByAlumnoId(usuarioId).stream()
                .filter(m -> Boolean.TRUE.equals(m.activo))
                .map(m -> m.seccion_id)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return seccionRepository.findAll().stream()
                .filter(s -> s.id != null && !inscritos.contains(s.id))
                .filter(s -> s.tieneVacantes())  // Solo mostrar secciones con vacantes
                .map(seccion -> {
                    Curso c = cursoRepository.findById(seccion.curso_id);
                    String docenteNombre = firstDocenteNombreOrdenadoPorSeccion(seccion.id);
                    return new CursoAlumnoResponse(
                            c == null ? null : c.id,
                            seccion.id,
                            seccion.codigo,
                            c == null ? null : c.nombre,
                            c == null ? null : c.horas_semanales,
                            c == null ? null : c.creditos,
                            seccion.modalidad,
                            seccion.horarios,
                            null,
                            docenteNombre,
                            null,
                            usuarioId,
                            null,
                            null,
                            seccion.estado
                    );
                })
                .sorted(Comparator.comparing(r -> r.curso == null ? "" : r.curso))
                .collect(Collectors.toList());
    }
}
