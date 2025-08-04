package com.merca.merca.repository;

import com.merca.merca.entity.Formulario;
import com.merca.merca.entity.Proveedor;
import com.merca.merca.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FormularioRepository extends JpaRepository<Formulario, Long> {

    /**
     * Busca formularios por usuario
     */
    List<Formulario> findByUsuario(Usuario usuario);

    /**
     * Busca formularios por proveedor
     */
    List<Formulario> findByProveedor(Proveedor proveedor);

    /**
     * Busca formularios por estado
     */
    List<Formulario> findByEstado(Formulario.Estado estado);

    /**
     * Busca formularios por código de tienda
     */
    List<Formulario> findByCodigoTienda(String codigoTienda);

    /**
     * Busca formularios por rango de fechas de creación
     */
    @Query("SELECT f FROM Formulario f WHERE f.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ORDER BY f.fechaCreacion DESC")
    List<Formulario> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                               @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Busca formularios por rango de fechas de vigencia
     */
    @Query("SELECT f FROM Formulario f WHERE f.fechaInicio <= :fecha AND f.fechaFin >= :fecha ORDER BY f.fechaCreacion DESC")
    List<Formulario> findFormulariosVigentesEnFecha(@Param("fecha") LocalDate fecha);

    /**
     * Busca formularios activos por tienda
     */
    @Query("SELECT f FROM Formulario f WHERE f.codigoTienda = :codigoTienda AND f.estado = 'ACTIVO' ORDER BY f.fechaCreacion DESC")
    List<Formulario> findFormulariosActivosByTienda(@Param("codigoTienda") String codigoTienda);

    /**
     * Busca formularios por proveedor y estado
     */
    @Query("SELECT f FROM Formulario f WHERE f.proveedor = :proveedor AND f.estado = :estado ORDER BY f.fechaCreacion DESC")
    List<Formulario> findByProveedorAndEstado(@Param("proveedor") Proveedor proveedor, 
                                             @Param("estado") Formulario.Estado estado);

    /**
     * Busca formularios próximos a vencer (en los próximos N días)
     */
    @Query("SELECT f FROM Formulario f WHERE f.fechaFin BETWEEN :hoy AND :fechaLimite AND f.estado = 'ACTIVO' ORDER BY f.fechaFin ASC")
    List<Formulario> findFormulariosProximosAVencer(@Param("hoy") LocalDate hoy, 
                                                   @Param("fechaLimite") LocalDate fechaLimite);

    /**
     * Búsqueda avanzada con múltiples filtros - TEMPORALMENTE DESHABILITADO
     * Problema con tipos de datos en PostgreSQL
     */
    /*
    @Query("SELECT f FROM Formulario f WHERE " +
           "(:codigoTienda IS NULL OR f.codigoTienda = :codigoTienda) AND " +
           "(:proveedorId IS NULL OR f.proveedor.id = :proveedorId) AND " +
           "(:estado IS NULL OR f.estado = :estado) AND " +
           "(:fechaInicio IS NULL OR f.fechaCreacion >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR f.fechaCreacion <= :fechaFin) " +
           "ORDER BY f.fechaCreacion DESC")
    List<Formulario> buscarFormulariosConFiltros(@Param("codigoTienda") String codigoTienda,
                                                @Param("proveedorId") Long proveedorId,
                                                @Param("estado") Formulario.Estado estado,
                                                @Param("fechaInicio") LocalDateTime fechaInicio,
                                                @Param("fechaFin") LocalDateTime fechaFin);
    */

    /**
     * Cuenta formularios por estado
     */
    @Query("SELECT COUNT(f) FROM Formulario f WHERE f.estado = :estado")
    long countByEstado(@Param("estado") Formulario.Estado estado);

    /**
     * Cuenta formularios por tienda y estado
     */
    @Query("SELECT COUNT(f) FROM Formulario f WHERE f.codigoTienda = :codigoTienda AND f.estado = :estado")
    long countByCodigoTiendaAndEstado(@Param("codigoTienda") String codigoTienda, 
                                     @Param("estado") Formulario.Estado estado);

    /**
     * Obtiene formularios por tipo de espacio
     */
    List<Formulario> findByTipoEspacio(Formulario.TipoEspacio tipoEspacio);

    /**
     * Reporte de formularios por proveedor en un período
     */
    @Query("SELECT f FROM Formulario f WHERE f.proveedor.id = :proveedorId AND " +
           "f.fechaCreacion BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY f.fechaCreacion DESC")
    List<Formulario> reporteFormulariosPorProveedorYPeriodo(@Param("proveedorId") Long proveedorId,
                                                          @Param("fechaInicio") LocalDateTime fechaInicio,
                                                          @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Reporte de formularios por tienda en un período
     */
    @Query("SELECT f FROM Formulario f WHERE f.codigoTienda = :codigoTienda AND " +
           "f.fechaCreacion BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY f.fechaCreacion DESC")
    List<Formulario> reporteFormulariosPorTiendaYPeriodo(@Param("codigoTienda") String codigoTienda,
                                                       @Param("fechaInicio") LocalDateTime fechaInicio,
                                                       @Param("fechaFin") LocalDateTime fechaFin);
}
