package siisquad.rutinas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;

import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import siisquad.rutinas.entities.*;
import siisquad.rutinas.repositories.*;
import siisquad.rutinas.dtos.*;
import siisquad.rutinas.security.JwtUtil;
import siisquad.rutinas.servicios.GestionEntrenamientoService;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("Tests api Rest")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RutinasApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private RestTemplate entrenaRestTemplate;
	private MockRestServiceServer mockServer;
	@BeforeEach
	public void init() {
		mockServer = MockRestServiceServer.createServer(entrenaRestTemplate);
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
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

			assertThat(respuesta.getBody()).isEqualTo(List.of());
		}

		
		@Test
		@DisplayName("inserta correctamente una rutina")
		public void insertaRutina() {
			var rutina = Rutina.builder()
									.nombre("Rutina1")
									.build();
			
			var peticion = post("http", host,port,rutinaPath+entrenadorParam, jwtToken, rutina);
 
			var respuesta = restTemplate.exchange(peticion,Void.class);
			System.err.println(respuesta);

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
			assertThat(respuesta.getBody()).isEqualTo(List.of());
		}
		
		@Test
		@DisplayName("inserta correctamente un ejercicio")
		public void insertaEjercicio() {
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
		
			var peticion = post("http", host,port,ejercicioPath+entrenadorParam, jwtToken, ejercicio);

			var res = restTemplate.exchange(peticion,Void.class);
	
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
			mockServer.expect(request -> request.getURI().toString().contains("/entrena?cliente=0&entrenador=0"))
					.andRespond(withSuccess("[{}]", MediaType.APPLICATION_JSON));
			mockServer.expect(request -> request.getURI().toString().contains("/entrena?cliente=1&entrenador=0"))
					.andRespond(withSuccess("[{}]", MediaType.APPLICATION_JSON));
		}
		@Test
		@DisplayName("Cliente acceso ejercicio sin permiso")
		public void clienteAccesoEjercicio() {
			var peticion = get("http", host,port, "/ejercicio/1", jwtUtil.generateToken("2"));

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		}
		@Test
		@DisplayName("Cliente acceso ejercicio con permiso")
		public void clienteAccesoEjercicioOk() {
			var peticion = get("http", host,port, "/ejercicio/1", jwtUtil.generateToken("1"));

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		}
		@Test
		@DisplayName("Da error cuando inserta una rutina que ya existe")
		public void insertaRutinaExistente() {
			var rutina = RutinaDTO.builder()
					.nombre("Rutina1")
					.build();
			
			var peticion = post("http", host,port, "/rutina?entrenador=0", jwtToken, rutina);

			var respuesta = restTemplate.exchange(peticion,Void.class);

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
			var ejercicio = EjercicioDTO.builder().id(1L).nombre("Ejercicio1").build();
			
			var peticion = post("http", host,port, "/ejercicio?entrenador=0", jwtToken, ejercicio);

			var respuesta = restTemplate.exchange(peticion,Void.class);

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
		@DisplayName("Acceso con token no válido a ejercicio")
		public void accesoConTokenNoValidoEjercicio() {
			var tokenInvalido = "token_novalido";

			var peticion = get("http", host, port, ejercicioPath + "/112323", tokenInvalido);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Acceso con token válido pero caducado a ejercicio")
		public void accesoConTokenCaducadoEjercicio() {
			var tokenCaducado = jwtUtil.generateExpiredToken("0");

			var peticion = get("http", host, port, ejercicioPath + "/112323", tokenCaducado);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Acceso sin token de autenticación a ejercicio")
		public void accesoSinTokenEjercicio() {
			var peticion = get("http", host, port, ejercicioPath + "/112323", null);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Acceso con token válido a ejercicio no encontrado")
		public void accesoEjercicioNoEncontrado() {

			var peticion = get("http", host, port, ejercicioPath + "/999999", jwtToken);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		}

		@Test
		@DisplayName("Acceso con token válido a ejercicio existente")
		public void ejercicioExistente() {
			var ejercicio = Ejercicio.builder()
					.nombre("EjercicioExistente")
					.build();
			ejercicioRepo.save(ejercicio);
			
			var peticion = post("http", host, port, ejercicioPath + entrenadorParam, jwtToken, ejercicio);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		}

		@Test
		@DisplayName("Acceso con token no válido a rutina")
		public void accesoConTokenNoValidoRutina() {
			var tokenInvalido = "token_novalido";

			var peticion = get("http", host, port, rutinaPath + "/112323", tokenInvalido);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Acceso con token válido pero caducado a rutina")
		public void accesoConTokenCaducadoRutina() {
			var tokenCaducado = jwtUtil.generateExpiredToken("0");

			var peticion = get("http", host, port, rutinaPath + "/112323", tokenCaducado);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Acceso sin token de autenticación a rutina")
		public void accesoSinTokenRutina() {
			var peticion = get("http", host, port, rutinaPath + "/112323", null);

			var respuesta = restTemplate.exchange(peticion, String.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
        @DisplayName("Acceso con token válido a rutina no encontrada")
        public void accesoRutinaNoEncontrada() {

            var peticion = get("http", host, port, rutinaPath +"/999999", jwtToken);
            
            var respuesta = restTemplate.exchange(peticion, String.class);

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Acceso con token válido a rutina existente")
        public void rutinaExistente() {
            var rutina = Rutina.builder()
                                .nombre("RutinaExistente")
                                .build();
			rutinaRepo.save(rutina);
            var peticion = post("http", host, port, rutinaPath + entrenadorParam, jwtToken, rutina);

            var respuesta = restTemplate.exchange(peticion, String.class);

            assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
	}
}
