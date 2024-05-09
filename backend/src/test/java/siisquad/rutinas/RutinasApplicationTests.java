package siisquad.rutinas;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;


import siisquad.rutinas.entities.*;
import siisquad.rutinas.repositories.*;
import siisquad.rutinas.dtos.*;

//@PropertySource("classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "spring.datasource.url=jdbc:h2:mem:test" })
@DisplayName("Tests api Rest")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RutinasApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	private final String host = "localhost";
	@Autowired
	private RepositorioRutina rutinaRepo;
	@Autowired
	private RepositorioEjercicio ejercicioRepo;

	private final String entrenadorParam = "?entrenador=0";
	private final String rutinaPath= "/rutina";
	private final String ejercicioPath = "/ejercicio";



	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		var peticion = RequestEntity.get(URI.create(scheme+"://"+host+":"+port+path))
			.accept(MediaType.APPLICATION_JSON)
			.build();
		return peticion;
	}
	
	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		var peticion = RequestEntity.delete(URI.create(scheme+"://"+host+":"+port+path))
			.build();
		return peticion;
	}
	
	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		var peticion = RequestEntity.post(URI.create(scheme+"://"+host+":"+port+path))
			.contentType(MediaType.APPLICATION_JSON)
			.body(object);
		return peticion;
	}
	
	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		var peticion = RequestEntity.put(URI.create(scheme+"://"+host+":"+port+path))
			.contentType(MediaType.APPLICATION_JSON)
			.body(object);
		return peticion;
	}


	private String URL_BASE(){
		return  "http://"+host+":"+port;
	}

	@Nested
	@DisplayName("cuando la base de datos está vacía")
	public class BaseDatosVacia {

		@Test
		@DisplayName("devuelve error al acceder a una rutina concreta")
		public void errorRutinaConcreta() {
			var peticion = get("http", host,port, rutinaPath +"/112323");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<RutinaDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al acceder a un ejercicio concreto")
		public void errorEjercicioConcreto() {
			var peticion = get("http", host,port, ejercicioPath + "/1243902834");
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve una lista vacía de rutinas")
		public void devuelveListaVaciaRutinas() {
			var peticion = RequestEntity.get(URI.create(URL_BASE()+"/ejercicio?entrenador=0")).build();

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<RutinaDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			//Lista vacía
			assertThat(respuesta.getBody()).isEqualTo(List.of());
		}

		
		@Test
		@DisplayName("inserta correctamente una rutina")
		public void insertaRutina() {
			// Preparamos la rutina a insertar
			var rutina = Rutina.builder()
									.nombre("Rutina1")
									.build();
			// Preparamos la petición con la rutina dentro
			var peticion = RequestEntity.post(URI.create(URL_BASE()+"/rutina?entrenador=0"))
					.contentType(MediaType.APPLICATION_JSON)
					.body(rutina);
			
			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);
			System.err.println(respuesta);
			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

		
			List<Rutina> rutinasBD = rutinaRepo.findAll();
			assertThat(rutinasBD).hasSize(1);
			assertThat(rutina.getNombre()).isEqualTo(rutinasBD.get(0).getNombre());
		}

		@Test
		@DisplayName("devuelve error al modificar una rutina que no existe")
		public void modificarRutinaInexistente() {
			var ejercicio = EjercicioDTO.builder().nombre("Rutina1").build();
			var peticion = put("http", host,port, "/rutina/2", ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al eliminar una rutina que no existe")
		public void eliminareRutinaInexistente() {
			var peticion = delete("http", host,port, "/rutina/2");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		@Test
		@DisplayName("devuelve una lista vacía de ejercicios")
		public void devuelveListaVaciaEjercicios() {
			var peticion = RequestEntity.get(URI.create(URL_BASE()+ejercicioPath+entrenadorParam)).build();

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<EjercicioDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			//Lista vacía
			assertThat(respuesta.getBody()).isEqualTo(List.of());
		}
		
		@Test
		@DisplayName("inserta correctamente un ejercicio")
		public void insertaEjercicio() {
			// Preparamos el ejercicio a insertar
			var ejercicio = EjercicioNuevoDTO.builder()
					.nombre("Ejercicio2")
					.descripcion("desc")
					.dificultad("dificil")
					.material("ma")
					.tipo("t")
					.musculosTrabajados("m")
					.multimedia(List.of("url1", "url2"))
					.observaciones("obs")
					.build();
			// Preparamos la petición con el ejercicio dentro
			var peticion = RequestEntity.post(URI.create(URL_BASE()+"/ejercicio?entrenador=0"))
					.contentType(MediaType.APPLICATION_JSON)
					.body(ejercicio);
			// Invocamos al servicio REST
			var res = restTemplate.exchange(peticion,Void.class);
			// Comprobamos el resultado
			System.err.println(res);
			assertThat(res.getStatusCode().value()).isEqualTo(201);

			List<Ejercicio> ejerciciosBD = ejercicioRepo.findAll();
			assertThat(ejerciciosBD).hasSize(1);
			assertThat(ejercicio.getNombre()).isEqualTo(ejerciciosBD.get(0).getNombre());
		}

		@Test
		@DisplayName("devuelve error al modificar un ejercicio que no existe")
		public void modificarEjercicioInexistente() {
			var ejercicio = EjercicioDTO.builder().nombre("Ejercicio1").build();
			var peticion = put("http", host,port, "/ejercicio/2", ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al eliminar un ejercicio que no existe")
		public void eliminareEjercicioInexistente() {
			var peticion = delete("http", host,port, "/ejercicio/2");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
	}
	@Nested
	@DisplayName("cuando la base de datos está llena")
	public class BaseDatosLlena {
		@BeforeEach
		public void crearDatos(){
			ejercicioRepo.save(Ejercicio.builder().id(1L).nombre("Ejercicio1").build());
			rutinaRepo.save(Rutina.builder().id(1L).nombre("Rutina1").build());
		}

		@Test
		@DisplayName("Da error cuando inserta una rutina que ya existe")
		public void insertaRutinaExistente() {
			// Preparamos el rutina a insertar
			var rutina = RutinaDTO.builder()
					.nombre("Rutina1")
					.build();
			// Preparamos la petición con el rutina dentro
			var peticion = post("http", host,port, "/rutina?entrenador=0", rutina);

			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);

			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
		}

		@Test 
		@DisplayName("Obtiene una rutina")
		public void obtieneRutina(){
			var peticion = get("http", host,port, "/rutina/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<RutinaDTO>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Rutina1");
		}

		@Test 
		@DisplayName("Actualiza una rutina")
		public void actualizaRutina(){
			var rutina = RutinaDTO.builder().nombre("Rutina2").build();
			var peticion = put("http", host,port, "/rutina/1", rutina);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			//assertThat(rutinaRepo.findById().get().getNombre()).isEqualTo("Rutina2");
		}

		@Test 
		@DisplayName("Elimina una rutina")
		public void eliminaRutina(){
			var peticion = delete("http", host,port, "/rutina/1");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			//assertThat(rutinaRepo.count()).isEqualTo(0);
		}

		@Test
		@DisplayName("Da error cuando inserta una ejercicio que ya existe")
		public void insertaEjercicioExistente() {
			// Preparamos el ejercicio a insertar
			var ejercicio = Ejercicio.builder().id(1L).nombre("Ejercicio1").build();
			// Preparamos la petición con el ejercicio dentro
			var peticion = post("http", host,port, "/ejercicio?entrenador=0", ejercicio);

			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);

			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
		}

		@Test 
		@DisplayName("Obtiene un ejercicio")
		public void obtieneEjercicio(){
			var peticion = get("http", host,port, "/ejercicio/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Ejercicio1");
		}

		@Test 
		@DisplayName("Actualiza un ejercicio")
		public void actualizaEjercicio(){
			var ejercicio = EjercicioDTO.builder().nombre("Ejercicio2").build();
			var peticion = put("http", host,port, "/ejercicio/1", ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepo.findById(1L).get().getNombre()).isEqualTo("Ejercicio2");

		}

		@Test 
		@DisplayName("Elimina un ejercicio")
		public void eliminaEjercicio(){
			var ejercicio = new Ejercicio();
			ejercicio.setNombre("ejercicio");
			ejercicioRepo.save(ejercicio);
			
			var peticion = delete("http", host,port, "/ejercicio/2");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepo.count()).isEqualTo(1);

		}

	}
}
