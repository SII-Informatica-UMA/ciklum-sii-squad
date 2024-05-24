package siisquad.rutinas.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GestionEntrenamientoService {
    @Bean
    public static RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Autowired
    private RestTemplate restTemplate;

    public boolean comprubaEntrenaCliente(Long idEntrenador, Long idCliente){
        ResponseEntity<List> resp =
                restTemplate.getForEntity("http://localhost:8080/entrena?cliente=" + idCliente + "&entrenador=" + idEntrenador, List.class);
        return resp.hasBody() && resp.getBody() != null && !((List)resp.getBody()).isEmpty();
    }
}
