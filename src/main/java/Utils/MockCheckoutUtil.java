package Utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Utilidad para mockear el checkout de Mercado Pago
 * Permite simular pagos sin necesidad de credenciales reales
 */
public class MockCheckoutUtil {
    private static final Logger log = Logger.getLogger(MockCheckoutUtil.class.getName());
    
    // Parámetro para activar modo mock en desarrollo
    private static final String MOCK_PARAM = "mock_payment";
    private static final String MOCK_SESSION_ATTR = "mockMode";
    
    /**
     * Verifica si el request está en modo mock
     * @param request HTTP request
     * @return true si está en modo mock
     */
    public static boolean isMockMode(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        
        // Verificar parámetro en request (para pruebas)
        String mockParam = request.getParameter(MOCK_PARAM);
        if ("true".equalsIgnoreCase(mockParam) || "1".equals(mockParam)) {
            return true;
        }
        
        // Verificar atributo de sesión
        Boolean mockMode = (Boolean) request.getSession(false).getAttribute(MOCK_SESSION_ATTR);
        return Boolean.TRUE.equals(mockMode);
    }
    
    /**
     * Activa el modo mock en la sesión
     * @param request HTTP request
     */
    public static void enableMockMode(HttpServletRequest request) {
        if (request != null && request.getSession(false) != null) {
            request.getSession(true).setAttribute(MOCK_SESSION_ATTR, Boolean.TRUE);
            log.info("Mock checkout activado");
        }
    }
    
    /**
     * Desactiva el modo mock en la sesión
     * @param request HTTP request
     */
    public static void disableMockMode(HttpServletRequest request) {
        if (request != null && request.getSession(false) != null) {
            request.getSession().removeAttribute(MOCK_SESSION_ATTR);
            log.info("Mock checkout desactivado");
        }
    }
    
    /**
     * Genera un ID de preferencia falso para testing
     * Formato: MOCK_PREF_ + UUID
     * @return preference ID falso
     */
    public static String generateMockPreferenceId() {
        String mockId = "MOCK_PREF_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        log.info("Generated mock preference ID: " + mockId);
        return mockId;
    }
    
    /**
     * Genera un ID de pago falso para testing
     * Formato: MOCK_PAY_ + UUID
     * @return payment ID falso
     */
    public static String generateMockPaymentId() {
        String mockId = "MOCK_PAY_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        log.info("Generated mock payment ID: " + mockId);
        return mockId;
    }
}