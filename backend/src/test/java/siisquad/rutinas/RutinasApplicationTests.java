package siisquad.rutinas;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Collections;
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


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RutinasApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	@Autowired
	private RutinasApplication rutinaRepo;	

	@Autowired
	private RepositorioEjercicio ejercicioRepo;
	private URI uri(String scheme, String host, int port, String ...paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path: paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}
	
	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.get(uri)
			.accept(MediaType.APPLICATION_JSON)
			.build();
		return peticion;
	}
	
	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.delete(uri)
			.build();
		return peticion;
	}
	
	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.post(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.body(object);
		return peticion;
	}
	
	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.put(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.body(object);
		return peticion;
	}
	
	private void compruebaCampos(Ejercicio expected, Ejercicio actual) {
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
	}
	
	private void compruebaCampos(Rutina expected, Rutina actual) {
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
	}
	

	@Nested
	@DisplayName("cuando la base de datos está vacía")
	public class BaseDatosVacia {

		@Test
		@DisplayName("devuelve error al acceder a una rutina concreta")
		public void errorRutinaConcreta() {
			var peticion = get("http", "localhost",port, "/rutina/1");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<RutinaDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al acceder a un ejercicio concreto")
		public void errorEjercicioConcreto() {
			var peticion = get("http", "localhost",port, "/ejercicios/1");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve una lista vacía de rutinas")
		public void devuelveListaVaciaRutinas() {
			var peticion = get("http", "localhost",port, "/rutina");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<RutinaDTO>>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}

		@Test
		@DisplayName("inserta correctamente una rutina")
		public void insertaRutina() {
			// Preparamos la rutina a insertar
			var rutina = Rutina.builder()
									.nombre("Rutina1")
									.build();
			// Preparamos la petición con la rutina dentro
			var peticion = post("http", "localhost",port, "/rutina", rutina);
			
			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);
			
			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.startsWith("http://localhost:"+port+"/rutina");
		
			List<Rutina> rutinasBD = rutinaRepo.findAll();
			assertThat(rutinasBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.endsWith("/"+rutinasBD.get(0).getId());
			compruebaCampos(rutina, rutinasBD.get(0));
		}

		@Test
		@DisplayName("devuelve una lista vacía de ejercicios")
		public void devuelveListaVaciaEjercicios() {
			
			
			//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			//assertThat(respuesta.getBody()).isEmpty();
		}
		
		@Test
		@DisplayName("inserta correctamente un ejercicio")
		public void insertaEjercicio() {
			
			// Preparamos el ejercicio a insertar
			var ejercicio = Ejercicio.builder()
									.nombre("Ejercicio1")
									.build();
			// Preparamos la petición con el ejercicio dentro
			var peticion = post("http", "localhost",port, "/ejercicios", ejercicio);
			
			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);
			
			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.startsWith("http://localhost:"+port+"/ejercicios");
		
			List<Ejercicio> ejerciciosBD = ejercicioRepo.findAll();
			assertThat(ejerciciosBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.endsWith("/"+ejerciciosBD.get(0).getId());
			compruebaCampos(ejercicio, ejerciciosBD.get(0));
		}

		@Test
		@DisplayName("devuelve error al modificar un ejercicio que no existe")
		public void modificarEjercicioInexistente() {
			var ejercicio = EjercicioDTO.builder().nombre("Ejercicio1").build();
			var peticion = put("http", "localhost",port, "/ejercicios/1", ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al eliminar un ejercicio que no existe")
		public void eliminareEercicioInexistente() {
			var peticion = delete("http", "localhost",port, "/ejercicios/1");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
	}
	@Nested
	@DisplayName("cuando la base de datos está llena")
	public class BaseDatosLlena {
		@BeforeEach
		public void crearDatos(){
			
		}

		@Test
		@DisplayName("Da error cuando inserta una rutina que ya existe")
		public void insertaRutinaExistente() {
			
			//assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
		}

		@Test 
		@DisplayName("Obtiene una rutina")
		public void obtieneRutina(){
			
			//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		}

		@Test 
		@DisplayName("Actualiza una rutina")
		public void actualizaRutina(){
			
			//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		}

		@Test 
		@DisplayName("Elimina una rutina")
		public void eliminaRutina(){
			
			//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		}

		@Test
		@DisplayName("Da error cuando inserta una ejercicio que ya existe")
		public void insertaEjercicioExistente() {
			
			//assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
		}

		@Test 
		@DisplayName("Obtiene un ejercicio")
		public void obtieneEjercicio(){
			
			//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		}

		@Test 
		@DisplayName("Actualiza un ejercicio")
		public void actualizaEjercicio(){
			
			//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

		}

		@Test 
		@DisplayName("Elimina un ejercicio")
		public void eliminaEjercicio(){
			
			//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

		}

		@Test
		@DisplayName("Devuelve una lista de rutinas")
		public void devuelveListaRutinas() {
			

			//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		}
	
		@Test
		@DisplayName("Devuelve una lista de ejercicios")
		public void devuelveListaEjercicios() {
			

			//assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		}
	}
}
