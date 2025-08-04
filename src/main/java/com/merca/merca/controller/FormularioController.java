package com.merca.merca.controller;

import com.merca.merca.entity.Formulario;
import com.merca.merca.entity.Proveedor;
import com.merca.merca.entity.Usuario;
import com.merca.merca.service.FormularioService;
import com.merca.merca.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/formularios")
public class FormularioController {

    @Autowired
    private FormularioService formularioService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listarFormularios(@RequestParam(value = "tienda", required = false) String tienda,
                                   @RequestParam(value = "proveedor", required = false) Long proveedorId,
                                   @RequestParam(value = "estado", required = false) String estado,
                                   @RequestParam(value = "fechaInicio", required = false) 
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                   @RequestParam(value = "fechaFin", required = false) 
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                   @AuthenticationPrincipal Usuario usuario,
                                   Model model) {
        
        List<Formulario> formularios;
        
        // Convertir fechas a LocalDateTime si están presentes
        LocalDateTime fechaInicioDateTime = null;
        LocalDateTime fechaFinDateTime = null;
        
        if (fechaInicio != null) {
            fechaInicioDateTime = fechaInicio.atStartOfDay();
        }
        
        if (fechaFin != null) {
            fechaFinDateTime = fechaFin.atTime(LocalTime.MAX);
        }
        
        Formulario.Estado estadoFiltro = null;
        if (estado != null && !estado.isEmpty()) {
            try {
                estadoFiltro = Formulario.Estado.valueOf(estado);
            } catch (IllegalArgumentException e) {
                // Ignorar estado inválido
            }
        }

        // Filtrar según el rol del usuario
        if (usuario.getRol() == Usuario.Rol.TIENDA) {
            // Los usuarios de tienda solo ven sus formularios o los de su tienda
            String tiendaFiltro = usuario.getTiendaAsignada();
            formularios = formularioService.buscarFormulariosConFiltros(tiendaFiltro, proveedorId, 
                                                                        estadoFiltro, fechaInicioDateTime, fechaFinDateTime);
        } else {
            // Comercial y Admin ven todos los formularios con filtros
            formularios = formularioService.buscarFormulariosConFiltros(tienda, proveedorId, 
                                                                        estadoFiltro, fechaInicioDateTime, fechaFinDateTime);
        }

        model.addAttribute("formularios", formularios);
        model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivosOrdenados());
        model.addAttribute("estados", Formulario.Estado.values());
        
        // Mantener filtros en el modelo
        model.addAttribute("tiendaSeleccionada", tienda);
        model.addAttribute("proveedorSeleccionado", proveedorId);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        
        // Estadísticas
        model.addAttribute("totalActivos", formularioService.contarFormulariosPorEstado(Formulario.Estado.ACTIVO));
        model.addAttribute("totalVencidos", formularioService.contarFormulariosPorEstado(Formulario.Estado.VENCIDO));
        model.addAttribute("totalCancelados", formularioService.contarFormulariosPorEstado(Formulario.Estado.CANCELADO));

        return "formularios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(@AuthenticationPrincipal Usuario usuario, Model model) {
        Formulario formulario = new Formulario();
        
        // Pre-llenar datos del usuario de tienda
        if (usuario.getRol() == Usuario.Rol.TIENDA && usuario.getTiendaAsignada() != null) {
            formulario.setCodigoTienda(usuario.getTiendaAsignada());
            formulario.setNombreTienda(usuario.getTiendaAsignada()); // Se puede mejorar con un catálogo de tiendas
        }
        
