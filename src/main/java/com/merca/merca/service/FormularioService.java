package com.merca.merca.service;

import com.merca.merca.entity.Formulario;
import com.merca.merca.entity.Proveedor;
import com.merca.merca.entity.Usuario;
import com.merca.merca.repository.FormularioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FormularioService {

    @Autowired
    private FormularioRepository formularioRepository;

    /**
     * Registra un nuevo formulario
     */
    public Formulario registrarFormulario(Formulario formulario) {
        // Validar fechas
        if (formulario.getFechaFin().isBefore(formulario.getFechaInicio())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        // Validar que la fecha de inicio no sea anterior a hoy
        if (formulario.getFechaInicio().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de inicio no puede ser anterior a la fecha actual");
        }

        formulario.setFechaCreacion(LocalDateTime.now());
        formulario.setEstado(Formulario.Estado.ACTIVO);

        return formularioRepository.save(formulario);
    }

    /**
     * Actualiza un formulario existente
     */
    public Formulario actualizarFormulario(Formulario formulario) {
        Optional<Formulario> formularioExistente = formularioRepository.findById(formulario.getId());
        if (formularioExistente.isPresent()) {
            Formulario formularioActual = formularioExistente.get();

            // Validar fechas
            if (formulario.getFechaFin().isBefore(formulario.getFechaInicio())) {
                throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
            }

            // Actualizar campos
            formularioActual.setNombreTienda(formulario.getNombreTienda());
            formularioActual.setCodigoTienda(formulario.getCodigoTienda());
            formularioActual.setProveedor(formulario.getProveedor());
            formularioActual.setAreaAsignada(formulario.getAreaAsignada());
            formularioActual.setTipoEspacio(formulario.getTipoEspacio());
            formularioActual.setMetrosCuadrados(formulario.getMetrosCuadrados());
            formularioActual.setNumeroProductos(formulario.getNumeroProductos());
            formularioActual.setFechaInicio(formulario.getFechaInicio());
            formularioActual.setFechaFin(formulario.getFechaFin());
            formularioActual.setPrecioAcordado(formulario.getPrecioAcordado());
            formularioActual.setObservaciones(formulario.getObservaciones());
            formularioActual.setEstado(formulario.getEstado());
            formularioActual.setFechaActualizacion(LocalDateTime.now());

            return formularioRepository.save(formularioActual);
        }
        throw new RuntimeException("Formulario no encontrado");
    }

    /**
     * Busca un formulario por ID
     */
    public Optional<Formulario> buscarPorId(Long id) {
        return formularioRepository.findById(id);
    }

    /**
     * Obtiene todos los formularios
     */
    public List<Formulario> obtenerTodosLosFormularios() {
        return formularioRepository.findAll();
    }

    /**
     * Obtiene formularios por usuario
     */
    public List<Formulario> obtenerFormulariosPorUsuario(Usuario usuario) {
        return formularioRepository.findByUsuario(usuario);
    }

    /**
     * Obtiene formularios por proveedor
     */
    public List<Formulario> obtenerFormulariosPorProveedor(Proveedor proveedor) {
        return formularioRepository.findByProveedor(proveedor);
    }

    /**
     * Obtiene formularios por estado
     */
    public List<Formulario> obtenerFormulariosPorEstado(Formulario.Estado estado) {
        return formularioRepository.findByEstado(estado);
    }

    /**
     * Obtiene formularios por código de tienda
     */
    public List<Formulario> obtenerFormulariosPorTienda(String codigoTienda) {
        return formularioRepository.findByCodigoTienda(codigoTienda);
    }

    /**
     * Obtiene formularios activos por tienda
     */
    public List<Formulario> obtenerFormulariosActivosPorTienda(String codigoTienda) {
        return formularioRepository.findFormulariosActivosByTienda(codigoTienda);
    }

    /**
     * Obtiene formularios por rango de fechas
     */
    public List<Formulario> obtenerFormulariosPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return formularioRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
    }

    /**
     * Obtiene formularios vigentes en una fecha específica
     */
    public List<Formulario> obtenerFormulariosVigentesEnFecha(LocalDate fecha) {
        return formularioRepository.findFormulariosVigentesEnFecha(fecha);
    }

    /**
     * Obtiene formularios próximos a vencer
     */
    public List<Formulario> obtenerFormulariosProximosAVencer(int diasAntelacion) {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = hoy.plusDays(diasAntelacion);
        return formularioRepository.findFormulariosProximosAVencer(hoy, fechaLimite);
    }

    /**
     * Búsqueda avanzada con filtros
     */
    public List<Formulario> buscarFormulariosConFiltros(String codigoTienda, Long proveedorId, 
                                                       Formulario.Estado estado, LocalDateTime fechaInicio, 
                                                       LocalDateTime fechaFin) {
        
        // Si no hay filtros, devolver todos los formularios
        if (codigoTienda == null && proveedorId == null && estado == null && 
            fechaInicio == null && fechaFin == null) {
            return formularioRepository.findAll();
        }
        
        // Construir la consulta de forma programática para evitar problemas con PostgreSQL
        List<Formulario> resultados = formularioRepository.findAll();
        
        return resultados.stream()
            .filter(f -> codigoTienda == null || (f.getCodigoTienda() != null && f.getCodigoTienda().equals(codigoTienda)))
            .filter(f -> proveedorId == null || (f.getProveedor() != null && f.getProveedor().getId().equals(proveedorId)))
            .filter(f -> estado == null || f.getEstado().equals(estado))
            .filter(f -> fechaInicio == null || f.getFechaCreacion().isAfter(fechaInicio.minusSeconds(1)))
            .filter(f -> fechaFin == null || f.getFechaCreacion().isBefore(fechaFin.plusSeconds(1)))
            .sorted((f1, f2) -> f2.getFechaCreacion().compareTo(f1.getFechaCreacion()))
            .toList();
    }

    /**
     * Cancelar un formulario
     */
    public void cancelarFormulario(Long formularioId) {
        Optional<Formulario> formulario = formularioRepository.findById(formularioId);
        if (formulario.isPresent()) {
            Formulario formularioActual = formulario.get();
            formularioActual.setEstado(Formulario.Estado.CANCELADO);
            formularioActual.setFechaActualizacion(LocalDateTime.now());
            formularioRepository.save(formularioActual);
        } else {
            throw new RuntimeException("Formulario no encontrado");
        }
    }

    /**
     * Activar un formulario
     */
    public void activarFormulario(Long formularioId) {
        Optional<Formulario> formulario = formularioRepository.findById(formularioId);
        if (formulario.isPresent()) {
            Formulario formularioActual = formulario.get();
            formularioActual.setEstado(Formulario.Estado.ACTIVO);
            formularioActual.setFechaActualizacion(LocalDateTime.now());
            formularioRepository.save(formularioActual);
        } else {
            throw new RuntimeException("Formulario no encontrado");
        }
    }

    /**
     * Eliminar un formulario
     */
    public void eliminarFormulario(Long formularioId) {
        if (formularioRepository.existsById(formularioId)) {
            formularioRepository.deleteById(formularioId);
        } else {
            throw new RuntimeException("Formulario no encontrado");
        }
    }

    /**
     * Actualizar estados de formularios vencidos
     */
    public void actualizarFormulariosVencidos() {
        LocalDate hoy = LocalDate.now();
        List<Formulario> formulariosActivos = obtenerFormulariosPorEstado(Formulario.Estado.ACTIVO);
        
        for (Formulario formulario : formulariosActivos) {
            if (formulario.getFechaFin().isBefore(hoy)) {
                formulario.setEstado(Formulario.Estado.VENCIDO);
                formulario.setFechaActualizacion(LocalDateTime.now());
                formularioRepository.save(formulario);
            }
        }
    }

    /**
     * Obtiene estadísticas de formularios por estado
     */
    public long contarFormulariosPorEstado(Formulario.Estado estado) {
        return formularioRepository.countByEstado(estado);
    }

    /**
     * Obtiene estadísticas de formularios por tienda y estado
     */
    public long contarFormulariosPorTiendaYEstado(String codigoTienda, Formulario.Estado estado) {
        return formularioRepository.countByCodigoTiendaAndEstado(codigoTienda, estado);
    }

    /**
     * Reporte de formularios por proveedor en un período
     */
    public List<Formulario> reporteFormulariosPorProveedorYPeriodo(Long proveedorId, 
                                                                 LocalDateTime fechaInicio, 
                                                                 LocalDateTime fechaFin) {
        return formularioRepository.reporteFormulariosPorProveedorYPeriodo(proveedorId, fechaInicio, fechaFin);
    }

    /**
     * Reporte de formularios por tienda en un período
     */
    public List<Formulario> reporteFormulariosPorTiendaYPeriodo(String codigoTienda, 
                                                              LocalDateTime fechaInicio, 
                                                              LocalDateTime fechaFin) {
        return formularioRepository.reporteFormulariosPorTiendaYPeriodo(codigoTienda, fechaInicio, fechaFin);
    }

    /**
     * Obtiene formularios por tipo de espacio
     */
    public List<Formulario> obtenerFormulariosPorTipoEspacio(Formulario.TipoEspacio tipoEspacio) {
        return formularioRepository.findByTipoEspacio(tipoEspacio);
    }

    /**
     * Verifica si un usuario puede editar un formulario
     */
    public boolean puedeEditarFormulario(Usuario usuario, Long formularioId) {
        Optional<Formulario> formulario = buscarPorId(formularioId);
        if (formulario.isPresent()) {
            Formulario form = formulario.get();
            
            // Los administradores pueden editar cualquier formulario
            if (usuario.getRol() == Usuario.Rol.ADMINISTRADOR) {
                return true;
            }
            
            // Los usuarios comerciales pueden editar cualquier formulario
            if (usuario.getRol() == Usuario.Rol.COMERCIAL) {
                return true;
            }
            
            // Los usuarios de tienda solo pueden editar sus propios formularios
            if (usuario.getRol() == Usuario.Rol.TIENDA) {
                return form.getUsuario().getId().equals(usuario.getId());
            }
        }
        return false;
    }
}
