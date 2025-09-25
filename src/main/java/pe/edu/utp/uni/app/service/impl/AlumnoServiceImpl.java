package pe.edu.utp.uni.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.uni.app.model.Curso;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.model.relationship.AlumnoCurso;
import pe.edu.utp.uni.app.model.relationship.DocenteCurso;
import pe.edu.utp.uni.app.response.CursoAlumnoResponse;
import pe.edu.utp.uni.app.service.AlumnoService;
import pe.edu.utp.uni.app.repository.AlumnoCursoRepository;
import pe.edu.utp.uni.app.repository.CursoRepository;
import pe.edu.utp.uni.app.repository.DocenteCursoRepository;
import pe.edu.utp.uni.app.repository.UsuarioRepository;
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


    @Override
    public List<CursoAlumnoResponse> listarCursosPorUsuario(Long usuarioId) {
        List<AlumnoCurso> acs = alumnoCursoRepository.listByUsuarioId(usuarioId);
        Usuario alumno = usuarioRepository.findById(usuarioId);
        String alumnoNombre = alumno == null ? null : (alumno.nombre + " " + alumno.paterno + " " + alumno.materno).trim();

        return acs.stream()
                .filter(ac -> Boolean.TRUE.equals(ac.activo))
                .map(ac -> {
                    Curso c = cursoRepository.findById(ac.curso_id);
                    String docenteNombre = firstDocenteNombreOrdenado(ac.curso_id);
                    String notaAlumnoFinalStr = ac.nota_alumno_final == null ? null
                            : String.format("%02d", ac.nota_alumno_final.intValue());
                    return new CursoAlumnoResponse(
                            c == null ? null : c.id,
                            c == null ? null : c.nombre,
                            c == null ? null : c.horas_semanales,
                            c == null ? null : c.creditos,
                            c == null ? null : c.modalidad,
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
}
