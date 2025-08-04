package com.merca.merca.repository;

import com.merca.merca.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    /**
     * Busca un proveedor por RFC
     */
    Optional<Proveedor> findByRfc(String rfc);

    /**
     * Busca un proveedor por email
     */
    Optional<Proveedor> findByEmail(String email);

    /**
     * Verifica si existe un proveedor con el RFC dado
     */
    boolean existsByRfc(String rfc);

    /**
     * Verifica si existe un proveedor con el email dado
     */
    boolean existsByEmail(String email);

    /**
     * Busca proveedores activos
     */
    List<Proveedor> findByEstado(Proveedor.Estado estado);

    /**
     * Busca proveedores por nombre (búsqueda parcial)
     */
    @Query("SELECT p FROM Proveedor p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) ORDER BY p.nombre")
    List<Proveedor> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    /**
     * Busca proveedores por razón social (búsqueda parcial)
     */
    @Query("SELECT p FROM Proveedor p WHERE LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :razonSocial, '%')) ORDER BY p.razonSocial")
    List<Proveedor> findByRazonSocialContainingIgnoreCase(@Param("razonSocial") String razonSocial);

    /**
     * Busca proveedores activos ordenados por nombre
     */
    @Query("SELECT p FROM Proveedor p WHERE p.estado = 'ACTIVO' ORDER BY p.nombre")
    List<Proveedor> findProveedoresActivosOrdenados();

    /**
     * Búsqueda general por múltiples campos
     */
    @Query("SELECT p FROM Proveedor p WHERE " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.rfc) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(p.contactoPrincipal) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
           "AND p.estado = :estado ORDER BY p.nombre")
    List<Proveedor> buscarProveedores(@Param("texto") String texto, @Param("estado") Proveedor.Estado estado);

    /**
     * Cuenta proveedores por estado
     */
    @Query("SELECT COUNT(p) FROM Proveedor p WHERE p.estado = :estado")
    long countByEstado(@Param("estado") Proveedor.Estado estado);

    /**
     * Busca proveedores con formularios activos
     */
    @Query("SELECT DISTINCT p FROM Proveedor p " +
           "INNER JOIN p.formularios f " +
           "WHERE f.estado = 'ACTIVO' AND p.estado = 'ACTIVO' " +
           "ORDER BY p.nombre")
    List<Proveedor> findProveedoresConFormulariosActivos();
}
