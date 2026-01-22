package ActionController.checkout;

/**
 * DTO que representa un item en la preferencia de pago de Mercado Pago
 */
public class CheckoutItemDTO {
    private String id;
    private String title;
    private Integer quantity;
    private Double unitPrice;
    
    // Constructores
    public CheckoutItemDTO() {}
    
    public CheckoutItemDTO(String id, String title, Integer quantity, Double unitPrice) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
