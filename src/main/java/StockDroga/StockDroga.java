package StockDroga;

import Droga.Droga;
import Proveedor.Proveedor;

public class StockDroga {
    private Integer id;
    private Droga droga;
    private Proveedor proveedor;
    private Integer disponible;
    private Double precioUnitario;

    public StockDroga() {
    }

    public StockDroga(Integer id, Droga droga, Proveedor proveedor, Integer disponible, Double precioUnitario) {
        this.id = id;
        this.droga = droga;
        this.proveedor = proveedor;
        this.disponible = disponible;
        this.precioUnitario = precioUnitario;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Droga getDroga() {
        return droga;
    }

    public void setDroga(Droga droga) {
        this.droga = droga;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Integer getDisponible() {
        return disponible;
    }

    public void setDisponible(Integer disponible) {
        this.disponible = disponible;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
