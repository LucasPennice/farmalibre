package Utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static String USER;
    private static String PASS;
    private static String URL;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Intentar primero con JNDI (producción)
            try {
                Context ctx = new InitialContext();
                Context envCtx = (Context) ctx.lookup("java:comp/env");
                USER = (String) envCtx.lookup("DB_USER");
                PASS = (String) envCtx.lookup("DB_PASS");
                URL = (String) envCtx.lookup("DB_URL");
            } catch (Exception e) {
                // Fallback a variables de entorno
                USER = System.getenv("DB_USER");
                PASS = System.getenv("DB_PASS");
                URL = System.getenv("DB_URL");
            }

            if (URL == null || USER == null || PASS == null) {
                throw new RuntimeException("Variables de BD no configuradas. Verifica .env.dev");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se pudo cargar el driver de MySQL", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}