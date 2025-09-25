package pe.edu.utp.uni.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.edu.utp.uni.app.model.Rol;
import pe.edu.utp.uni.app.model.Usuario;
import pe.edu.utp.uni.app.repository.RolRepository;
import pe.edu.utp.uni.app.repository.UsuarioRepository;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initData(RolRepository rolRepo, UsuarioRepository userRepo) {
        return args -> {
            rolRepo.save(new Rol(null, "ALUMNO"));
            rolRepo.save(new Rol(null, "PROFESOR"));
            userRepo.save(new Usuario(null, "RPRADA", "Pedro1415", "RICARDO ENRIQUE", "PRADA", "GUERRA", "M", "rprada@hotmail.com", 2, true));
            userRepo.save(new Usuario(null, "JMORALES", "Marco1415", "JUAN JOSÉ", "MORALES", "VELASQUEZ", "M", "jmorales@hotmail.com", 1, true));
            userRepo.save(new Usuario(null, "NLOPEZO", "Marco1415", "NIKOL", "LOPEZ", "OCHOA", "F", "nlopezo@hotmail.com", 1, true));
        };
    }
}