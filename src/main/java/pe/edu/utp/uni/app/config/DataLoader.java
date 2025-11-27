package pe.edu.utp.uni.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.utp.uni.app.model.*;
import pe.edu.utp.uni.app.model.relationship.AlumnoCurso;
import pe.edu.utp.uni.app.model.relationship.DocenteCurso;
import pe.edu.utp.uni.app.repository.*;

import java.util.List;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initData(RolRepository rolRepo, CursoRepository cursoRepo,
                               UsuarioRepository userRepo,
                               DocenteCursoRepository docenteCursoRepo,
                               AlumnoCursoRepository alumnoCursoRepo,
                               CriterioEvaluacionRepository criterioRepo,
                               SeccionRepository seccionRepo) {
        return args -> {
            if (!cursoRepo.findAll().isEmpty()) return;

            rolRepo.save(new Rol(null, "ALUMNO"));
            rolRepo.save(new Rol(null, "PROFESOR"));

            userRepo.guardarUsuario(new Usuario(null, "RPRADA", "Marco1415", "RICARDO ENRIQUE", "PRADA", "GUERRA", "M", "rprada@hotmail.com","", 2L, true));
            userRepo.guardarUsuario(new Usuario(null, "JMORALES", "Marco1415", "JUAN JOSE", "MORALES", "VELASQUEZ", "M", "jmorales@hotmail.com","U23316357", 1L, true));
            userRepo.guardarUsuario(new Usuario(null, "NLOPEZO", "Marco1415", "NIKOL", "LOPEZ", "OCHOA", "F", "nlopezo@hotmail.com","U23316358", 1L, true));

            // Cursos
            Curso c1 = cursoRepo.save(new Curso(null, "Redes y comunicación de datos I (1I41N)", 4.0, 4.0, "P"));
            Curso c2 = cursoRepo.save(new Curso(null, "Algoritmos y estructuras de datos (1I53N)", 4.0, 3.0, "P"));
            Curso c3 = cursoRepo.save(new Curso(null, "Taller de programación web (1SI45)", 3.0, 2.0, "R")); // virtual en vivo
            Curso c4 = cursoRepo.save(new Curso(null, "Base de datos II (1SI46)", 4.0, 4.0, "P"));
            Curso c5 = cursoRepo.save(new Curso(null, "Diseño de patrones (1SI47)", 3.0, 2.0, "P"));
            Curso c6 = cursoRepo.save(new Curso(null, "Negociación y narrativa (1S76T)", 2.0, 2.0, "R")); // virtual en vivo
            Curso c7 = cursoRepo.save(new Curso(null, "Sistemas operativos (1TV74)", 4.0, 3.0, "V")); // virtual 24/7

            Usuario profesor = userRepo.buscarPorUsuario("RPRADA");
            Usuario alumno1 = userRepo.buscarPorUsuario("JMORALES");
            Usuario alumno2 = userRepo.buscarPorUsuario("NLOPEZO");

            // Secciones con horarios y modalidad (todas asignadas a RPRADA)
            Seccion s1 = seccionRepo.save(new Seccion(null, "11366", c1.id, profesor.id, "P", 1, "E",
                    List.of(
                            new Horario("Lunes", "20:15", "21:45"),
                            new Horario("Miércoles", "20:15", "21:45")
                    )));
            Seccion s2 = seccionRepo.save(new Seccion(null, "16305", c2.id, profesor.id, "P", 1, "E",
                    List.of(
                            new Horario("Martes", "20:15", "21:45"),
                            new Horario("Jueves", "20:15", "21:45")
                    )));
            Seccion s3 = seccionRepo.save(new Seccion(null, "28531", c3.id, profesor.id, "R", 1, "E",
                    List.of(
                            new Horario("Jueves", "11:00", "13:15")
                    )));
            Seccion s4 = seccionRepo.save(new Seccion(null, "16307", c4.id, profesor.id, "P", 1, "E",
                    List.of(
                            new Horario("Miércoles", "18:30", "20:00"),
                            new Horario("Jueves", "18:30", "20:00")
                    )));
            Seccion s5 = seccionRepo.save(new Seccion(null, "16309", c5.id, profesor.id, "P", 1, "E",
                    List.of(
                            new Horario("Sábado", "15:45", "18:00")
                    )));
            Seccion s6 = seccionRepo.save(new Seccion(null, "28977", c6.id, profesor.id, "R", 1, "E",
                    List.of(
                            new Horario("Viernes", "20:15", "21:45")
                    )));
            Seccion s7 = seccionRepo.save(new Seccion(null, "38377", c7.id, profesor.id, "V", 1, "E",
                    List.of(
                            new Horario("Disponible", "24/7", "UTP+ Class")
                    )));

            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c1.id, s1.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c2.id, s2.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c3.id, s3.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c4.id, s4.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c5.id, s5.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c6.id, s6.id, true));
            docenteCursoRepo.save(new DocenteCurso(null, profesor.id, c7.id, s7.id, true));

            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c1.id, s1.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c2.id, s2.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c3.id, s3.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c4.id, s4.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c5.id, s5.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c6.id, s6.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno1.id, c7.id, s7.id, "E", null, null, null, true));

            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c1.id, s1.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c2.id, s2.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c3.id, s3.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c4.id, s4.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c5.id, s5.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c6.id, s6.id, "E", null, null, null, true));
            alumnoCursoRepo.save(new AlumnoCurso(null, alumno2.id, c7.id, s7.id, "E", null, null, null, true));

            // Criterios por sección
            // Redes y comunicación de datos I
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, s1.id, "Práctica calificada 1 (PC1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, s1.id, "Práctica calificada 2 (PC2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, s1.id, "Práctica calificada 3 (PC3)", 3, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, s1.id, "Participación en clase (PA)", 4, 10.00));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, s1.id, "Examen final (EXFN)", 5, 30.00));

            // Algoritmos y estructuras de datos
            criterioRepo.save(new CriterioEvaluacion(null, c2.id, s2.id, "Práctica calificada 1 (PC1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c2.id, s2.id, "Práctica calificada 2 (PC2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c2.id, s2.id, "Práctica calificada 3 (PC3)", 3, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c2.id, s2.id, "Proyecto final (PROY)", 4, 40.00));

            // Taller de programación web
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, s3.id, "Avance de proyecto final 1 (APF1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, s3.id, "Avance de proyecto final 2 (APF2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, s3.id, "Avance de proyecto final 3 (APF3)", 3, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, s3.id, "Proyecto final (PROY)", 4, 40.00));

            // Base de datos II
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, s4.id, "Práctica calificada 1 (PC1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, s4.id, "Práctica calificada 2 (PC2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, s4.id, "Práctica calificada 3 (PC3)", 3, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, s4.id, "Trabajo final (TF)", 4, 40.00));

            // Diseño de patrones
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, s5.id, "Práctica calificada 1 (PC1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, s5.id, "Práctica calificada 2 (PC2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, s5.id, "Práctica calificada 3 (PC3)", 3, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, s5.id, "Proyecto final (PROY)", 4, 40.00));

            // Negociación y narrativa
            criterioRepo.save(new CriterioEvaluacion(null, c6.id, s6.id, "Tarea académica 1 (TA1)", 1, 30.00));
            criterioRepo.save(new CriterioEvaluacion(null, c6.id, s6.id, "Tarea académica 2 (TA2)", 2, 30.00));
            criterioRepo.save(new CriterioEvaluacion(null, c6.id, s6.id, "Examen final (EXFN)", 3, 40.00));

            // Sistemas operativos
            criterioRepo.save(new CriterioEvaluacion(null, c7.id, s7.id, "Práctica calificada 1 (PC1)", 1, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c7.id, s7.id, "Práctica calificada 2 (PC2)", 2, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c7.id, s7.id, "Práctica calificada 3 (PC3)", 3, 20.00));
            criterioRepo.save(new CriterioEvaluacion(null, c7.id, s7.id, "Participación en clase (PA)", 4, 10.00));
            criterioRepo.save(new CriterioEvaluacion(null, c7.id, s7.id, "Examen final (EXFN)", 5, 30.00));
        };
    }
}
