package com.merca.merca.config;

import com.merca.merca.entity.Proveedor;
import com.merca.merca.entity.Usuario;
import com.merca.merca.service.ProveedorService;
import com.merca.merca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.initialize-data:false}")
    private boolean initializeData;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProveedorService proveedorService;

    @Override
    public void run(String... args) throws Exception {
        // Solo inicializar datos si está habilitado explícitamente
        if (initializeData) {
            System.out.println("🔄 Inicializando datos de ejemplo...");
            initializeUsers();
            initializeProveedores();
        } else {
            System.out.println("⏭️ Inicialización de datos deshabilitada");
            // Siempre intentar crear un usuario admin si no existe
            initializeAdminUser();
        }
    }

    private void initializeAdminUser() {
        try {
            if (usuarioService.buscarPorUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setEmail("admin@mercadia.com");
                admin.setPassword("password");
                admin.setNombreCompleto("Administrador del Sistema");
                admin.setRol(Usuario.Rol.ADMINISTRADOR);
                admin.setActivo(true);
                
                usuarioService.registrarUsuario(admin);
                System.out.println("✅ Usuario administrador creado: admin/password");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error al crear usuario admin: " + e.getMessage());
        }
    }

    private void initializeUsers() {
        // Crear usuario administrador
        if (usuarioService.buscarPorUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@mercadia.com");
            admin.setPassword("password");
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setRol(Usuario.Rol.ADMINISTRADOR);
            admin.setActivo(true);
            
            try {
                usuarioService.registrarUsuario(admin);
                System.out.println("Usuario administrador creado: admin/password");
            } catch (Exception e) {
                System.err.println("Error creando usuario admin: " + e.getMessage());
            }
        }

        // Crear usuario comercial
        if (usuarioService.buscarPorUsername("comercial").isEmpty()) {
            Usuario comercial = new Usuario();
            comercial.setUsername("comercial");
            comercial.setEmail("comercial@mercadia.com");
            comercial.setPassword("password");
            comercial.setNombreCompleto("Usuario Comercial");
            comercial.setRol(Usuario.Rol.COMERCIAL);
            comercial.setActivo(true);
            
            try {
                usuarioService.registrarUsuario(comercial);
                System.out.println("Usuario comercial creado: comercial/password");
            } catch (Exception e) {
                System.err.println("Error creando usuario comercial: " + e.getMessage());
            }
        }

        // Crear usuario de tienda
        if (usuarioService.buscarPorUsername("tienda001").isEmpty()) {
            Usuario tienda = new Usuario();
            tienda.setUsername("tienda001");
            tienda.setEmail("tienda001@mercadia.com");
            tienda.setPassword("password");
            tienda.setNombreCompleto("Encargado Tienda 001");
            tienda.setRol(Usuario.Rol.TIENDA);
            tienda.setTiendaAsignada("TDA001");
            tienda.setActivo(true);
            
            try {
                usuarioService.registrarUsuario(tienda);
                System.out.println("Usuario tienda creado: tienda001/password");
            } catch (Exception e) {
                System.err.println("Error creando usuario tienda: " + e.getMessage());
            }
        }
    }

    private void initializeProveedores() {
        // Crear proveedores de ejemplo
        if (proveedorService.buscarPorRfc("ABC123456789").isEmpty()) {
            Proveedor proveedor1 = new Proveedor();
            proveedor1.setNombre("Coca-Cola FEMSA");
            proveedor1.setRfc("ABC123456789");
            proveedor1.setRazonSocial("Coca-Cola FEMSA, S.A.B. de C.V.");
            proveedor1.setEmail("contacto@cocacola.com");
            proveedor1.setTelefono("5555551234");
            proveedor1.setContactoPrincipal("Juan Pérez");
            proveedor1.setDireccion("Av. Insurgentes Sur 1234, Ciudad de México");
            proveedor1.setEstado(Proveedor.Estado.ACTIVO);

            try {
                proveedorService.registrarProveedor(proveedor1);
                System.out.println("Proveedor Coca-Cola FEMSA creado");
            } catch (Exception e) {
                System.err.println("Error creando proveedor Coca-Cola: " + e.getMessage());
            }
        }

        if (proveedorService.buscarPorRfc("DEF987654321").isEmpty()) {
            Proveedor proveedor2 = new Proveedor();
            proveedor2.setNombre("Bimbo");
            proveedor2.setRfc("DEF987654321");
            proveedor2.setRazonSocial("Grupo Bimbo, S.A.B. de C.V.");
            proveedor2.setEmail("contacto@bimbo.com");
            proveedor2.setTelefono("5555559876");
            proveedor2.setContactoPrincipal("María García");
            proveedor2.setDireccion("Prolongación Paseo de la Reforma 1000, Ciudad de México");
            proveedor2.setEstado(Proveedor.Estado.ACTIVO);

            try {
                proveedorService.registrarProveedor(proveedor2);
                System.out.println("Proveedor Bimbo creado");
            } catch (Exception e) {
                System.err.println("Error creando proveedor Bimbo: " + e.getMessage());
            }
        }

        if (proveedorService.buscarPorRfc("GHI456789123").isEmpty()) {
            Proveedor proveedor3 = new Proveedor();
            proveedor3.setNombre("Nestlé México");
            proveedor3.setRfc("GHI456789123");
            proveedor3.setRazonSocial("Nestlé México, S.A. de C.V.");
            proveedor3.setEmail("contacto@nestle.com.mx");
            proveedor3.setTelefono("5555557890");
            proveedor3.setContactoPrincipal("Carlos López");
            proveedor3.setDireccion("Ejército Nacional 453, Ciudad de México");
            proveedor3.setEstado(Proveedor.Estado.ACTIVO);

            try {
                proveedorService.registrarProveedor(proveedor3);
                System.out.println("Proveedor Nestlé México creado");
            } catch (Exception e) {
                System.err.println("Error creando proveedor Nestlé: " + e.getMessage());
            }
        }

        if (proveedorService.buscarPorRfc("JKL789123456").isEmpty()) {
            Proveedor proveedor4 = new Proveedor();
            proveedor4.setNombre("Procter & Gamble");
            proveedor4.setRfc("JKL789123456");
            proveedor4.setRazonSocial("Procter & Gamble Manufacturing, S. de R.L. de C.V.");
            proveedor4.setEmail("contacto@pg.com");
            proveedor4.setTelefono("5555554567");
            proveedor4.setContactoPrincipal("Ana Rodríguez");
            proveedor4.setDireccion("Santa Fe 505, Ciudad de México");
            proveedor4.setEstado(Proveedor.Estado.ACTIVO);

            try {
                proveedorService.registrarProveedor(proveedor4);
                System.out.println("Proveedor Procter & Gamble creado");
            } catch (Exception e) {
                System.err.println("Error creando proveedor P&G: " + e.getMessage());
            }
        }
    }
}
