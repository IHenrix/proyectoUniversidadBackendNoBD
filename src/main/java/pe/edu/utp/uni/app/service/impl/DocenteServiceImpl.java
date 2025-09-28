package pe.edu.utp.uni.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.edu.utp.uni.app.model.Curso;
import pe.edu.utp.uni.app.model.relationship.AlumnoCurso;
import pe.edu.utp.uni.app.model.relationship.DocenteCurso;
import pe.edu.utp.uni.app.response.CursoDocenteResponse;
import pe.edu.utp.uni.app.service.DocenteService;
import pe.edu.utp.uni.app.repository.AlumnoCursoRepository;
import pe.edu.utp.uni.app.repository.CursoRepository;
import pe.edu.utp.uni.app.repository.DocenteCursoRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

@Repository
@RequiredArgsConstructor
public class DocenteServiceImpl implements DocenteService {
    private final DocenteCursoRepository docenteCursoRepository;
    private final CursoRepository cursoRepository;
    private final AlumnoCursoRepository alumnoCursoRepository;

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
}
