package pe.edu.utp.uni.app.model;

import java.util.ArrayList;
import java.util.List;

public class Seccion {
    public Long id;
    public String codigo;
    public Long curso_id;
    public Long docente_id;
    public String modalidad; // P: presencial, V: virtual, R: remota
    public Integer numero_vez;
    public String estado;
    public List<Horario> horarios = new ArrayList<>();

    public Seccion() {}

    public Seccion(Long id, String codigo, Long curso_id, Long docente_id, String modalidad, Integer numero_vez, String estado, List<Horario> horarios) {
        this.id = id;
        this.codigo = codigo;
        this.curso_id = curso_id;
        this.docente_id = docente_id;
        this.modalidad = modalidad;
        this.numero_vez = numero_vez;
        this.estado = estado;
        if (horarios != null) this.horarios = horarios;
    }
}
