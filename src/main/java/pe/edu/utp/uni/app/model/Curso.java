package pe.edu.utp.uni.app.model;

public class Curso {
    public Long id;
    public String nombre;
    public Double horas_semanales;
    public Double creditos;
    public String modalidad;

    public Curso() {}
    public Curso(Long id, String nombre, Double horas_semanales, Double creditos, String modalidad) {
        this.id = id; this.nombre = nombre; this.horas_semanales = horas_semanales; this.creditos = creditos; this.modalidad = modalidad;
    }
}
