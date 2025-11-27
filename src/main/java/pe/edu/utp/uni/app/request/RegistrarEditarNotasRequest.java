package pe.edu.utp.uni.app.request;

import java.util.List;

public class RegistrarEditarNotasRequest {
    public Long alumnoCursoId;
    public Long seccionId;
    public List<NotaItem> notas;

    public RegistrarEditarNotasRequest() {}
}
