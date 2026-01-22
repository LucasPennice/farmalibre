package Utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Utilidad para cargar credenciales de Mercado Pago
 * Intenta cargar desde: JNDI → variables de entorno → test.properties
 */
public class MercadoPagoUtil {
    private static String ACCESS_TOKEN;
    private static final Logger log = Logger.getLogger(MercadoPagoUtil.class.getName());
    
    static {
        try {
            // Intentar primero con JNDI (producción/Tomcat)
            try {
                Context ctx = new InitialContext();
                Context envCtx = (Context) ctx.lookup("java:comp/env");
                ACCESS_TOKEN = (String) envCtx.lookup("ACCESS_TOKEN_MP");
                log.info("ACCESS_TOKEN de Mercado Pago cargado desde JNDI");
            } catch (Exception e) {
                // Fallback a variables de entorno (desarrollo)
                ACCESS_TOKEN = System.getenv("ACCESS_TOKEN_MP");
                
                // Si no hay variables de entorno, cargar desde test.properties (tests)
                if (ACCESS_TOKEN == null) {
                    loadFromTestProperties();
                }
                log.info("ACCESS_TOKEN de Mercado Pago cargado desde variables de entorno");
            }
            
            if (ACCESS_TOKEN == null || ACCESS_TOKEN.trim().isEmpty()) {
                throw new RuntimeException("ACCESS_TOKEN_MP no configurado. Verifica .env.dev o variables de entorno");
            }
            
            log.info("✓ Credenciales de Mercado Pago cargadas exitosamente");
            
        } catch (Exception e) {
            log.severe("Error al cargar credenciales de Mercado Pago: " + e.getMessage());
            throw new RuntimeException("Error al cargar ACCESS_TOKEN de Mercado Pago", e);
        }
    }
    
    private static void loadFromTestProperties() {
        try {
            InputStream input = MercadoPagoUtil.class.getClassLoader().getResourceAsStream("test.properties");
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                ACCESS_TOKEN = prop.getProperty("ACCESS_TOKEN_MP");
                log.info("ACCESS_TOKEN de Mercado Pago cargado desde test.properties");
            }
        } catch (Exception e) {
            log.warning("No se pudo cargar test.properties: " + e.getMessage());
        }
    }
    
    public static String getAccessToken() {
        return ACCESS_TOKEN;
    }
}
