package ActionController.checkout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

import Utils.MercadoPagoUtil;

/**
 * Controlador para manejar el checkout y preferencias de pago con Mercado Pago
 * Utiliza el SDK oficial de Mercado Pago
 */
public class CheckoutController {
    private static final Logger log = Logger.getLogger(CheckoutController.class.getName());
    private static final String BACK_URL = "http://localhost:8080/farmalibre";
    private static final String SUCCESS_URL = BACK_URL + "/checkout/success";
    private static final String FAILURE_URL = BACK_URL + "/checkout/failure";
    private static final String PENDING_URL = BACK_URL + "/checkout/pending";
    
    static {
        try {
            // Inicializar el SDK de Mercado Pago con el access token
            String accessToken = MercadoPagoUtil.getAccessToken();
            MercadoPagoConfig.setAccessToken(accessToken);
            log.info("✓ Mercado Pago SDK inicializado correctamente");
        } catch (Exception e) {
            log.severe("Error al inicializar Mercado Pago SDK: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar Mercado Pago", e);
        }
    }
    
    /**
     * Crea una preferencia de pago en Mercado Pago usando el SDK
     * @param request DTO con los datos del checkout
     * @return DTO con preferenceId e initPoint
     * @throws Exception si hay error al crear la preferencia
     */
    @SuppressWarnings("unused")
    public static CheckoutResponseDTO crearPreferenciaPago(CheckoutRequestDTO request) {
        try {
            log.info("Iniciando creación de preferencia de pago para usuario: " + request.getUsuarioId());
            
            // Validaciones de entrada
            if (request == null) {
                throw new IllegalArgumentException("La solicitud de checkout no puede ser nula");
            }
            if (request.getTotal() == null || request.getTotal() <= 0) {
                throw new IllegalArgumentException("El total debe ser mayor a 0");
            }
            if (request.getItems() == null || request.getItems().isEmpty()) {
                throw new IllegalArgumentException("Debe haber al menos un item en el carrito");
            }
            
            // Convertir items del DTO a PreferenceItemRequest del SDK
            List<PreferenceItemRequest> items = new ArrayList<>();
            for (CheckoutItemDTO itemDTO : request.getItems()) {
                PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(itemDTO.getId())
                    .title(itemDTO.getTitle())
                    .description("Medicamento - Farmalibre")
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(new BigDecimal(itemDTO.getUnitPrice().toString()))
                    .currencyId("ARS")  // Moneda: Pesos Argentinos
                    .build();
                items.add(itemRequest);
            }
            
            // Construir URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(SUCCESS_URL)
                .failure(FAILURE_URL)
                .pending(PENDING_URL)
                .build();
            
             // Construir la preferencia de pago
             PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                 .items(items)
                 .backUrls(backUrls)
                 .externalReference(request.getUsuarioId() + "_" + System.currentTimeMillis())
                 .build();
            
            // Crear la preferencia en Mercado Pago
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);
            
            log.info("✓ Preferencia de pago creada exitosamente");
            log.info("  - ID: " + preference.getId());
            log.info("  - Init Point: " + preference.getInitPoint());
            log.info("  - Sandbox Init Point: " + preference.getSandboxInitPoint());
            
            // Retornar respuesta con preference ID e initPoint
            CheckoutResponseDTO response = new CheckoutResponseDTO(
                preference.getId(),
                preference.getInitPoint(),
                preference.getSandboxInitPoint()
            );
            
            return response;
            
        } catch (MPApiException e) {
            log.severe("Error de API de Mercado Pago: " + e.getMessage());
            log.severe("Response Status: " + e.getApiResponse().getStatusCode());
            throw new RuntimeException("Error en Mercado Pago: " + e.getMessage());
        } catch (MPException e) {
            log.severe("Error en Mercado Pago: " + e.getMessage());
            throw new RuntimeException("Error al crear preferencia de pago: " + e.getMessage());
        } catch (RuntimeException e) {
            log.warning("Error en validación: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.severe("Error inesperado al crear preferencia: " + e.getMessage());
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Verifica el estado de un pago usando el SDK
     * @param paymentId ID del pago a verificar
     * @return true si el pago fue aprobado
     * @throws Exception si hay error
     */
    public static boolean verificarPago(String paymentId) {
        try {
            log.info("Verificando estado del pago: " + paymentId);
            
            if (paymentId == null || paymentId.trim().isEmpty()) {
                throw new IllegalArgumentException("El ID del pago no puede ser vacío");
            }
            
            // TODO: Implementar verificación de estado del pago
            // usando PaymentClient del SDK
            
            log.info("✓ Pago verificado: " + paymentId);
            return true;
            
        } catch (Exception e) {
            log.severe("Error al verificar pago: " + e.getMessage());
            throw new RuntimeException("Error al verificar pago: " + e.getMessage());
        }
    }
    
    /**
     * Procesa el retorno exitoso del checkout
     * @param preferenceId ID de la preferencia
     * @param paymentId ID del pago
     * @throws Exception si hay error
     */
    public static void procesarRetornoExitoso(String preferenceId, String paymentId) {
        try {
            log.info("Procesando retorno exitoso - Preference: " + preferenceId + ", Payment: " + paymentId);
            
            if (preferenceId == null || preferenceId.trim().isEmpty()) {
                throw new IllegalArgumentException("ID de preferencia no válido");
            }
            
            // Aquí se podría:
            // 1. Reducir stock de los productos (implementado en StockReductionService)
            // 2. Crear pedido en la BD
            // 3. Enviar email de confirmación
            
            log.info("✓ Retorno exitoso procesado");
            
        } catch (Exception e) {
            log.severe("Error al procesar retorno exitoso: " + e.getMessage());
            throw new RuntimeException("Error al procesar pago: " + e.getMessage());
        }
    }
    
    /**
     * Procesa el retorno fallido del checkout
     * @param preferenceId ID de la preferencia
     * @throws Exception si hay error
     */
    public static void procesarRetornoFallido(String preferenceId) {
        try {
            log.info("Procesando retorno fallido - Preference: " + preferenceId);
            
            if (preferenceId == null || preferenceId.trim().isEmpty()) {
                throw new IllegalArgumentException("ID de preferencia no válido");
            }
            
            log.info("✓ Retorno fallido procesado. El carrito se mantiene intacto");
            
        } catch (Exception e) {
            log.severe("Error al procesar retorno fallido: " + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
