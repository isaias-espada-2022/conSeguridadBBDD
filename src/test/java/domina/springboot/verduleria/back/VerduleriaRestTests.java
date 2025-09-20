package domina.springboot.verduleria.back;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class VerduleriaRestTests {
	@Autowired
	TestRestTemplate restTemplate;
	
	@Test
	void consultarVerduras() {
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("carmen", "lechuguita123")
				.getForEntity("/verduras", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$['content']");
        assertThat(page.size()).isEqualTo(3);

        JSONArray nombres = documentContext.read("$..nombre");
        assertThat(nombres).containsExactlyInAnyOrder("Tomate H2 Test", "Calabaza H2 Test", "Lechuga H2 Test");		
	}
	
	@Test
	void consultarTomateTest() {
		ResponseEntity<String> response = restTemplate
				.withBasicAuth("carmen", "lechuguita123")
				.getForEntity("/verduras/2001", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		Number id = documentContext.read("$.id");
		assertThat(id).isEqualTo(2001);

		String nombre = documentContext.read("$.nombre");
		assertThat(nombre).isEqualTo("Tomate H2 Test");

		Double precio = documentContext.read("$.precio");
		assertThat(precio).isEqualTo(3.82);

		Boolean troceable = documentContext.read("$.troceable");
		assertThat(troceable).isFalse();
	}

	@Test
	@DirtiesContext
	void crearRemolachaTest() {
		Verdura remolacha = new Verdura(0, "Remolacha", 4.52, false);
		ResponseEntity<Verdura> createResponse = restTemplate
				.withBasicAuth("carmen", "lechuguita123")
				.postForEntity("/verduras", remolacha, Verdura.class);
		// comprobación status respuesta
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// recuperación URI del objeto creado
		URI remolachaLocation = createResponse.getHeaders().getLocation();
		// recuperación del nuevo id
		Number nuevoId = recuperarId(remolachaLocation);
		
		// comprobación body respuesta
		Verdura remolachaCreada = JsonPath.parse(createResponse.getBody()).json();
		assertThat(remolachaCreada.getId()).isEqualTo(nuevoId); 
		assertThat(remolachaCreada.getNombre()).isEqualTo("Remolacha");
		assertThat(remolachaCreada.getPrecio()).isEqualTo(4.52);
		assertThat(remolachaCreada.isTroceable()).isFalse();

		// comprobación get by id del objeto creado
		ResponseEntity<String> getResponse = restTemplate
				.withBasicAuth("carmen", "lechuguita123")
				.getForEntity(remolachaLocation, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(getResponse.getBody());

		Number id = documentContext.read("$.id");
		assertThat(id).isNotNull(); // no sabemos cuál será
		assertThat(id).isNotEqualTo(0);
		assertThat(Long.valueOf(id.longValue())).isEqualTo((long)nuevoId); // o sí

		String nombre = documentContext.read("$.nombre");
		assertThat(nombre).isEqualTo("Remolacha");
		
		Double precio = documentContext.read("$.precio");
		assertThat(precio).isEqualTo(4.52);
		
		Boolean troceable = documentContext.read("$.troceable");
		assertThat(troceable).isFalse();
	}

	private long recuperarId(URI remolachaLocation) {
		String path = remolachaLocation.getPath();
		String idText = path.substring(path.lastIndexOf('/') + 1);
		return Long.parseLong(idText);
	}
}
