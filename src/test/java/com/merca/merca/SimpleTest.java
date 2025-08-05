package com.merca.merca;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test extremadamente simple para verificar que Travis CI funciona
 * No requiere Spring Boot, base de datos ni configuraciones complejas
 */
class SimpleTest {

    @Test
    void testBasicMath() {
        // Test de matemáticas básicas
        int resultado = 2 + 2;
        assertEquals(4, resultado, "2 + 2 debe ser igual a 4");
    }

    @Test
    void testStringOperations() {
        // Test de strings básico
        String saludo = "Hola " + "Mundo";
        assertEquals("Hola Mundo", saludo);
        assertTrue(saludo.contains("Hola"));
        assertFalse(saludo.isEmpty());
    }

    @Test
    void testBooleanLogic() {
        // Test de lógica booleana
        boolean verdadero = true;
        boolean falso = false;
        
        assertTrue(verdadero);
        assertFalse(falso);
        assertNotEquals(verdadero, falso);
    }
}
