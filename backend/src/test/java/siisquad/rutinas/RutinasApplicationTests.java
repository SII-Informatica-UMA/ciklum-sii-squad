package siisquad.rutinas;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;

import siisquad.rutinas.entities.*;
import siisquad.rutinas.repositories.*;
import siisquad.rutinas.dtos.*;
import siisquad.rutinas.controllers.*;
import siisquad.rutinas.security.JwtUtil;
import siisquad.rutinas.servicios.ServicioEjercicio;
import siisquad.rutinas.servicios.ServicioEntrena;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("Tests api Rest")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RutinasApplicationTests {

	@Mock
    private ServicioEjercicio servicioEjercicio;

    @Mock
    private ServicioEntrena servicioEntrena;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private EjercicioController ejercicioController;

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

	private final String clienteParam = "?cliente=0";
	private final String entrenadorParam = "?entrenador=0";
	private final String rutinaPath= "/rutina";
	private final String ejercicioPath = "/ejercicio";

	private String jwtToken, tokenInvalido;


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
		this.tokenInvalido = jwtUtil.generateToken("1");
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
			assertThat(rutinaRepo.findById(1L).get().getNombre()).isEqualTo("Rutina2");
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
		@DisplayName("devuelve una lista de rutinas")
		public void devuelveListaRutinas() {
			var peticion = get("http", host,port,rutinaPath+entrenadorParam, jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<RutinaDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(1);
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
		@Test
		@DisplayName("devuelve una lista de ejercicios")
		public void devuelveListaEjercicios() {
			var peticion = get("http", host,port,ejercicioPath+entrenadorParam, jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<EjercicioDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(1);
		}

	}
	@Nested
	@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
	@DisplayName("Tests de autenticación")
	public class TestsAutenticacion{
		@BeforeEach
		public void crearDatos(){
			ejercicioRepo.save(Ejercicio.builder().id(1L).nombre("Ejercicio1").entrenador(0).build());
			rutinaRepo.save(Rutina.builder().id(1L).nombre("Rutina1").entrenador(0).build());
		}
		@Test
		@DisplayName("inserta correctamente una rutina")
		public void insertaRutina() {
			var rutina = Rutina.builder()
					.nombre("Rutina1")
					.build();

			var peticion = post("http", host,port,rutinaPath+entrenadorParam, tokenInvalido, rutina);

			var respuesta = restTemplate.exchange(peticion,Void.class);
			System.err.println(respuesta);
			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("devuelve una lista de rutinas")
		public void devuelveListaRutinas() {
			var peticion = get("http", host,port,rutinaPath+entrenadorParam, tokenInvalido);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<RutinaDTO>>() {});

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Obtiene una rutina")
		public void obtieneRutina(){
			
			var peticion = get("http", host,port, "/rutina/1", tokenInvalido);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<RutinaDTO>() {});

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Actualiza una rutina")
		public void actualizaRutina(){
			
			var rutina = RutinaDTO.builder().nombre("Rutina2").build();
			var peticion = put("http", host,port, "/rutina/1", tokenInvalido, rutina);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Elimina una rutina")
		public void eliminaRutina(){
			
			var peticion = delete("http", host,port, "/rutina/1", tokenInvalido);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("devuelve una lista de ejercicios")
		public void devuelveListaEjercicios() {
			var peticion = get("http", host,port,ejercicioPath+entrenadorParam, tokenInvalido);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<EjercicioDTO>>() {});

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Obtiene un ejercicio")
		public void obtieneEjercicio(){
			
			var peticion = get("http", host,port, "/ejercicio/1", tokenInvalido);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Obtiene un ejercicio un cliente")
		public void obtieneEjercicioCliente(){
			Long idEntrenador = 1L;
			Long idCliente = 2L;
			Long idEjercicio = 1L;

			// Simular asignación de entrenador a cliente
			List<AsignacionEntrenamientoDTO> asignaciones = new ArrayList<>();
			asignaciones.add(new AsignacionEntrenamientoDTO(1L, idCliente, idEntrenador));
			when(servicioEntrena.getEntrenaPorCliente(idCliente, "Bearer tokenInvalido")).thenReturn(null);

			// Simular ejercicio por el entrenador
			EjercicioDTO ejercicioDTO = new EjercicioDTO();
			ejercicioDTO.setId(idEjercicio);
			when(servicioEjercicio.getEjercicio(idEjercicio)).thenReturn(null);

			ResponseEntity<EjercicioDTO> responseEntity = null;
			try {
				responseEntity = restTemplate.exchange(
						get("http",host,port, "/entrena?cliente=2" , jwtToken),
						EjercicioDTO.class
				);
			} catch (HttpClientErrorException.Forbidden e) {
				assertEquals(HttpStatus.FORBIDDEN, e.getStatusCode());
        	}

			var peticion = get("http", host,port, "/entrena?cliente=2", jwtToken);

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<EjercicioDTO>() {});

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Actualiza un ejercicio")
		public void actualizaEjercicio(){
			
			var ejercicio = EjercicioDTO.builder().nombre("Ejercicio2").build();
			var peticion = put("http", host,port, "/ejercicio/1", tokenInvalido, ejercicio);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Elimina un ejercicio")
		public void eliminaEjercicio(){
			

			var peticion = delete("http", host,port, "/ejercicio/1", tokenInvalido);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

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

			var peticion = post("http", host,port,ejercicioPath+entrenadorParam,tokenInvalido, ejercicio);

			var respuesta = restTemplate.exchange(peticion,Void.class);
			
			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

		}

		@Test
		@DisplayName("obtiene una lista de entrenadores de un cliente")
   		public void obtenerListaEntrenadoresDeCliente() {
			AsignacionEntrenamientoDTO asig1 = new AsignacionEntrenamientoDTO(1L, 4L, null, null, null);
			AsignacionEntrenamientoDTO asig2 = new AsignacionEntrenamientoDTO(2L, 5L, null, null, null);
			AsignacionEntrenamientoDTO asig3 = new AsignacionEntrenamientoDTO(3L, 6L, null, null, null);
			
			List<AsignacionEntrenamientoDTO> entrenadores = new ArrayList<>();
			entrenadores.add(asig1);
			entrenadores.add(asig2);
			entrenadores.add(asig3);

			EjercicioController servicios = new EjercicioController(null, null);
			List<Long> idsEntrenadores = servicios.listaEntrenadoresDeCliente(entrenadores);

			// Verify that the list contains the correct IDs
			Assertions.assertEquals(3, idsEntrenadores.size());
			Assertions.assertTrue(idsEntrenadores.contains(1L));
			Assertions.assertTrue(idsEntrenadores.contains(2L));
			Assertions.assertTrue(idsEntrenadores.contains(3L));
    	}


	}
}
