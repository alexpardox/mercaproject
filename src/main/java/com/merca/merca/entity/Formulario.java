package com.merca.merca.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "formularios")
public class Formulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la tienda es obligatorio")
    @Size(max = 100, message = "El nombre de la tienda no puede exceder 100 caracteres")
    @Column(name = "nombre_tienda", nullable = false)
    private String nombreTienda;

    @NotBlank(message = "El código de la tienda es obligatorio")
    @Size(max = 20, message = "El código de la tienda no puede exceder 20 caracteres")
    @Column(name = "codigo_tienda", nullable = false)
    private String codigoTienda;

    @NotNull(message = "El proveedor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank(message = "El área asignada es obligatoria")
    @Size(max = 100, message = "El área asignada no puede exceder 100 caracteres")
    @Column(name = "area_asignada", nullable = false)
    private String areaAsignada;

    @NotNull(message = "El tipo de espacio es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_espacio", nullable = false)
    private TipoEspacio tipoEspacio;

    @Column(name = "metros_cuadrados", columnDefinition = "NUMERIC")
    private Double metrosCuadrados;

    @Column(name = "numero_productos")
    private Integer numeroProductos;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "precio_acordado", columnDefinition = "NUMERIC")
    private Double precioAcordado;

    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.PENDIENTE_APROBACION;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public enum TipoEspacio {
        GONDOLA("Góndola"),
        EXHIBIDOR("Exhibidor"),
        ISLA("Isla central"),
        ENTRADA("Entrada de tienda"),
        CAJA("Área de caja"),
        PASILLO("Pasillo principal"),
        REFRIGERADOR("Refrigerador"),
        CONGELADOR("Congelador"),
        OTRO("Otro");

        private final String descripcion;

        TipoEspacio(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public enum Estado {
        PENDIENTE_APROBACION,
        ACTIVO,
        VENCIDO,
        CANCELADO
    }

    // Constructores
    public Formulario() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Formulario(String nombreTienda, String codigoTienda, Proveedor proveedor, Usuario usuario,
                     String areaAsignada, TipoEspacio tipoEspacio, LocalDate fechaInicio, LocalDate fechaFin) {
        this();
        this.nombreTienda = nombreTienda;
        this.codigoTienda = codigoTienda;
        this.proveedor = proveedor;
        this.usuario = usuario;
        this.areaAsignada = areaAsignada;
        this.tipoEspacio = tipoEspacio;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        
        // Actualizar estado basado en fechas
        if (LocalDate.now().isAfter(fechaFin)) {
            this.estado = Estado.VENCIDO;
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getCodigoTienda() {
        return codigoTienda;
    }

    public void setCodigoTienda(String codigoTienda) {
        this.codigoTienda = codigoTienda;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getAreaAsignada() {
        return areaAsignada;
    }

    public void setAreaAsignada(String areaAsignada) {
        this.areaAsignada = areaAsignada;
    }

    public TipoEspacio getTipoEspacio() {
        return tipoEspacio;
    }

    public void setTipoEspacio(TipoEspacio tipoEspacio) {
        this.tipoEspacio = tipoEspacio;
    }

    public Double getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(Double metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public Integer getNumeroProductos() {
        return numeroProductos;
    }

    public void setNumeroProductos(Integer numeroProductos) {
        this.numeroProductos = numeroProductos;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getPrecioAcordado() {
        return precioAcordado;
    }

    public void setPrecioAcordado(Double precioAcordado) {
        this.precioAcordado = precioAcordado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @Override
    public String toString() {
        return "Formulario{" +
                "id=" + id +
                ", nombreTienda='" + nombreTienda + '\'' +
                ", codigoTienda='" + codigoTienda + '\'' +
                ", areaAsignada='" + areaAsignada + '\'' +
                ", tipoEspacio=" + tipoEspacio +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                '}';
    }
}
