package pe.edu.utp.uni.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.utp.uni.app.model.CriterioEvaluacion;
import pe.edu.utp.uni.app.model.Curso;
import pe.edu.utp.uni.app.model.Rol;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.model.relationship.AlumnoCurso;
import pe.edu.utp.uni.app.model.relationship.DocenteCurso;
import pe.edu.utp.uni.app.repository.*;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initData(RolRepository rolRepo, CursoRepository cursoRepo,
                               UsuarioRepository userRepo,
                               DocenteCursoRepository docenteCursoRepo,
                               AlumnoCursoRepository alumnoCursoRepo,
                               CriterioEvaluacionRepository criterioRepo) {
        return args -> {
            if (!cursoRepo.findAll().isEmpty()) return;

            rolRepo.save(new Rol(null, "ALUMNO"));
            rolRepo.save(new Rol(null, "PROFESOR"));
            userRepo.guardarUsuario(new Usuario(null, "RPRADA", "pedrito", "RICARDO ENRIQUE", "PRADA", "GUERRA", "M", "rprada@hotmail.com","", 2L, true));
            userRepo.guardarUsuario(new Usuario(null, "JMORALES", "Marco1415", "JUAN JOSÉ", "MORALES", "VELASQUEZ", "M", "jmorales@hotmail.com","U23316357", 1L, true));
            userRepo.guardarUsuario(new Usuario(null, "NLOPEZO", "Marco1415", "NIKOL", "LOPEZ", "OCHOA", "F", "nlopezo@hotmail.com","U23316358", 1L, true));

            Curso c1 = cursoRepo.save(new Curso(null, "Taller de programación (1I50N)", 4.0, 3.0, "P"));
            Curso c2 = cursoRepo.save(new Curso(null, "Herramientas informáticas para la toma de decisiones (1I04N)", 2.0, 2.0, "V"));
            Curso c3 = cursoRepo.save(new Curso(null, "Administración y organización de empresas (1I27N)", 3.0, 3.0, "V"));
            Curso c4 = cursoRepo.save(new Curso(null, "Investigación académica (1N02C)", 4.0, 4.0, "V"));
            Curso c5 = cursoRepo.save(new Curso(null, "Estadística descriptiva y probabilidades (1S21V)", 3.0, 3.0, "V"));
            Curso c6 = cursoRepo.save(new Curso(null, "Inglés III (1N08I)", 3.0, 3.0, "V"));

            Usuario profesor = userRepo.buscarPorUsuario("RPRADA");
            Usuario alumno1 = userRepo.buscarPorUsuario("JMORALES");
            Usuario alumno2 = userRepo.buscarPorUsuario("NLOPEZO");
            // relaciona docente con curso
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c1.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c2.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c3.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c4.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c5.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c6.id, true));
            // relaciona alumno con curso
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c1.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c2.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c3.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c4.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c5.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c6.id, "E", null, null, null, true));

            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c1.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c2.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c3.id, "E", null, null, null, true));


            criterioRepo.save(new CriterioEvaluacion(null, c2.id, "Práctica calificada 1 (PC1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c2.id, "Práctica calificada 2 (PC2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c2.id, "Participación en clase (PA)", 3, 30.00));
            criterioRepo.save(new CriterioEvaluacion(null, c2.id, "Examen final individual (EXFI)", 4, 30.00));

            criterioRepo.save(new CriterioEvaluacion(null, c3.id, "Avance de proyecto final 1 (APF1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, "Avance de proyecto final 2 (APF2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, "Avance de proyecto final 3 (APF3)", 3, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, "Participación en clase (PA)", 4, 10.00));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, "Proyecto final (PROY)", 5, 30.00));

            criterioRepo.save(new CriterioEvaluacion(null, c1.id, "Práctica calificada 1 (PC1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, "Práctica calificada 2 (PC2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, "Práctica calificada 3 (PC3)", 3, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, "Participación en clase (PA)", 4, 10.00));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, "Proyecto final (PROY)", 5, 30.00));

            criterioRepo.save(new CriterioEvaluacion(null, c6.id, "Participación en clase (PA)", 4, 35.00));
            criterioRepo.save(new CriterioEvaluacion(null, c6.id, "Proyecto final (PROY)", 5, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c6.id, "Tarea académica 1 (TA1)", 1, 15.00));
            criterioRepo.save(new CriterioEvaluacion(null, c6.id, "Tarea académica 2 (TA2)", 2, 15.00));
            criterioRepo.save(new CriterioEvaluacion(null, c6.id, "Tarea académica 3 (TA3)", 3, 15.00));

            criterioRepo.save(new CriterioEvaluacion(null, c5.id, "Práctica calificada 1 (PC1)", 1, 10.00));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, "Práctica calificada 2 (PC2)", 2, 10.00));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, "Avance de proyecto final (APF)", 3, 15.00));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, "Práctica calificada 3 (PC3)", 4, 15.00));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, "Práctica calificada 4 (PC4)", 5, 15.00));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, "Participación en clase (PA)", 6, 15.00));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, "Proyecto final (PROY)", 7, 20.00));

            criterioRepo.save(new CriterioEvaluacion(null, c4.id, "Avance de informe 1 (AIF1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, "Avance de informe 2 (AIF2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, "Avance de informe 3 (AIF3)", 3, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, "Informe final (IF)", 4, 40.00));


        };
    }
}