package com.merca.merca.config;

import com.merca.merca.entity.Usuario;
import com.merca.merca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void run(String... args) throws Exception {
        // Siempre verificar si existen usuarios y crearlos solo si no existen
        System.out.println("🔄 Verificando usuarios del sistema...");
        initializeUsersIfNeeded();
        
        // Ya no inicializamos proveedores automáticamente
        System.out.println("⏭️ Inicialización de proveedores deshabilitada");
    }

    private void initializeUsersIfNeeded() {
        // Crear usuario administrador solo si no existe
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
                System.out.println("✅ Usuario administrador creado: admin/password");
            } catch (Exception e) {
                System.out.println("⚠️ Error al crear usuario admin: " + e.getMessage());
            }
        } else {
            System.out.println("✓ Usuario administrador ya existe");
        }

        // Crear usuario comercial solo si no existe
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
                System.out.println("✅ Usuario comercial creado: comercial/password");
            } catch (Exception e) {
                System.out.println("⚠️ Error al crear usuario comercial: " + e.getMessage());
            }
        } else {
            System.out.println("✓ Usuario comercial ya existe");
        }

        // Crear usuario de tienda solo si no existe
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
                System.out.println("✅ Usuario tienda creado: tienda001/password");
            } catch (Exception e) {
                System.out.println("⚠️ Error al crear usuario tienda: " + e.getMessage());
            }
        } else {
            System.out.println("✓ Usuario tienda ya existe");
        }
    }
}
