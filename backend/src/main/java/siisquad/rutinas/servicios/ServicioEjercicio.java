package siisquad.rutinas.servicios;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import siisquad.rutinas.entities.Ejercicio;

import java.util.List;

@Service
@Transactional
public class ServicioEjercicio {

    public Ejercicio obtenerEjercicio(Long id){
        return null;
    }

    public Long aniadirEjercicio(Long idEntrenador, Ejercicio ejercicio){
        return null;
    }


    public void eliminarEjercicio(Long id){

    }

    public void actualizarEjercicio(Ejercicio ejercicio) {

    }

    public List<Ejercicio> obtenerEjerciciosPorEntrenador(Long id){
        return null;
    }


}
