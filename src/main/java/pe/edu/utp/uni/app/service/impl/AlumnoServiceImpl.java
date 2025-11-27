package pe.edu.utp.uni.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.uni.app.model.CriterioEvaluacion;
import pe.edu.utp.uni.app.model.Curso;
import pe.edu.utp.uni.app.model.Seccion;
import pe.edu.utp.uni.app.model.Nota;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.model.relationship.AlumnoCurso;
import pe.edu.utp.uni.app.model.relationship.DocenteCurso;
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

    private final AlumnoCursoRepository alumnoCursoRepository;
    private final CursoRepository cursoRepository;
    private final DocenteCursoRepository docenteCursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CriterioEvaluacionRepository criterioEvaluacionRepository;
    private final NotaRepository notaRepository;
    private final SeccionRepository seccionRepository;
    @Override
    public List<CursoAlumnoResponse> listarSeccionesPorUsuario(Long usuarioId) {
        List<AlumnoCurso> acs = alumnoCursoRepository.listByUsuarioId(usuarioId);
        Usuario alumno = usuarioRepository.findById(usuarioId);
        String alumnoNombre = alumno == null ? null : (alumno.nombre + " " + alumno.paterno + " " + alumno.materno).trim();

        return acs.stream()
                .filter(ac -> Boolean.TRUE.equals(ac.activo))
                .map(ac -> {
                    Seccion seccion = ac.seccion_id == null ? null : seccionRepository.findById(ac.seccion_id);
                    Curso c = cursoRepository.findById(ac.curso_id);
                    String docenteNombre = seccion != null ? firstDocenteNombreOrdenadoPorSeccion(seccion.id) : firstDocenteNombreOrdenado(ac.curso_id);
                    String notaAlumnoFinalStr = ac.nota_alumno_final == null ? null
                            : String.format("%02d", ac.nota_alumno_final.intValue());
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
                            ac.id,
                            ac.usuario_id,
                            ac.nota_final,
                            notaAlumnoFinalStr,
                            ac.estado
                    );
                })
                .sorted(Comparator.comparing(r -> r.curso == null ? "" : r.curso))
                .collect(Collectors.toList());
    }

    @Override
    public List<NotasAlumnosResponse> listarNotasAlumnos(Long seccionId, Long alumnoCursoId) {
        List<CriterioEvaluacion> criterios = seccionId == null
                ? java.util.List.of()
                : criterioEvaluacionRepository.listBySeccionId(seccionId);
        if (criterios.isEmpty()) {
            AlumnoCurso ac = alumnoCursoRepository.findById(alumnoCursoId);
            if (ac != null && ac.curso_id != null) {
                criterios = criterioEvaluacionRepository.listByCursoId(ac.curso_id);
            }
        }
        return criterios.stream().map(ce -> {
            Nota n = notaRepository.findByAlumnoCursoAndCriterio(alumnoCursoId, ce.id);
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

    private String firstDocenteNombreOrdenado(Long cursoId) {
        List<DocenteCurso> dcs = docenteCursoRepository.listByCursoId(cursoId);
        return dcs.stream()
                .filter(dc -> Boolean.TRUE.equals(dc.activo))
                .map(dc -> usuarioRepository.findById(dc.usuario_id))
                .filter(Objects::nonNull)
                .map(u -> (u.nombre + " " + u.paterno + " " + u.materno).trim())
                .sorted()
                .findFirst().orElse(null);
    }

    private String firstDocenteNombreOrdenadoPorSeccion(Long seccionId) {
        List<DocenteCurso> dcs = docenteCursoRepository.listBySeccionId(seccionId);
        return dcs.stream()
                .filter(dc -> Boolean.TRUE.equals(dc.activo))
                .map(dc -> usuarioRepository.findById(dc.usuario_id))
                .filter(Objects::nonNull)
                .map(u -> (u.nombre + " " + u.paterno + " " + u.materno).trim())
                .sorted()
                .findFirst().orElse(null);
    }

    @Override
    public CursoAlumnoResponse matricularEnSeccion(Long usuarioId, Long seccionId) {
        Usuario alumno = usuarioRepository.findById(usuarioId);
        Seccion seccion = seccionRepository.findById(seccionId);
        if (alumno == null || seccion == null) return null;
        // evitar duplicado
        boolean yaInscrito = alumnoCursoRepository.listByUsuarioId(usuarioId).stream()
                .anyMatch(ac -> ac.seccion_id != null && ac.seccion_id.equals(seccionId));
        if (yaInscrito) return null;

        AlumnoCurso nuevo = new AlumnoCurso(null, usuarioId, seccion.curso_id, seccionId, "E", null, null, null, true);
        alumnoCursoRepository.save(nuevo);

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
                nuevo.id,
                nuevo.usuario_id,
                null,
                null,
                "E"
        );
    }

    @Override
    public List<CursoAlumnoResponse> listarSeccionesDisponibles(Long usuarioId) {
        List<Long> inscritos = alumnoCursoRepository.listByUsuarioId(usuarioId).stream()
                .map(ac -> ac.seccion_id).filter(Objects::nonNull).collect(Collectors.toList());
        return seccionRepository.findAll().stream()
                .filter(s -> s.id != null && !inscritos.contains(s.id))
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
