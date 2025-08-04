package com.merca.merca.service;

import com.merca.merca.entity.Proveedor;
import com.merca.merca.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    /**
     * Registra un nuevo proveedor
     */
    public Proveedor registrarProveedor(Proveedor proveedor) {
        // Verificar que el RFC no exista
        if (proveedorRepository.existsByRfc(proveedor.getRfc())) {
            throw new RuntimeException("El RFC ya está registrado");
        }

        // Verificar que el email no exista si se proporciona
        if (proveedor.getEmail() != null && !proveedor.getEmail().isEmpty() && 
            proveedorRepository.existsByEmail(proveedor.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        proveedor.setFechaRegistro(LocalDateTime.now());
        return proveedorRepository.save(proveedor);
    }

    /**
     * Actualiza un proveedor existente
     */
    public Proveedor actualizarProveedor(Proveedor proveedor) {
        Optional<Proveedor> proveedorExistente = proveedorRepository.findById(proveedor.getId());
        if (proveedorExistente.isPresent()) {
            Proveedor proveedorActual = proveedorExistente.get();

            // Verificar RFC único (si ha cambiado)
            if (!proveedorActual.getRfc().equals(proveedor.getRfc()) && 
                proveedorRepository.existsByRfc(proveedor.getRfc())) {
                throw new RuntimeException("El RFC ya está registrado");
            }

            // Verificar email único (si ha cambiado y no está vacío)
            if (proveedor.getEmail() != null && !proveedor.getEmail().isEmpty() &&
                !proveedor.getEmail().equals(proveedorActual.getEmail()) &&
                proveedorRepository.existsByEmail(proveedor.getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }

            // Actualizar campos
            proveedorActual.setNombre(proveedor.getNombre());
            proveedorActual.setRfc(proveedor.getRfc());
            proveedorActual.setRazonSocial(proveedor.getRazonSocial());
            proveedorActual.setEmail(proveedor.getEmail());
            proveedorActual.setTelefono(proveedor.getTelefono());
            proveedorActual.setDireccion(proveedor.getDireccion());
            proveedorActual.setContactoPrincipal(proveedor.getContactoPrincipal());
            proveedorActual.setEstado(proveedor.getEstado());
            proveedorActual.setFechaActualizacion(LocalDateTime.now());

            return proveedorRepository.save(proveedorActual);
        }
        throw new RuntimeException("Proveedor no encontrado");
    }

    /**
     * Busca un proveedor por ID
     */
    public Optional<Proveedor> buscarPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    /**
     * Busca un proveedor por RFC
     */
    public Optional<Proveedor> buscarPorRfc(String rfc) {
        return proveedorRepository.findByRfc(rfc);
    }

    /**
     * Busca un proveedor por email
     */
    public Optional<Proveedor> buscarPorEmail(String email) {
        return proveedorRepository.findByEmail(email);
    }

    /**
     * Obtiene todos los proveedores
     */
    public List<Proveedor> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll();
    }

    /**
     * Obtiene proveedores por estado
     */
    public List<Proveedor> obtenerProveedoresPorEstado(Proveedor.Estado estado) {
        return proveedorRepository.findByEstado(estado);
    }

    /**
     * Obtiene proveedores activos ordenados
     */
    public List<Proveedor> obtenerProveedoresActivosOrdenados() {
        return proveedorRepository.findProveedoresActivosOrdenados();
    }

    /**
     * Busca proveedores por nombre
     */
    public List<Proveedor> buscarPorNombre(String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Busca proveedores por razón social
     */
    public List<Proveedor> buscarPorRazonSocial(String razonSocial) {
        return proveedorRepository.findByRazonSocialContainingIgnoreCase(razonSocial);
    }

    /**
     * Búsqueda general de proveedores
     */
    public List<Proveedor> buscarProveedores(String texto, Proveedor.Estado estado) {
        if (texto == null || texto.trim().isEmpty()) {
            return obtenerProveedoresPorEstado(estado);
        }
        return proveedorRepository.buscarProveedores(texto.trim(), estado);
    }

    /**
     * Activa un proveedor
     */
    public void activarProveedor(Long proveedorId) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(proveedorId);
        if (proveedor.isPresent()) {
            Proveedor proveedorActual = proveedor.get();
            proveedorActual.setEstado(Proveedor.Estado.ACTIVO);
            proveedorActual.setFechaActualizacion(LocalDateTime.now());
            proveedorRepository.save(proveedorActual);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }

    /**
     * Desactiva un proveedor
     */
    public void desactivarProveedor(Long proveedorId) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(proveedorId);
        if (proveedor.isPresent()) {
            Proveedor proveedorActual = proveedor.get();
            proveedorActual.setEstado(Proveedor.Estado.INACTIVO);
            proveedorActual.setFechaActualizacion(LocalDateTime.now());
            proveedorRepository.save(proveedorActual);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }

    /**
     * Suspende un proveedor
     */
    public void suspenderProveedor(Long proveedorId) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(proveedorId);
        if (proveedor.isPresent()) {
            Proveedor proveedorActual = proveedor.get();
            proveedorActual.setEstado(Proveedor.Estado.SUSPENDIDO);
            proveedorActual.setFechaActualizacion(LocalDateTime.now());
            proveedorRepository.save(proveedorActual);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }

    /**
     * Elimina un proveedor (solo si no tiene formularios asociados)
     */
    public void eliminarProveedor(Long proveedorId) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(proveedorId);
        if (proveedor.isPresent()) {
            Proveedor proveedorActual = proveedor.get();
            if (proveedorActual.getFormularios() != null && !proveedorActual.getFormularios().isEmpty()) {
                throw new RuntimeException("No se puede eliminar el proveedor porque tiene formularios asociados");
            }
            proveedorRepository.deleteById(proveedorId);
        } else {
            throw new RuntimeException("Proveedor no encontrado");
        }
    }

    /**
     * Obtiene estadísticas de proveedores por estado
     */
    public long contarProveedoresPorEstado(Proveedor.Estado estado) {
        return proveedorRepository.countByEstado(estado);
    }

    /**
     * Obtiene proveedores con formularios activos
     */
    public List<Proveedor> obtenerProveedoresConFormulariosActivos() {
        return proveedorRepository.findProveedoresConFormulariosActivos();
    }

    /**
     * Verifica si un RFC está disponible
     */
    public boolean isRfcDisponible(String rfc) {
        return !proveedorRepository.existsByRfc(rfc);
    }

    /**
     * Verifica si un email está disponible
     */
    public boolean isEmailDisponible(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true;
        }
        return !proveedorRepository.existsByEmail(email);
    }
}
