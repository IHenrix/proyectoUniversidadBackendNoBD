package pe.edu.utp.uni.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.edu.utp.uni.app.model.Curso;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.model.relationship.AlumnoCurso;
import pe.edu.utp.uni.app.model.relationship.DocenteCurso;
import pe.edu.utp.uni.app.repository.UsuarioRepository;
import pe.edu.utp.uni.app.response.AlumnoListaCursoResponse;
import pe.edu.utp.uni.app.response.CursoDocenteResponse;
import pe.edu.utp.uni.app.service.DocenteService;
import pe.edu.utp.uni.app.repository.AlumnoCursoRepository;
import pe.edu.utp.uni.app.repository.CursoRepository;
import pe.edu.utp.uni.app.repository.DocenteCursoRepository;
import pe.edu.utp.uni.app.struct.MinHeap;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

@Repository
@RequiredArgsConstructor
public class DocenteServiceImpl implements DocenteService {
    private final DocenteCursoRepository docenteCursoRepository;
    private final CursoRepository cursoRepository;
    private final AlumnoCursoRepository alumnoCursoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<CursoDocenteResponse> listarCursosDeDocente(Long docenteId) {
        List<DocenteCurso> asignaciones = docenteCursoRepository.listByDocenteId(docenteId);
        List<CursoDocenteResponse> out = new ArrayList<>();
        for (DocenteCurso dc : asignaciones) {
            if (dc.activo == null || !dc.activo) continue;
            Curso c = cursoRepository.findById(dc.curso_id);
            if (c == null) continue;
            int alumnos = 0;
            for (AlumnoCurso ac : alumnoCursoRepository.listByCursoId(c.id)) {
                if (Boolean.TRUE.equals(ac.activo)) alumnos++;
            }
            out.add(new CursoDocenteResponse(c.id, c.nombre, c.horas_semanales, c.creditos, c.modalidad, alumnos));
        }
        out.sort(Comparator.comparing(a -> a.curso == null ? "" : a.curso));
        return out;
    }

    @Override
    public List<AlumnoListaCursoResponse> listarAlumnosPorCurso(Long cursoId) {
        List<AlumnoCurso> inscripciones = alumnoCursoRepository.listByCursoId(cursoId);

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
        while(!heap.isEmpty()) out.add(heap.pop());
        return out;
    }

}
