package com.merca.merca.controller;

import com.merca.merca.service.FormularioService;
import com.merca.merca.service.ProveedorService;
import com.merca.merca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ProveedorService proveedorService;
    
    @Autowired
    private FormularioService formularioService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Estadísticas generales para el administrador
        model.addAttribute("totalUsuarios", usuarioService.obtenerTodosLosUsuarios().size());
        model.addAttribute("totalProveedores", proveedorService.obtenerTodosLosProveedores().size());
        model.addAttribute("totalFormularios", formularioService.obtenerTodosLosFormularios().size());
        
        // Datos básicos para el dashboard
        model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivosOrdenados());
        model.addAttribute("formularios", formularioService.obtenerTodosLosFormularios());
        
        return "admin/dashboard";
    }
    
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "admin/usuarios";
    }
    
    @GetMapping("/reportes")
    public String reportes(Model model) {
        // Datos para reportes
        model.addAttribute("proveedores", proveedorService.obtenerTodosLosProveedores());
        model.addAttribute("formularios", formularioService.obtenerTodosLosFormularios());
        return "admin/reportes";
    }
}
