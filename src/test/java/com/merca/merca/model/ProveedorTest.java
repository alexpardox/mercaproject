package com.merca.merca.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para la entidad Proveedor")
class ProveedorTest {

    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
    }

    @Test
    @DisplayName("Crear proveedor con datos válidos")
    void testCrearProveedorValido() {
        // Arrange & Act
        proveedor.setNombre("Coca-Cola FEMSA");
        proveedor.setRazonSocial("Coca-Cola FEMSA S.A.B. de C.V.");
        proveedor.setRfc("CCF123456T1A");
        proveedor.setEmail("contacto@coca-cola.com");
        proveedor.setTelefono("5555551234");
        proveedor.setDireccion("Av. Insurgentes Sur 1234, CDMX");
        proveedor.setContactoPrincipal("Juan Pérez");
        proveedor.setEstado(Proveedor.Estado.ACTIVO);

        // Assert
        assertEquals("Coca-Cola FEMSA", proveedor.getNombre());
        assertEquals("Coca-Cola FEMSA S.A.B. de C.V.", proveedor.getRazonSocial());
        assertEquals("CCF123456T1A", proveedor.getRfc());
        assertEquals("contacto@coca-cola.com", proveedor.getEmail());
        assertEquals("5555551234", proveedor.getTelefono());
        assertEquals("Av. Insurgentes Sur 1234, CDMX", proveedor.getDireccion());
        assertEquals("Juan Pérez", proveedor.getContactoPrincipal());
        assertEquals(Proveedor.Estado.ACTIVO, proveedor.getEstado());
    }

    @Test
    @DisplayName("Proveedor recién creado debe tener estado ACTIVO por defecto")
    void testEstadoPorDefecto() {
        // Arrange & Act
        Proveedor nuevoProveedor = new Proveedor();
        
        // Assert
        assertEquals(Proveedor.Estado.ACTIVO, nuevoProveedor.getEstado());
        assertNotNull(nuevoProveedor.getFechaRegistro());
    }

    @Test
    @DisplayName("Proveedor debe poder cambiar de estado")
    void testCambiarEstado() {
        // Arrange
        proveedor.setEstado(Proveedor.Estado.ACTIVO);
        
        // Act
        proveedor.setEstado(Proveedor.Estado.INACTIVO);
        
        // Assert
        assertEquals(Proveedor.Estado.INACTIVO, proveedor.getEstado());
    }

    @Test
    @DisplayName("Todos los estados deben estar disponibles")
    void testEstadosDisponibles() {
        // Test estado ACTIVO
        proveedor.setEstado(Proveedor.Estado.ACTIVO);
        assertEquals(Proveedor.Estado.ACTIVO, proveedor.getEstado());
        
        // Test estado INACTIVO
        proveedor.setEstado(Proveedor.Estado.INACTIVO);
        assertEquals(Proveedor.Estado.INACTIVO, proveedor.getEstado());
        
        // Test estado SUSPENDIDO
        proveedor.setEstado(Proveedor.Estado.SUSPENDIDO);
        assertEquals(Proveedor.Estado.SUSPENDIDO, proveedor.getEstado());
    }

    @Test
    @DisplayName("Constructor con parámetros debe funcionar correctamente")
    void testConstructorConParametros() {
        // Arrange & Act
        Proveedor proveedorConDatos = new Proveedor(
            "PepsiCo", 
            "PEP123456ABC", 
            "PepsiCo México S.A. de C.V.", 
            "contacto@pepsico.com", 
            "María González"
        );
        
        // Assert
        assertEquals("PepsiCo", proveedorConDatos.getNombre());
        assertEquals("PEP123456ABC", proveedorConDatos.getRfc());
        assertEquals("PepsiCo México S.A. de C.V.", proveedorConDatos.getRazonSocial());
        assertEquals("contacto@pepsico.com", proveedorConDatos.getEmail());
        assertEquals("María González", proveedorConDatos.getContactoPrincipal());
        assertEquals(Proveedor.Estado.ACTIVO, proveedorConDatos.getEstado());
        assertNotNull(proveedorConDatos.getFechaRegistro());
    }

    @Test
    @DisplayName("Validar que el email se almacena correctamente")
    void testEmail() {
        // Arrange & Act
        String email = "test@proveedor.com";
        proveedor.setEmail(email);
        
        // Assert
        assertEquals(email, proveedor.getEmail());
    }

    @Test
    @DisplayName("Validar que el teléfono se almacena correctamente")
    void testTelefono() {
        // Arrange & Act
        String telefono = "55-1234-5678";
        proveedor.setTelefono(telefono);
        
        // Assert
        assertEquals(telefono, proveedor.getTelefono());
    }

    @Test
    @DisplayName("Validar que la fecha de registro se establece automáticamente")
    void testFechaRegistroAutomatica() {
        // Arrange & Act
        Proveedor nuevoProveedor = new Proveedor();
        
        // Assert
        assertNotNull(nuevoProveedor.getFechaRegistro());
    }
}
