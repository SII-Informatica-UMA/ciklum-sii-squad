package siisquad.rutinas;

import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import siisquad.rutinas.security.JwtUtil;

@SpringBootApplication
public class RutinasApplication {

	public static void main(String[] args) {
		SpringApplication.run(RutinasApplication.class, args);
	}

	public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Introduce un comando:");
            String comando = scanner.nextLine();
            // Procesa el comando
            if ("salir".equals(comando)) {
                break;
            } else {
                // Aquí puedes llamar a los métodos de tu servicio para interactuar con tu base de datos
                // Por ejemplo: tuServicio.ejecutarComando(comando);
            }
        }
        scanner.close();
    }

}
