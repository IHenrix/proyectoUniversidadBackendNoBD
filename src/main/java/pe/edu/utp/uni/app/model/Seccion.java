package pe.edu.utp.uni.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Seccion - Representa una sección de un curso en un ciclo específico
 *
 * Object Composition: Sección compone curso, ciclo y profesores (relaciones)
 * Information Expert: Conoce su capacidad, vacantes, horarios
 */
public class Seccion {
    public Long id;
    public String codigo;
    public Long curso_id;
    public Long ciclo_id;              // ⭐ NUEVO: FK a Ciclo
    public Long docente_id;            // Docente principal (mantener por compatibilidad)
    public String modalidad;           // P: presencial, V: virtual, R: remota
    public Integer numero_vez;
    public String estado;
    public String turno;               // ⭐ NUEVO: "mañana", "tarde", "noche"
    public List<Horario> horarios = new ArrayList<>();

    // ⭐ NUEVO: Control de vacantes
    public Integer vacantesTotales;
    public Integer vacantesOcupadas;

    // ⭐ NUEVO: Timestamps
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Seccion() {
        this.vacantesOcupadas = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Seccion(Long id, String codigo, Long curso_id, Long ciclo_id, Long docente_id,
                   String modalidad, Integer numero_vez, String estado, String turno,
                   List<Horario> horarios, Integer vacantesTotales) {
        this.id = id;
        this.codigo = codigo;
        this.curso_id = curso_id;
        this.ciclo_id = ciclo_id;
        this.docente_id = docente_id;
        this.modalidad = modalidad;
        this.numero_vez = numero_vez;
        this.estado = estado;
        this.turno = turno;
        if (horarios != null) this.horarios = horarios;
        this.vacantesTotales = vacantesTotales;
        this.vacantesOcupadas = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Information Expert: La sección conoce sus vacantes disponibles
     */
    public Integer getVacantesDisponibles() {
        return vacantesTotales - vacantesOcupadas;
    }

    /**
     * Strategy Pattern: Validación de vacantes
     */
    public boolean tieneVacantes() {
        return getVacantesDisponibles() > 0;
    }

    /**
     * Alerta de pocas vacantes (≤5)
     */
    public boolean pocasVacantes() {
        int disponibles = getVacantesDisponibles();
        return disponibles > 0 && disponibles <= 5;
    }

    /**
     * Alerta de última vacante
     */
    public boolean ultimaVacante() {
        return getVacantesDisponibles() == 1;
    }

    /**
     * Ocupa una vacante (llamado al matricular)
     */
    public void ocuparVacante() {
        if (tieneVacantes()) {
            this.vacantesOcupadas++;
            this.updatedAt = LocalDateTime.now();
        }
    }

    /**
     * Libera una vacante (llamado al desmatricular)
     */
    public void liberarVacante() {
        if (this.vacantesOcupadas > 0) {
            this.vacantesOcupadas--;
            this.updatedAt = LocalDateTime.now();
        }
    }
}