        model.addAttribute("formulario", formulario);
        model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivosOrdenados());
        model.addAttribute("tiposEspacio", Formulario.TipoEspacio.values());
        
        return "formularios/formulario";
    }

    @PostMapping("/nuevo")
    public String crearFormulario(@Valid @ModelAttribute Formulario formulario,
                                 BindingResult result,
                                 @AuthenticationPrincipal Usuario usuario,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivosOrdenados());
            model.addAttribute("tiposEspacio", Formulario.TipoEspacio.values());
            return "formularios/formulario";
        }

        try {
            // Asignar el usuario actual
            formulario.setUsuario(usuario);
            
            formularioService.registrarFormulario(formulario);
            redirectAttributes.addFlashAttribute("success", "Formulario registrado exitosamente");
            return "redirect:/formularios";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivosOrdenados());
            model.addAttribute("tiposEspacio", Formulario.TipoEspacio.values());
            return "formularios/formulario";
        }
    }

    @GetMapping("/{id}")
    public String verFormulario(@PathVariable Long id, 
                               @AuthenticationPrincipal Usuario usuario,
                               Model model) {
        Optional<Formulario> formulario = formularioService.buscarPorId(id);
        if (formulario.isPresent()) {
            Formulario form = formulario.get();
            
            // Verificar permisos de visualización
            if (usuario.getRol() == Usuario.Rol.TIENDA && 
                !form.getUsuario().getId().equals(usuario.getId()) &&
                !form.getCodigoTienda().equals(usuario.getTiendaAsignada())) {
                return "redirect:/formularios?error=No tienes permisos para ver este formulario";
            }
            
            model.addAttribute("formulario", form);
            model.addAttribute("puedeEditar", formularioService.puedeEditarFormulario(usuario, id));
            return "formularios/detalle";
        } else {
            return "redirect:/formularios?error=Formulario no encontrado";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id,
                                         @AuthenticationPrincipal Usuario usuario,
                                         Model model) {
        Optional<Formulario> formulario = formularioService.buscarPorId(id);
        if (formulario.isPresent()) {
            Formulario form = formulario.get();
            
            // Verificar permisos de edición
            if (!formularioService.puedeEditarFormulario(usuario, id)) {
                return "redirect:/formularios/" + id + "?error=No tienes permisos para editar este formulario";
            }
            
            model.addAttribute("formulario", form);
            model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivosOrdenados());
            model.addAttribute("tiposEspacio", Formulario.TipoEspacio.values());
            model.addAttribute("estados", Formulario.Estado.values());
            return "formularios/formulario";
        } else {
            return "redirect:/formularios?error=Formulario no encontrado";
        }
    }

    @PostMapping("/{id}/editar")
    public String actualizarFormulario(@PathVariable Long id,
                                      @Valid @ModelAttribute Formulario formulario,
                                      BindingResult result,
                                      @AuthenticationPrincipal Usuario usuario,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        
        // Verificar permisos
        if (!formularioService.puedeEditarFormulario(usuario, id)) {
            return "redirect:/formularios/" + id + "?error=No tienes permisos para editar este formulario";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivosOrdenados());
            model.addAttribute("tiposEspacio", Formulario.TipoEspacio.values());
            model.addAttribute("estados", Formulario.Estado.values());
            return "formularios/formulario";
        }

        try {
            formulario.setId(id);
            formularioService.actualizarFormulario(formulario);
            redirectAttributes.addFlashAttribute("success", "Formulario actualizado exitosamente");
            return "redirect:/formularios/" + id;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivosOrdenados());
            model.addAttribute("tiposEspacio", Formulario.TipoEspacio.values());
            model.addAttribute("estados", Formulario.Estado.values());
            return "formularios/formulario";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarFormulario(@PathVariable Long id,
                                    @AuthenticationPrincipal Usuario usuario,
                                    RedirectAttributes redirectAttributes) {
        if (!formularioService.puedeEditarFormulario(usuario, id)) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para cancelar este formulario");
            return "redirect:/formularios/" + id;
        }
        
        try {
            formularioService.cancelarFormulario(id);
            redirectAttributes.addFlashAttribute("success", "Formulario cancelado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/formularios/" + id;
    }

    @PostMapping("/{id}/activar")
    public String activarFormulario(@PathVariable Long id,
                                   @AuthenticationPrincipal Usuario usuario,
                                   RedirectAttributes redirectAttributes) {
        if (!formularioService.puedeEditarFormulario(usuario, id)) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para activar este formulario");
            return "redirect:/formularios/" + id;
        }
        
        try {
            formularioService.activarFormulario(id);
            redirectAttributes.addFlashAttribute("success", "Formulario activado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/formularios/" + id;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarFormulario(@PathVariable Long id,
                                    @AuthenticationPrincipal Usuario usuario,
                                    RedirectAttributes redirectAttributes) {
        // Solo administradores pueden eliminar
        if (usuario.getRol() != Usuario.Rol.ADMINISTRADOR) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar este formulario");
            return "redirect:/formularios/" + id;
        }
        
        try {
            formularioService.eliminarFormulario(id);
            redirectAttributes.addFlashAttribute("success", "Formulario eliminado exitosamente");
            return "redirect:/formularios";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/formularios/" + id;
        }
    }
}
