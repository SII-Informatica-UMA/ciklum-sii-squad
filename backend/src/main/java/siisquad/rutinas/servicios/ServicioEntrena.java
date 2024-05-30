package siisquad.rutinas.servicios;

import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import siisquad.rutinas.dtos.AsignacionEntrenamientoDTO;
import siisquad.rutinas.dtos.RutinaDTO;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ServicioEntrena {

    private final int port=9001;

    private final String host = "localhost";

    private final RestTemplate restTemplate;

    @Autowired
    public ServicioEntrena(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene la asignacion del cliente idCliente con su entrenador si existe
     * @param idCliente
     * @param token supuestamente viene ya con Bearer
     * @return
     */
    public Optional<List<AsignacionEntrenamientoDTO>> getEntrenaPorCliente(Long idCliente, String token)
    {
        // Define las cabeceras de la solicitud y añade el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        var peticion = RequestEntity.get("http://localhost:"+port+"/entrena?cliente="+idCliente.intValue()).headers(headers).build();
        var respuesta = restTemplate.exchange(peticion,
                new ParameterizedTypeReference<List<AsignacionEntrenamientoDTO>>() {});
        return Optional.ofNullable(respuesta.getBody());
    }




}
