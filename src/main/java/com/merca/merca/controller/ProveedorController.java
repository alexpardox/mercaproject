package com.merca.merca.controller;

import com.merca.merca.entity.Proveedor;
import com.merca.merca.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public String listarProveedores(@RequestParam(value = "buscar", required = false) String buscar,
                                   @RequestParam(value = "estado", required = false) String estado,
                                   Model model) {
        List<Proveedor> proveedores;
        
        Proveedor.Estado estadoFiltro = null;
        if (estado != null && !estado.isEmpty()) {
            try {
                estadoFiltro = Proveedor.Estado.valueOf(estado);
            } catch (IllegalArgumentException e) {
                estadoFiltro = Proveedor.Estado.ACTIVO;
            }
        } else {
            estadoFiltro = Proveedor.Estado.ACTIVO;
        }

        if (buscar != null && !buscar.trim().isEmpty()) {
            proveedores = proveedorService.buscarProveedores(buscar, estadoFiltro);
            model.addAttribute("buscar", buscar);
        } else {
            proveedores = proveedorService.obtenerProveedoresPorEstado(estadoFiltro);
        }

        model.addAttribute("proveedores", proveedores);
        model.addAttribute("estado", estadoFiltro);
        model.addAttribute("estados", Proveedor.Estado.values());
        
        // Estadísticas
        model.addAttribute("totalActivos", proveedorService.contarProveedoresPorEstado(Proveedor.Estado.ACTIVO));
        model.addAttribute("totalInactivos", proveedorService.contarProveedoresPorEstado(Proveedor.Estado.INACTIVO));
        model.addAttribute("totalSuspendidos", proveedorService.contarProveedoresPorEstado(Proveedor.Estado.SUSPENDIDO));

        return "proveedores/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("estados", Proveedor.Estado.values());
        return "proveedores/formulario";
    }

    @PostMapping("/nuevo")
    public String crearProveedor(@Valid @ModelAttribute Proveedor proveedor,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("estados", Proveedor.Estado.values());
            return "proveedores/formulario";
        }

        try {
            proveedorService.registrarProveedor(proveedor);
            redirectAttributes.addFlashAttribute("success", "Proveedor registrado exitosamente");
            return "redirect:/proveedores";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("estados", Proveedor.Estado.values());
            return "proveedores/formulario";
        }
    }

    @GetMapping("/{id}")
    public String verProveedor(@PathVariable Long id, Model model) {
        Optional<Proveedor> proveedor = proveedorService.buscarPorId(id);
        if (proveedor.isPresent()) {
            model.addAttribute("proveedor", proveedor.get());
            return "proveedores/detalle";
        } else {
            return "redirect:/proveedores?error=Proveedor no encontrado";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Proveedor> proveedor = proveedorService.buscarPorId(id);
        if (proveedor.isPresent()) {
            model.addAttribute("proveedor", proveedor.get());
            model.addAttribute("estados", Proveedor.Estado.values());
            return "proveedores/formulario";
        } else {
            return "redirect:/proveedores?error=Proveedor no encontrado";
        }
    }

    @PostMapping("/{id}/editar")
    public String actualizarProveedor(@PathVariable Long id,
                                     @Valid @ModelAttribute Proveedor proveedor,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("estados", Proveedor.Estado.values());
            return "proveedores/formulario";
        }

        try {
            proveedor.setId(id);
            proveedorService.actualizarProveedor(proveedor);
            redirectAttributes.addFlashAttribute("success", "Proveedor actualizado exitosamente");
            return "redirect:/proveedores/" + id;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("estados", Proveedor.Estado.values());
            return "proveedores/formulario";
        }
    }

    @PostMapping("/{id}/activar")
    public String activarProveedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.activarProveedor(id);
            redirectAttributes.addFlashAttribute("success", "Proveedor activado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedores/" + id;
    }

    @PostMapping("/{id}/desactivar")
    public String desactivarProveedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.desactivarProveedor(id);
            redirectAttributes.addFlashAttribute("success", "Proveedor desactivado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedores/" + id;
    }

    @PostMapping("/{id}/suspender")
    public String suspenderProveedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.suspenderProveedor(id);
            redirectAttributes.addFlashAttribute("success", "Proveedor suspendido exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedores/" + id;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarProveedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.eliminarProveedor(id);
            redirectAttributes.addFlashAttribute("success", "Proveedor eliminado exitosamente");
            return "redirect:/proveedores";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/proveedores/" + id;
        }
    }
}
