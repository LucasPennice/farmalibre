package Carrito;

import Droga.Droga;
import Proveedor.Proveedor;

public class ItemCarrito {
    private Integer id;
    private Droga droga;
    private Integer cantidad;
    private Proveedor proveedor;
    private Double precioUnitario;
    
    // Constructores
    public ItemCarrito() {}
    
    public ItemCarrito(Droga droga, Integer cantidad, Proveedor proveedor, Double precioUnitario) {
        this.droga = droga;
        this.cantidad = cantidad;
        this.proveedor = proveedor;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Droga getDroga() { return droga; }
    public void setDroga(Droga droga) { this.droga = droga; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    
    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
}