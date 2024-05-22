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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;

import siisquad.rutinas.entities.*;
import siisquad.rutinas.repositories.*;
import siisquad.rutinas.dtos.*;
import siisquad.rutinas.security.JwtUtil;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("Tests api Rest")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RutinasApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private JwtUtil jwtUtil;

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

	private String jwtToken;


	private RequestEntity<Void> get(String scheme, String host, int port, String path, String token) {
		var peticion = RequestEntity.get(URI.create(scheme+"://"+host+":"+port+path))
			.accept(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
			.build();
		return peticion;
	}
	
	private RequestEntity<Void> delete(String scheme, String host, int port, String path, String token) {
		var peticion = RequestEntity.delete(URI.create(scheme+"://"+host+":"+port+path))
			.header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
			.build();
		return peticion;
	}
	
	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, String token, T object) {
		var peticion = RequestEntity.post(URI.create(scheme+"://"+host+":"+port+path))
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
			.body(object);
		return peticion;
	}
	
	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, String token, T object) {
		var peticion = RequestEntity.put(URI.create(scheme+"://"+host+":"+port+path))
			.contentType(MediaType.APPLICATION_JSON)
			.header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
			.body(object);
		return peticion;
	}

	private String URL_BASE(){
		return  "http://"+host+":"+port;
	}
	@BeforeEach
	public void creaToken(){
		this.jwtToken = jwtUtil.generateToken("0");
	}
	@Test
	@DisplayName("Comprueba token valido")
	public void compruebaToken(){
		System.err.println("\n"+jwtToken+"\n");
		assertThat(jwtUtil.getUsernameFromToken(jwtToken)).isEqualTo("0");
		assertThat(jwtUtil.isTokenExpired(jwtToken)).isFalse();
	}
	@Nested
	@DisplayName("cuando la base de datos está vacía")
	public class BaseDatosVacia {

		@Test
		@DisplayName("devuelve error al acceder a una rutina concreta")
		public void errorRutinaConcreta() {
			var peticion = get("http", host,port, rutinaPath +"/112323", jwtToken);
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<RutinaDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al acceder a un ejercicio concreto")
		public void errorEjercicioConcreto() {
			var peticion = get("http", host,port, ejercicioPath + "/1243902834", jwtToken);
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve una lista vacía de rutinas")
		public void devuelveListaVaciaRutinas() {
			var peticion = get("http", host,port,rutinaPath+entrenadorParam, jwtToken);

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
			var peticion = post("http", host,port,rutinaPath+entrenadorParam, jwtToken, rutina);

			
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
			var peticion = put("http", host,port, "/rutina/2", jwtToken, ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al eliminar una rutina que no existe")
		public void eliminareRutinaInexistente() {
			var peticion = delete("http", host,port, "/rutina/2", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		@Test
		@DisplayName("devuelve una lista vacía de ejercicios")
		public void devuelveListaVaciaEjercicios() {
			var peticion = get("http", host,port,ejercicioPath+entrenadorParam, jwtToken);

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
			var peticion = post("http", host,port,ejercicioPath+entrenadorParam, jwtToken, ejercicio);

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
			var peticion = put("http", host,port, "/ejercicio/2", jwtToken, ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error al eliminar un ejercicio que no existe")
		public void eliminareEjercicioInexistente() {
			var peticion = delete("http", host,port, "/ejercicio/2", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
	}
	@Nested
	@DisplayName("cuando la base de datos está llena")
	public class BaseDatosLlena {
		@BeforeEach
		public void crearDatos(){
			ejercicioRepo.save(Ejercicio.builder().id(1L).nombre("Ejercicio1").entrenador(0).build());
			rutinaRepo.save(Rutina.builder().id(1L).nombre("Rutina1").entrenador(0).build());
		}

		@Test
		@DisplayName("Da error cuando inserta una rutina que ya existe")
		public void insertaRutinaExistente() {
			// Preparamos el rutina a insertar
			var rutina = RutinaDTO.builder()
					.nombre("Rutina1")
					.build();
			// Preparamos la petición con el rutina dentro
			var peticion = post("http", host,port, "/rutina?entrenador=0", jwtToken, rutina);

			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);

			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
		}

		@Test 
		@DisplayName("Obtiene una rutina")
		public void obtieneRutina(){
			var peticion = get("http", host,port, "/rutina/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<RutinaDTO>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Rutina1");
		}

		@Test 
		@DisplayName("Actualiza una rutina")
		public void actualizaRutina(){
			var rutina = RutinaDTO.builder().nombre("Rutina2").build();
			var peticion = put("http", host,port, "/rutina/1", jwtToken, rutina);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			//assertThat(rutinaRepo.findById().get().getNombre()).isEqualTo("Rutina2");
		}

		@Test 
		@DisplayName("Elimina una rutina")
		public void eliminaRutina(){
			var peticion = delete("http", host,port, "/rutina/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(rutinaRepo.count()).isEqualTo(0);
		}

		@Test
		@DisplayName("Da error cuando inserta una ejercicio que ya existe")
		public void insertaEjercicioExistente() {
			// Preparamos el ejercicio a insertar
			var ejercicio = Ejercicio.builder().id(1L).nombre("Ejercicio1").build();
			// Preparamos la petición con el ejercicio dentro
			var peticion = post("http", host,port, "/ejercicio?entrenador=0", jwtToken, ejercicio);

			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);

			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(409);
		}

		@Test 
		@DisplayName("Obtiene un ejercicio")
		public void obtieneEjercicio(){
			var peticion = get("http", host,port, "/ejercicio/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Ejercicio1");
		}

		@Test 
		@DisplayName("Actualiza un ejercicio")
		public void actualizaEjercicio(){
			var ejercicio = EjercicioDTO.builder().nombre("Ejercicio2").build();
			var peticion = put("http", host,port, "/ejercicio/1", jwtToken, ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepo.findById(1L).get().getNombre()).isEqualTo("Ejercicio2");

		}

		@Test 
		@DisplayName("Elimina un ejercicio")
		public void eliminaEjercicio(){
			var peticion = delete("http", host,port, "/ejercicio/1", jwtToken);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ejercicioRepo.count()).isEqualTo(0);

		}

	}
	@Nested
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
	@DisplayName("Tests de autenticación")
	public class TestsAutenticacion{

		@Test
		@DisplayName("Acceso sin token de autenticación")
		public void accesoSinToken() {
			var peticion = get("http", host, port, rutinaPath + "/112323", null);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Acceso con token no válido")
		public void accesoConTokenNoValido() {
			var tokenInvalido = "token_novalido";

			var peticion = get("http", host, port, rutinaPath + "/112323", tokenInvalido);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Acceso con token válido pero caducado")
		public void accesoConTokenCaducado() {
			var tokenCaducado = jwtUtil.generateExpiredToken("0");

			var peticion = get("http", host, port, rutinaPath + "/112323", tokenCaducado);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Acceso con token válido")
		public void accesoConTokenValido() {
			var tokenValido = jwtUtil.generateToken("0");

			var peticion = get("http", host, port, rutinaPath + "/112323", tokenValido);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND); 
		}
	}
}
