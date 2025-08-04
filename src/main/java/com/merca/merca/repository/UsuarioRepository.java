package com.merca.merca.repository;

import com.merca.merca.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de usuario
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por su email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el username dado
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email dado
     */
    boolean existsByEmail(String email);

    /**
     * Busca usuarios activos por rol
     */
    List<Usuario> findByRolAndActivoTrue(Usuario.Rol rol);

    /**
     * Busca usuarios por tienda asignada
     */
    List<Usuario> findByTiendaAsignadaAndActivoTrue(String tiendaAsignada);

    /**
     * Busca usuarios activos
     */
    List<Usuario> findByActivoTrue();

    /**
     * Busca usuarios por nombre completo (búsqueda parcial)
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombreCompleto) LIKE LOWER(CONCAT('%', :nombre, '%')) AND u.activo = true")
    List<Usuario> findByNombreCompletoContainingIgnoreCaseAndActivoTrue(@Param("nombre") String nombre);

    /**
     * Cuenta usuarios por rol
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol = :rol AND u.activo = true")
    long countByRolAndActivoTrue(@Param("rol") Usuario.Rol rol);

    /**
     * Busca usuarios de tienda por tienda específica
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'TIENDA' AND u.tiendaAsignada = :tienda AND u.activo = true")
    List<Usuario> findUsuariosTiendaByTienda(@Param("tienda") String tienda);
}
