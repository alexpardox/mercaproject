package com.merca.merca.controller;

import com.merca.merca.entity.Formulario;
import com.merca.merca.entity.Usuario;
import com.merca.merca.service.FormularioService;
import com.merca.merca.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tienda")
public class TiendaController {

    @Autowired
    private FormularioService formularioService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal Usuario usuario, Model model) {
        
        // Estadísticas específicas para la tienda del usuario
        String tienda = usuario.getTiendaAsignada();
        
        if (tienda != null) {
            // Formularios activos de la tienda
            List<Formulario> formulariosActivos = formularioService.obtenerFormulariosActivosPorTienda(tienda);
            
            // Formularios próximos a vencer (en los próximos 7 días)
            List<Formulario> formulariosProximosAVencer = formularioService.obtenerFormulariosProximosAVencer(7);
            
            // Filtrar solo los de esta tienda
            formulariosProximosAVencer = formulariosProximosAVencer.stream()
                    .filter(f -> tienda.equals(f.getCodigoTienda()))
                    .toList();
            
            // Estadísticas
            long totalActivos = formularioService.contarFormulariosPorTiendaYEstado(tienda, Formulario.Estado.ACTIVO);
            long totalVencidos = formularioService.contarFormulariosPorTiendaYEstado(tienda, Formulario.Estado.VENCIDO);
            long totalCancelados = formularioService.contarFormulariosPorTiendaYEstado(tienda, Formulario.Estado.CANCELADO);
            
            model.addAttribute("formulariosActivos", formulariosActivos);
            model.addAttribute("formulariosProximosAVencer", formulariosProximosAVencer);
            model.addAttribute("totalActivos", totalActivos);
            model.addAttribute("totalVencidos", totalVencidos);
            model.addAttribute("totalCancelados", totalCancelados);
            model.addAttribute("tienda", tienda);
        }
        
        // Información del usuario
        model.addAttribute("usuario", usuario);
        
        return "tienda/dashboard";
    }

    @GetMapping("/formularios")
    public String misFormularios(@AuthenticationPrincipal Usuario usuario, Model model) {
        List<Formulario> formularios = formularioService.obtenerFormulariosPorUsuario(usuario);
        model.addAttribute("formularios", formularios);
        model.addAttribute("usuario", usuario);
        return "tienda/mis-formularios";
    }
}
