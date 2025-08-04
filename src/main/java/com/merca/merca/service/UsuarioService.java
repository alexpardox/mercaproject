package com.merca.merca.service;

import com.merca.merca.entity.Usuario;
import com.merca.merca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Actualizar fecha de último acceso
        usuario.setFechaUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return usuario;
    }

    /**
     * Registra un nuevo usuario
     */
    public Usuario registrarUsuario(Usuario usuario) {
        // Verificar que el username no exista
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        // Verificar que el email no exista
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Establecer fecha de creación
        usuario.setFechaCreacion(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente
     */
    public Usuario actualizarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
        if (usuarioExistente.isPresent()) {
            Usuario usuarioActual = usuarioExistente.get();
            
            // Actualizar campos (excepto username y password si no han cambiado)
            usuarioActual.setEmail(usuario.getEmail());
            usuarioActual.setNombreCompleto(usuario.getNombreCompleto());
            usuarioActual.setRol(usuario.getRol());
            usuarioActual.setTiendaAsignada(usuario.getTiendaAsignada());
            usuarioActual.setActivo(usuario.getActivo());

            return usuarioRepository.save(usuarioActual);
        }
        throw new RuntimeException("Usuario no encontrado");
    }

    /**
     * Cambia la contraseña de un usuario
     */
    public void cambiarPassword(Long usuarioId, String nuevaPassword) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            Usuario usuarioActual = usuario.get();
            usuarioActual.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioRepository.save(usuarioActual);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    /**
     * Busca un usuario por ID
     */
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por username
     */
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Busca un usuario por email
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtiene todos los usuarios activos
     */
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    /**
     * Obtiene usuarios por rol
     */
    public List<Usuario> obtenerUsuariosPorRol(Usuario.Rol rol) {
        return usuarioRepository.findByRolAndActivoTrue(rol);
    }

    /**
     * Obtiene usuarios de una tienda específica
     */
    public List<Usuario> obtenerUsuariosPorTienda(String tienda) {
        return usuarioRepository.findByTiendaAsignadaAndActivoTrue(tienda);
    }

    /**
     * Busca usuarios por nombre
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombreCompletoContainingIgnoreCaseAndActivoTrue(nombre);
    }

    /**
     * Desactiva un usuario
     */
    public void desactivarUsuario(Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            Usuario usuarioActual = usuario.get();
            usuarioActual.setActivo(false);
            usuarioRepository.save(usuarioActual);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    /**
     * Activa un usuario
     */
    public void activarUsuario(Long usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            Usuario usuarioActual = usuario.get();
            usuarioActual.setActivo(true);
            usuarioRepository.save(usuarioActual);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    /**
     * Obtiene estadísticas de usuarios por rol
     */
    public long contarUsuariosPorRol(Usuario.Rol rol) {
        return usuarioRepository.countByRolAndActivoTrue(rol);
    }

    /**
     * Obtiene todos los usuarios (para administradores)
     */
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Verifica si un username está disponible
     */
    public boolean isUsernameDisponible(String username) {
        return !usuarioRepository.existsByUsername(username);
    }

    /**
     * Verifica si un email está disponible
     */
    public boolean isEmailDisponible(String email) {
        return !usuarioRepository.existsByEmail(email);
    }
}
