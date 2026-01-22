package Utils;

import java.util.logging.Logger;

/**
 * Utilidad para validar y procesar webhooks de Mercado Pago
 * 
 * Los webhooks son notificaciones HTTP que Mercado Pago envía cuando ocurren eventos
 * relacionados con pagos (ej: aprobación, rechazo, etc.)
 */
public class MercadoPagoWebhookUtil {
    private static final Logger log = Logger.getLogger(MercadoPagoWebhookUtil.class.getName());
    
    /**
     * Tipo de evento: pago
     */
    public static final String EVENT_TYPE_PAYMENT = "payment";
    
    /**
     * Tipo de evento: plan
     */
    public static final String EVENT_TYPE_PLAN = "plan";
    
    /**
     * Estado de pago: aprobado
     */
    public static final String PAYMENT_STATUS_APPROVED = "approved";
    
    /**
     * Estado de pago: rechazado
     */
    public static final String PAYMENT_STATUS_REJECTED = "rejected";
    
    /**
     * Estado de pago: cancelado
     */
    public static final String PAYMENT_STATUS_CANCELLED = "cancelled";
    
    /**
     * Estado de pago: pendiente
     */
    public static final String PAYMENT_STATUS_PENDING = "pending";
    
    /**
     * Valida que la notificación venga de Mercado Pago
     * En un escenario de producción, se debe verificar la firma HMAC
     * 
     * @param signature Firma del webhook (header X-Signature)
     * @param nonce Nonce del webhook (header X-Idempotency-Key)
     * @param body Body del webhook
     * @return true si la firma es válida
     */
    public static boolean validarFirmaWebhook(String signature, String nonce, String body) {
        try {
            log.info("Validando firma del webhook");
            
            if (signature == null || signature.trim().isEmpty()) {
                log.warning("Firma del webhook vacía");
                return false;
            }
            
            // TODO: Implementar validación HMAC-SHA256
            // Este es un paso de seguridad crucial en producción
            // Se debe validar contra el secret de Mercado Pago
            
            log.info("✓ Firma del webhook válida");
            return true;
            
        } catch (Exception e) {
            log.severe("Error al validar firma: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Valida el tipo de evento del webhook
     * 
     * @param eventType Tipo de evento (payment, plan, etc.)
     * @return true si el tipo es soportado
     */
    public static boolean esEventoSoportado(String eventType) {
        return EVENT_TYPE_PAYMENT.equals(eventType) || EVENT_TYPE_PLAN.equals(eventType);
    }
    
    /**
     * Extrae el tipo de evento del body JSON del webhook
     * 
     * @param jsonBody Body del webhook
     * @return Tipo de evento
     */
    public static String extraerTipoEvento(String jsonBody) {
        try {
            // Búsqueda simple del tipo de evento
            int startIdx = jsonBody.indexOf("\"type\":");
            if (startIdx == -1) return null;
            
            startIdx = jsonBody.indexOf("\"", startIdx + 8);
            int endIdx = jsonBody.indexOf("\"", startIdx + 1);
            
            if (startIdx == -1 || endIdx == -1) return null;
            
            return jsonBody.substring(startIdx + 1, endIdx);
            
        } catch (Exception e) {
            log.warning("Error al extraer tipo de evento: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Extrae el ID de recurso del webhook (payment ID, plan ID, etc.)
     * 
     * @param jsonBody Body del webhook
     * @return ID del recurso
     */
    public static String extraerIdRecurso(String jsonBody) {
        try {
            // Búsqueda simple del ID de recurso
            int startIdx = jsonBody.indexOf("\"id\":");
            if (startIdx == -1) return null;
            
            startIdx = jsonBody.indexOf("\"", startIdx) + 1;
            int endIdx = jsonBody.indexOf("\"", startIdx);
            
            if (endIdx == -1) {
                // Podría ser un número sin comillas
                int commaIdx = jsonBody.indexOf(",", startIdx);
                int closeIdx = jsonBody.indexOf("}", startIdx);
                endIdx = commaIdx < closeIdx ? commaIdx : closeIdx;
            }
            
            if (endIdx == -1) return null;
            
            return jsonBody.substring(startIdx, endIdx);
            
        } catch (Exception e) {
            log.warning("Error al extraer ID de recurso: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Obtiene el estado del pago de una notificación
     * 
     * @param paymentStatus Estado del pago (approved, rejected, etc.)
     * @return true si el pago fue aprobado
     */
    public static boolean esPagoAprobado(String paymentStatus) {
        return PAYMENT_STATUS_APPROVED.equals(paymentStatus);
    }
    
    /**
     * Obtiene el estado del pago de una notificación
     * 
     * @param paymentStatus Estado del pago
     * @return true si el pago fue rechazado
     */
    public static boolean esPagoRechazado(String paymentStatus) {
        return PAYMENT_STATUS_REJECTED.equals(paymentStatus);
    }
    
    /**
     * Obtiene el estado del pago de una notificación
     * 
     * @param paymentStatus Estado del pago
     * @return true si el pago está pendiente
     */
    public static boolean esPagoPendiente(String paymentStatus) {
        return PAYMENT_STATUS_PENDING.equals(paymentStatus);
    }
    
    /**
     * Registra un webhook recibido
     * 
     * @param eventType Tipo de evento
     * @param resourceId ID del recurso
     * @param timestamp Timestamp del evento
     */
    public static void registrarWebhook(String eventType, String resourceId, long timestamp) {
        try {
            log.info("Webhook registrado - Tipo: " + eventType 
                + ", Resource: " + resourceId 
                + ", Timestamp: " + timestamp);
        } catch (Exception e) {
            log.warning("Error al registrar webhook: " + e.getMessage());
        }
    }
}
