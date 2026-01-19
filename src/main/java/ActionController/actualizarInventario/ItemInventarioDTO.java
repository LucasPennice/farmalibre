package ActionController.actualizarInventario;

public class ItemInventarioDTO {
    private String composicion;
    private String drogaId;
    private String nombreDroga;
    private Integer disponible;
    private Double precioUnitario;
    private String nombreCategoria;
    private String unidad;
    private Boolean aprobacion_pendiente;
    
    public String getDrogaId() {
        return drogaId;
    }
    public void setDrogaId(String drogaId) {
        this.drogaId = drogaId;
    }
    
    public String getUnidad() {
        return unidad;
    }
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
    
    public String getComposicion() {
        return composicion;
    }
    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }
    public String getNombreDroga() {
        return nombreDroga;
    }
    public void setNombreDroga(String nombreDroga) {
        this.nombreDroga = nombreDroga;
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
    public String getNombreCategoria() {
        return nombreCategoria;
    }
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Boolean getAprobacion_pendiente() {
        return aprobacion_pendiente;
    }
    public void setAprobacion_pendiente(Boolean aprobacion_pendiente) {
        this.aprobacion_pendiente = aprobacion_pendiente;
    }
}
