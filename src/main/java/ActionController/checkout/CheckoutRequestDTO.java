package ActionController.checkout;

import java.util.LinkedList;

/**
 * DTO para solicitudes de creación de preferencia de pago en Mercado Pago
 */
public class CheckoutRequestDTO {
    private String usuarioId;
    private Double total;
    private String description;
    private LinkedList<CheckoutItemDTO> items;
    
    // Constructores
    public CheckoutRequestDTO() {}
    
    public CheckoutRequestDTO(String usuarioId, Double total, String description, LinkedList<CheckoutItemDTO> items) {
        this.usuarioId = usuarioId;
        this.total = total;
        this.description = description;
        this.items = items;
    }
    
    // Getters y Setters
    public String getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LinkedList<CheckoutItemDTO> getItems() {
        return items;
    }
    
    public void setItems(LinkedList<CheckoutItemDTO> items) {
        this.items = items;
    }
}
