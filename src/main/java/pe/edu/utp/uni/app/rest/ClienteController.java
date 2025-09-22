package pe.edu.utp.uni.app.rest;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final Map<Integer, String> clientes = new HashMap<>();
    private int currentId = 1;

    @PostMapping
    public String crearCliente(@RequestBody String nombre) {
        clientes.put(currentId++, nombre);
        return "Cliente guardado: " + nombre;
    }
    @GetMapping
    public Map<Integer, String> listarClientes() {
        return clientes;
    }

    @DeleteMapping("/{id}")
    public String eliminarCliente(@PathVariable int id) {
        return clientes.remove(id) != null ? "Eliminado" : "No encontrado";
    }
}