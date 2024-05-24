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
import siisquad.rutinas.dtos.RutinaDTO;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@Service
public class ServicioEntrena {

    @Value(value = "${local.server.port}")
    private int port;

    private final String host = "localhost";

    private final RestTemplate restTemplate;

    @Autowired
    public ServicioEntrena(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    private RequestEntity<Void> get(String scheme, String host, int port, String path, String token) {
        var peticion = RequestEntity.get(URI.create(scheme+"://"+host+":"+port+path))
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
                .build();
        return peticion;
    }

    public Long getEntrenadorDeCliente(Long idCliente, String token)
    {
        var peticion = RequestEntity.get("http://localhost:"+port+"/entrena?cliente="+idCliente).build();
        var respuesta = restTemplate.exchange(peticion,
                new ParameterizedTypeReference<Long>() {});
        return respuesta.getBody();
    }




}
