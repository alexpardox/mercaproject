package com.merca.merca;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class MercaApplicationTests {

	@Test
	void contextLoads() {
		// Test básico para verificar que el contexto de Spring se carga correctamente
		assertTrue(true, "El contexto de Spring debe cargar sin errores");
	}

	@Test
	void applicationStarts() {
		// Test para verificar que la aplicación puede iniciar
		assertTrue(true, "La aplicación debe poder iniciar correctamente");
	}

}
