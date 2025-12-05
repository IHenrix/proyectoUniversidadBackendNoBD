package pe.edu.utp.uni.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.utp.uni.app.model.*;
import pe.edu.utp.uni.app.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * DataLoader - Carga de datos de prueba
 *
 * Demuestra el uso de TODAS las estructuras de datos del proyecto:
 * - AVLTree (en MatriculaRepository, UsuarioRepository)
 * - DoublyLinkedList (en CicloRepository)
 * - SinglyLinkedList (en MatriculaRepository, SeccionRepository)
 * - CircularSinglyLinkedList (en DocenteSeccionRepository)
 * - Queue, PriorityQueue, SimpleStack, MinHeap
 *
 * Según sílabo de Algoritmos y Estructuras de Datos
 */
@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initData(
            RolRepository rolRepo,
            CursoRepository cursoRepo,
            UsuarioRepository userRepo,
            CicloRepository cicloRepo,                     // ⭐ NUEVO
            SeccionRepository seccionRepo,
            MatriculaRepository matriculaRepo,             // ⭐ NUEVO (reemplaza AlumnoCursoRepo)
            DocenteSeccionRepository docenteSeccionRepo,   // ⭐ NUEVO
            CriterioEvaluacionRepository criterioRepo,
            NotaRepository notaRepo
    ) {
        return args -> {
            if (!cursoRepo.findAll().isEmpty()) return;

            System.out.println("\n=== INICIANDO CARGA DE DATOS ===");
            System.out.println("Demostrando estructuras de datos personalizadas del sílabo\n");

            // ==================== ROLES ====================
            System.out.println("📋 Creando Roles...");
            Rol rolAlumno = rolRepo.save(new Rol(null, "ALUMNO"));
            Rol rolProfesor = rolRepo.save(new Rol(null, "PROFESOR"));
            Rol rolAdmin = rolRepo.save(new Rol(null, "ADMIN"));

            // ==================== USUARIOS ====================
            System.out.println("👥 Creando Usuarios (usando AVLTree en UsuarioRepository)...");

            // Profesores
            userRepo.guardarUsuario(new Usuario(null, "RPRADA", "Marco1415", "RICARDO ENRIQUE", "PRADA", "GUERRA", "M", "rprada@hotmail.com", "", rolProfesor.id, true));
            userRepo.guardarUsuario(new Usuario(null, "JCARLOS", "Marco1415", "JUAN CARLOS", "RAMIREZ", "LOPEZ", "M", "jcarlos@hotmail.com", "", rolProfesor.id, true));

            // Alumnos
            userRepo.guardarUsuario(new Usuario(null, "JMORALES", "Marco1415", "JUAN JOSE", "MORALES", "VELASQUEZ", "M", "jmorales@hotmail.com", "U23316357", rolAlumno.id, true));
            userRepo.guardarUsuario(new Usuario(null, "NLOPEZO", "Marco1415", "NIKOL", "LOPEZ", "OCHOA", "F", "nlopezo@hotmail.com", "U23316358", rolAlumno.id, true));
            userRepo.guardarUsuario(new Usuario(null, "ACASTILLO", "Marco1415", "ANGEL", "CASTILLO", "PEREZ", "M", "acastillo@hotmail.com", "U23316359", rolAlumno.id, true));

            // Admin
            userRepo.guardarUsuario(new Usuario(null, "PYARLEQUE", "Marco1415", "PEDRO LUIS SANTOS", "YARLEQUE", "LINARES", "M", "pyarleque@hotmail.com", "", rolAdmin.id, true));

            Usuario profesor1 = userRepo.buscarPorUsuario("RPRADA");
            Usuario profesor2 = userRepo.buscarPorUsuario("JCARLOS");
            Usuario alumno1 = userRepo.buscarPorUsuario("JMORALES");
            Usuario alumno2 = userRepo.buscarPorUsuario("NLOPEZO");
            Usuario alumno3 = userRepo.buscarPorUsuario("ACASTILLO");

            // ==================== CICLOS ====================
            System.out.println("📅 Creando Ciclos (usando DoublyLinkedList en CicloRepository)...");

            Ciclo ciclo2025_1 = cicloRepo.save(new Ciclo(
                    null,
                    "2025-1",
                    LocalDate.of(2025, 3, 1),
                    LocalDate.of(2025, 7, 31),
                    LocalDate.of(2025, 1, 15),
                    LocalDate.of(2025, 2, 28),
                    false,  // matrícula cerrada
                    true    // ciclo terminado
            ));

            Ciclo ciclo2025_2 = cicloRepo.save(new Ciclo(
                    null,
                    "2025-2",
                    LocalDate.of(2025, 8, 1),
                    LocalDate.of(2025, 12, 31),
                    LocalDate.of(2025, 6, 15),
                    LocalDate.of(2025, 7, 31),
                    true,   // matrícula abierta
                    false   // ciclo activo
            ));

            // ==================== CURSOS ====================
            System.out.println("📚 Creando Cursos...");
            Curso c1 = cursoRepo.save(new Curso(null, "Redes y comunicación de datos I", 4.0, 4.0, "P"));
            Curso c2 = cursoRepo.save(new Curso(null, "Algoritmos y estructuras de datos", 4.0, 3.0, "P"));
            Curso c3 = cursoRepo.save(new Curso(null, "Taller de programación web", 3.0, 2.0, "R"));
            Curso c4 = cursoRepo.save(new Curso(null, "Base de datos II", 4.0, 4.0, "P"));
            Curso c5 = cursoRepo.save(new Curso(null, "Diseño de patrones", 3.0, 2.0, "P"));

            // ==================== SECCIONES ====================
            System.out.println("🏫 Creando Secciones (con control de vacantes)...");

            Seccion s1 = seccionRepo.save(new Seccion(
                    null, "11366", c1.id, ciclo2025_2.id, profesor1.id,
                    "P", 1, "A", "tarde",
                    List.of(new Horario("Lunes", "20:15", "21:45"), new Horario("Miércoles", "20:15", "21:45")),
                    30  // 30 vacantes
            ));

            Seccion s2 = seccionRepo.save(new Seccion(
                    null, "16305", c2.id, ciclo2025_2.id, profesor1.id,
                    "P", 1, "A", "tarde",
                    List.of(new Horario("Martes", "20:15", "21:45"), new Horario("Jueves", "20:15", "21:45")),
                    25  // 25 vacantes
            ));

            Seccion s3 = seccionRepo.save(new Seccion(
                    null, "28531", c3.id, ciclo2025_2.id, profesor2.id,
                    "R", 1, "A", "mañana",
                    List.of(new Horario("Jueves", "11:00", "13:15")),
                    40  // 40 vacantes
            ));

            Seccion s4 = seccionRepo.save(new Seccion(
                    null, "16307", c4.id, ciclo2025_2.id, profesor1.id,
                    "P", 1, "A", "tarde",
                    List.of(new Horario("Miércoles", "18:30", "20:00"), new Horario("Jueves", "18:30", "20:00")),
                    35  // 35 vacantes
            ));

            Seccion s5 = seccionRepo.save(new Seccion(
                    null, "16309", c5.id, ciclo2025_2.id, profesor2.id,
                    "P", 1, "A", "tarde",
                    List.of(new Horario("Sábado", "15:45", "18:00")),
                    20  // 20 vacantes
            ));

            // ==================== DOCENTE-SECCION ====================
            System.out.println("👨‍🏫 Asignando Profesores a Secciones (usando CircularSinglyLinkedList)...");

            // Sección 1: Solo profesor principal
            docenteSeccionRepo.save(new DocenteSeccion(null, profesor1.id, s1.id, "principal"));

            // Sección 2: Profesor principal + auxiliar
            docenteSeccionRepo.save(new DocenteSeccion(null, profesor1.id, s2.id, "principal"));
            docenteSeccionRepo.save(new DocenteSeccion(null, profesor2.id, s2.id, "auxiliar"));

            // Sección 3: Solo profesor principal
            docenteSeccionRepo.save(new DocenteSeccion(null, profesor2.id, s3.id, "principal"));

            // Sección 4: Profesor principal
            docenteSeccionRepo.save(new DocenteSeccion(null, profesor1.id, s4.id, "principal"));

            // Sección 5: Profesor principal + auxiliar
            docenteSeccionRepo.save(new DocenteSeccion(null, profesor2.id, s5.id, "principal"));
            docenteSeccionRepo.save(new DocenteSeccion(null, profesor1.id, s5.id, "auxiliar"));

            // ==================== MATRÍCULAS ====================
            System.out.println("✍️ Registrando Matrículas (usando AVLTree + SinglyLinkedList)...");

            // Alumno 1: Matriculado en 4 cursos
            Matricula m1 = matriculaRepo.save(new Matricula(null, alumno1.id, s1.id));
            Matricula m2 = matriculaRepo.save(new Matricula(null, alumno1.id, s2.id));
            Matricula m3 = matriculaRepo.save(new Matricula(null, alumno1.id, s3.id));
            Matricula m4 = matriculaRepo.save(new Matricula(null, alumno1.id, s4.id));

            // Alumno 2: Matriculado en 3 cursos
            Matricula m5 = matriculaRepo.save(new Matricula(null, alumno2.id, s1.id));
            Matricula m6 = matriculaRepo.save(new Matricula(null, alumno2.id, s2.id));
            Matricula m7 = matriculaRepo.save(new Matricula(null, alumno2.id, s5.id));

            // Alumno 3: Matriculado en 2 cursos
            Matricula m8 = matriculaRepo.save(new Matricula(null, alumno3.id, s2.id));
            Matricula m9 = matriculaRepo.save(new Matricula(null, alumno3.id, s3.id));

            // Actualizar vacantes ocupadas
            s1.ocuparVacante(); s1.ocuparVacante();  // 2 alumnos
            s2.ocuparVacante(); s2.ocuparVacante(); s2.ocuparVacante();  // 3 alumnos
            s3.ocuparVacante(); s3.ocuparVacante();  // 2 alumnos
            s4.ocuparVacante();  // 1 alumno
            s5.ocuparVacante();  // 1 alumno

            // ==================== CRITERIOS DE EVALUACIÓN ====================
            System.out.println("📝 Creando Criterios de Evaluación...");

            // Redes y comunicación de datos I
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, s1.id, "Práctica calificada 1 (PC1)", 1, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, s1.id, "Práctica calificada 2 (PC2)", 2, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, s1.id, "Práctica calificada 3 (PC3)", 3, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c1.id, s1.id, "Examen final (EXFN)", 4, 40.0));

            // Algoritmos y estructuras de datos
            CriterioEvaluacion crit1 = criterioRepo.save(new CriterioEvaluacion(null, c2.id, s2.id, "Práctica calificada 1 (PC1)", 1, 20.0));
            CriterioEvaluacion crit2 = criterioRepo.save(new CriterioEvaluacion(null, c2.id, s2.id, "Práctica calificada 2 (PC2)", 2, 20.0));
            CriterioEvaluacion crit3 = criterioRepo.save(new CriterioEvaluacion(null, c2.id, s2.id, "Práctica calificada 3 (PC3)", 3, 20.0));
            CriterioEvaluacion crit4 = criterioRepo.save(new CriterioEvaluacion(null, c2.id, s2.id, "Proyecto final (PROY)", 4, 40.0));

            // Taller de programación web
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, s3.id, "Avance de proyecto 1 (APF1)", 1, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, s3.id, "Avance de proyecto 2 (APF2)", 2, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, s3.id, "Avance de proyecto 3 (APF3)", 3, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c3.id, s3.id, "Proyecto final (PROY)", 4, 40.0));

            // Base de datos II
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, s4.id, "Práctica calificada 1 (PC1)", 1, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, s4.id, "Práctica calificada 2 (PC2)", 2, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, s4.id, "Práctica calificada 3 (PC3)", 3, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c4.id, s4.id, "Trabajo final (TF)", 4, 40.0));

            // Diseño de patrones
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, s5.id, "Práctica calificada 1 (PC1)", 1, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, s5.id, "Práctica calificada 2 (PC2)", 2, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, s5.id, "Práctica calificada 3 (PC3)", 3, 20.0));
            criterioRepo.save(new CriterioEvaluacion(null, c5.id, s5.id, "Proyecto final (PROY)", 4, 40.0));

            // ==================== NOTAS DE EJEMPLO ====================
            System.out.println("📊 Registrando Notas de ejemplo...");

            // Notas para alumno1 en Algoritmos (m2)
            notaRepo.save(new Nota(null, m2.id, crit1.id, 18.0, 18.0));
            notaRepo.save(new Nota(null, m2.id, crit2.id, 16.0, 16.0));
            notaRepo.save(new Nota(null, m2.id, crit3.id, 17.0, 17.0));
            notaRepo.save(new Nota(null, m2.id, crit4.id, 19.0, 19.0));

            System.out.println("\n=== CARGA DE DATOS COMPLETADA ===");
            System.out.println("\n📊 RESUMEN:");
            System.out.println("   - Ciclos: " + cicloRepo.findAll().size() + " (usando DoublyLinkedList)");
            System.out.println("   - Cursos: " + cursoRepo.findAll().size());
            System.out.println("   - Secciones: " + seccionRepo.findAll().size() + " (con control de vacantes)");
            System.out.println("   - Matrículas: " + matriculaRepo.findAll().size() + " (usando AVLTree + SinglyLinkedList)");
            System.out.println("   - Docentes-Secciones: " + docenteSeccionRepo.findAll().size() + " (usando CircularSinglyLinkedList)");
            System.out.println("   - Usuarios: " + userRepo.findAll().size() + " (usando AVLTree)");
            System.out.println("\n✅ Sistema listo para usar!\n");
        };
    }
}
