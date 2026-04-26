package Usuario;

public class WishlistItem {
    private Integer stockId;
    private Boolean tieneDisponibilidad;
    private Integer drogaId;
    private String nombreDroga;
    private String nombreFantasiaProveedor;

    public WishlistItem(Integer stockId, Boolean tieneDisponibilidad, Integer drogaId, String nombreDroga, String nombreFantasiaProveedor) {
        this.stockId = stockId;
        this.tieneDisponibilidad = tieneDisponibilidad;
        this.drogaId = drogaId;
        this.nombreDroga = nombreDroga;
        this.nombreFantasiaProveedor = nombreFantasiaProveedor;
    }

    public String getInicialesProveedor() {
        if (nombreFantasiaProveedor == null || nombreFantasiaProveedor.trim().isEmpty()) return "";
        String[] words = nombreFantasiaProveedor.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                if (i > 0) initials.append(".");
                initials.append(Character.toUpperCase(words[i].charAt(0)));
            }
        }
        return initials.toString();
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public Boolean getTieneDisponibilidad() {
        return tieneDisponibilidad;
    }

    public void setTieneDisponibilidad(Boolean tieneDisponibilidad) {
        this.tieneDisponibilidad = tieneDisponibilidad;
    }

    public Integer getDrogaId() {
        return drogaId;
    }

    public void setDrogaId(Integer drogaId) {
        this.drogaId = drogaId;
    }

    public String getNombreDroga() {
        return nombreDroga;
    }

    public void setNombreDroga(String nombreDroga) {
        this.nombreDroga = nombreDroga;
    }

    public String getNombreFantasiaProveedor() {
        return nombreFantasiaProveedor;
    }

    public void setNombreFantasiaProveedor(String nombreFantasiaProveedor) {
        this.nombreFantasiaProveedor = nombreFantasiaProveedor;
    }
}
