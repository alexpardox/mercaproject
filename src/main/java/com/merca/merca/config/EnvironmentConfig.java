package com.merca.merca.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Configuración para cargar variables de entorno desde el archivo .env
 * Esta clase se asegura de que las variables de entorno estén disponibles
 * para la aplicación Spring Boot antes de que se inicialice.
 */
@Component
public class EnvironmentConfig implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        Properties props = new Properties();
        
        try {
            // Intentar cargar el archivo .env desde el directorio raíz del proyecto
            String envPath = ".env";
            if (Files.exists(Paths.get(envPath))) {
                props.load(new FileInputStream(envPath));
                environment.getPropertySources().addLast(new PropertiesPropertySource("envProps", props));
                System.out.println("✅ Archivo .env cargado exitosamente");
                
                // Mostrar variables importantes para debug
                String dbUrl = props.getProperty("DB_URL");
                String dbUser = props.getProperty("DB_USERNAME");
                if (dbUrl != null && dbUser != null) {
                    System.out.println("🔗 DB_URL: " + dbUrl);
                    System.out.println("👤 DB_USERNAME: " + dbUser);
                } else {
                    System.out.println("⚠️ Variables DB_URL o DB_USERNAME no encontradas en .env");
                }
            } else {
                System.out.println("⚠️ Archivo .env no encontrado, usando variables de sistema");
            }
        } catch (IOException e) {
            System.out.println("❌ Error al cargar archivo .env: " + e.getMessage());
        }
    }
}
