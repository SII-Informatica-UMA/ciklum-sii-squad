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


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RutinasApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	private final String host = "localhost";
	@Autowired
	private RepositorioRutina rutinaRepo;

	private final String entrenadorParam = "?entrenador=0";
	private final String rutinaPath= "/rutina";
	private final String ejercicioPath = "/ejercicio";

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
			var peticion = get("http", host,port, rutinaPath +"/1");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<RutinaDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al acceder a un ejercicio concreto")
		public void errorEjercicioConcreto() {
			var peticion = get("http", host,port, ejercicioPath + "/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve una lista vacía de rutinas")
		public void devuelveListaVaciaRutinas() {
			var peticion = get("http", host,port, rutinaPath+entrenadorParam);
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<RutinaDTO>>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEqualTo("[]");
		}

		@Test
		@DisplayName("inserta correctamente una rutina")
		public void insertaRutina() {
			// Preparamos la rutina a insertar
			var rutina = Rutina.builder()
									.nombre("Rutina1")
									.build();
			// Preparamos la petición con la rutina dentro
			var peticion = post("http", host,port, "/rutina?entrenador=0", rutina);
			
			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);
			
			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.startsWith("http://localhost:"+port+"/rutina");
		
			/*List<Rutina> rutinasBD = rutinaRepo;
			assertThat(rutinasBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.endsWith("/"+rutinasBD.get(0).getId());
			compruebaCampos(rutina, rutinasBD.get(0));*/
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
			var ejercicio = EjercicioNuevoDTO.builder()
					.nombre("Ejercicio1")
					.descripcion("desc")
					.dificultad("dificil")
					.material("ma")
					.tipo("t")
					.musculosTrabajados("m")
					.multimedia(List.of("url1", "url2"))
					.observaciones("obs")
					.build();
			// Preparamos la petición con el ejercicio dentro

			var peticion = post("http", host,port, "/ejercicio?entrenador=0", ejercicio);
			System.err.println(peticion);
			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);
			
			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

			List<Ejercicio> ejerciciosBD = ejercicioRepo.findAll();
			assertThat(ejerciciosBD).hasSize(1);

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
		public void eliminareEercicioInexistente() {
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
			var peticion = get("http", host,port, "/rutinas/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<RutinaDTO>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Rutina1");
		}

		@Test 
		@DisplayName("Actualiza una rutina")
		public void actualizaRutina(){
			var rutina = RutinaDTO.builder().nombre("Rutina2").build();
			var peticion = put("http", host,port, "/rutinas/1", rutina);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			//assertThat(rutinaRepo.findById().get().getNombre()).isEqualTo("Rutina2");
		}

		@Test 
		@DisplayName("Elimina una rutina")
		public void eliminaRutina(){
			var peticion = delete("http", host,port, "/rutinas/1");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			//assertThat(rutinaRepo.count()).isEqualTo(0);
		}

		@Test
		@DisplayName("Da error cuando inserta una ejercicio que ya existe")
		public void insertaEjercicioExistente() {
			// Preparamos el ejercicio a insertar
			var ejercicio = EjercicioDTO.builder()
					.nombre("Ejercicio1")
					.build();
			// Preparamos la petición con el ejercicio dentro
			var peticion = post("http", host,port, "/ejercicios", ejercicio);

			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);

			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
		}

		@Test 
		@DisplayName("Obtiene un ejercicio")
		public void obtieneEjercicio(){
			var peticion = get("http", host,port, "/ejercicios/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Ejercicio1");
		}

		@Test 
		@DisplayName("Actualiza un ejercicio")
		public void actualizaEjercicio(){
			var ejercicio = EjercicioDTO.builder().nombre("Ejercicio2").build();
			var peticion = put("http", host,port, "/ejercicios/1", ejercicio);

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
			
			var peticion = delete("http", host,port, "/ejercicios/2");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepo.count()).isEqualTo(1);

		}

		@Test
		@DisplayName("Devuelve una lista de rutinas")
		public void devuelveListaRutinas() {
			var peticion = get("http", host,port, "/rutinas");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<RutinaDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().size()).isEqualTo(1);
		}
	
		@Test
		@DisplayName("Devuelve una lista de ejercicios")
		public void devuelveListaEjercicios() {
			var peticion = get("http", host,port, "/ejercicios");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<EjercicioDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().size()).isEqualTo(1);
		}
	}
}
