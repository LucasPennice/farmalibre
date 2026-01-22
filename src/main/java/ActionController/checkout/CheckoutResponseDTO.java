package ActionController.checkout;

/**
 * DTO para respuestas de preferencia de pago creada en Mercado Pago
 */
public class CheckoutResponseDTO {
    private String preferenceId;
    private String initPoint;
    private String sandboxInitPoint;
    
    // Constructores
    public CheckoutResponseDTO() {}
    
    public CheckoutResponseDTO(String preferenceId, String initPoint, String sandboxInitPoint) {
        this.preferenceId = preferenceId;
        this.initPoint = initPoint;
        this.sandboxInitPoint = sandboxInitPoint;
    }
    
    // Getters y Setters
    public String getPreferenceId() {
        return preferenceId;
    }
    
    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }
    
    public String getInitPoint() {
        return initPoint;
    }
    
    public void setInitPoint(String initPoint) {
        this.initPoint = initPoint;
    }
    
    public String getSandboxInitPoint() {
        return sandboxInitPoint;
    }
    
    public void setSandboxInitPoint(String sandboxInitPoint) {
        this.sandboxInitPoint = sandboxInitPoint;
    }
}
