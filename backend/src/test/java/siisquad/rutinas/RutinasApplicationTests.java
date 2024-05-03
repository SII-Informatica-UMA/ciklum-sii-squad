package siisquad.rutinas;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@DisplayName("")
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RutinasApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Value(value="${local.server.port}")
	private int port;
	
	@Autowired
	private RutinasApplication rutinaRepo;	
	
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
					new ParameterizedTypeReference<rutinaDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al acceder a un ejercicio concreto")
		public void errorEjercicioConcreto() {
			var peticion = get("http", "localhost",port, "/ejercicios/1");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<ejercicioDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve una lista vacía de rutina")
		public void devuelveListaVaciaRutinas() {
			var peticion = get("http", "localhost",port, "/rutina");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<rutinaDTO>>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}

		@Test
		@DisplayName("inserta correctamente una rutina")
		public void insertaRutina() {
			// Preparamos la rutina a insertar
			var rutina = rutinaDTO.builder()
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
		
			List<rutina> rutinasBD = rutinaRepo.findAll();
			assertThat(rutinasBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.endsWith("/"+rutinasBD.get(0).getId());
			compruebaCampos(rutina.rutina(), rutinasBD.get(0));
		}

		@Test
		@DisplayName("devuelve una lista vacía de ejercicios")
		public void devuelveListaVaciaEjercicios() {
			
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}
		
		@Test
		@DisplayName("inserta correctamente un ejercicio")
		public void insertaEjercicio() {
			
			// Preparamos el ejercicio a insertar
			var ejercicio = ejercicioDTO.builder()
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
		
			List<ejercicio> ejerciciosBD = ejercicioRepo.findAll();
			assertThat(ejerciciosBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.endsWith("/"+ejerciciosBD.get(0).getId());
			compruebaCampos(ejercicio.ejercicio(), ejerciciosBD.get(0));
		}
	}
	@Nested
	@DisplayName("cuando la base de datos está llena")
	public class BaseDatosLlena {
		@BeforeEach
		public void crearDatos(){
			
		}

		@Test 
		@DisplayName("Obtiene una rutina")
		public void obtieneRutina(){
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

		}

		@Test 
		@DisplayName("Actualiza una rutina")
		public void actualizaRutina(){
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

		}

		@Test 
		@DisplayName("Elimina una rutina")
		public void eliminaRutina(){
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

		}

		@Test 
		@DisplayName("Obtiene un ejercicio")
		public void obtieneEjercicio(){
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			
		}

		@Test 
		@DisplayName("Actualiza un ejercicio")
		public void actualizaEjercicio(){
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

		}

		@Test 
		@DisplayName("Elimina un ejercicio")
		public void eliminaEjercicio(){
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

		}

		@Test
		@DisplayName("Devuelve una lista de rutinas")
		public void devuelveListaRutinas() {
			

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		}
	
		@Test
		@DisplayName("Devuelve una lista de ejercicios")
		public void devuelveListaEjercicios() {
			

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		}
	}
}
