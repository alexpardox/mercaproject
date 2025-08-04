package com.merca.merca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Recursos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/login", "/error", "/favicon.ico", "/h2-console/**").permitAll()
                
                // Rutas de administrador
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // Rutas de área comercial
                .requestMatchers("/comercial/**").hasAnyRole("ADMIN", "COMERCIAL")
                
                // Rutas de tienda
                .requestMatchers("/tienda/**").hasAnyRole("ADMIN", "COMERCIAL", "TIENDA")
                
                // Rutas de proveedores - acceso según rol
                .requestMatchers("/proveedores/nuevo", "/proveedores/*/editar", "/proveedores/*/eliminar")
                    .hasAnyRole("ADMIN", "COMERCIAL")
                .requestMatchers("/proveedores/**").hasAnyRole("ADMIN", "COMERCIAL", "TIENDA")
                
                // Rutas de formularios
                .requestMatchers("/formularios/nuevo").hasAnyRole("ADMIN", "COMERCIAL", "TIENDA")
                .requestMatchers("/formularios/**").hasAnyRole("ADMIN", "COMERCIAL", "TIENDA")
                
                // Dashboard principal
                .requestMatchers("/dashboard", "/").hasAnyRole("ADMIN", "COMERCIAL", "TIENDA")
                
                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "MERCADIA_SESSION")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login?expired=true")
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/error/403")
            );
        return http.build();
    }
}
