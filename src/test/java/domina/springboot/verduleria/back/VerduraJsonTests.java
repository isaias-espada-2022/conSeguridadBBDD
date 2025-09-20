package domina.springboot.verduleria.back;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
@JsonTest
public class VerduraJsonTests {

	private static final Verdura REMOLACHA = new Verdura(1000, "Remolacha", 4.52, false);

	private static final String JSON_REMOLACHA = """
			{
				"id": 1000,
				"nombre": "Remolacha",
				"precio": 4.52,
				"troceable": false
			}
			""";
	
	@Autowired
	private JacksonTester<Verdura> jsonTester;
	
	@Test
	void serializacionVerduraTest() throws IOException {
		JsonContent<Verdura> jsonRemolacha = jsonTester.write(REMOLACHA);
		// tiene id
		assertThat(jsonRemolacha).hasJsonPathNumberValue("@.id");
		// el id es 1000L
		assertThat(jsonRemolacha).extractingJsonPathNumberValue("@.id").isEqualTo(1000);
		// tiene nombre y es Remolacha
		assertThat(jsonRemolacha).hasJsonPathStringValue("@.nombre");
		assertThat(jsonRemolacha).extractingJsonPathStringValue("@.nombre").isEqualTo("Remolacha");
		// comprobación precio
		assertThat(jsonRemolacha).hasJsonPathNumberValue("@.precio");
		assertThat(jsonRemolacha).extractingJsonPathNumberValue("@.precio").isEqualTo(4.52);
		// comprobación troceable
		assertThat(jsonRemolacha).hasJsonPathBooleanValue("@.troceable");
		assertThat(jsonRemolacha).extractingJsonPathBooleanValue("@.troceable").isFalse();
		// comprobación completa
		assertThat(jsonRemolacha).isStrictlyEqualToJson("remolacha.json");
	}
	
	@Test
	void deserializacionVerduraTest() throws IOException {
		// comprobación objeto completo
		ObjectContent<Verdura> objetoRemolacha = jsonTester.parse(JSON_REMOLACHA);
		assertThat(objetoRemolacha).isEqualTo(REMOLACHA);
		// comprobación por campos
		Verdura verduraRemolacha = jsonTester.parseObject(JSON_REMOLACHA);
		assertThat(verduraRemolacha.getId()).isEqualTo(1000L);
		assertThat(verduraRemolacha.getNombre()).isEqualTo("Remolacha");
		assertThat(verduraRemolacha.getPrecio()).isEqualTo(4.52);
		assertThat(verduraRemolacha.isTroceable()).isFalse();
	}
}
