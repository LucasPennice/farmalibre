package Utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtil {
    private static String USER;
    private static String PASS;
    private static String URL;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Intentar primero con JNDI (producción/Tomcat)
            try {
                Context ctx = new InitialContext();
                Context envCtx = (Context) ctx.lookup("java:comp/env");
                USER = (String) envCtx.lookup("DB_USER");
                PASS = (String) envCtx.lookup("DB_PASS");
                URL = (String) envCtx.lookup("DB_URL");
            } catch (Exception e) {
                // Fallback a variables de entorno (desarrollo)
                USER = System.getenv("DB_USER");
                PASS = System.getenv("DB_PASS");
                URL = System.getenv("DB_URL");

                // Si no hay variables de entorno, cargar desde test.properties (tests)
                if (URL == null || USER == null || PASS == null) {
                    loadFromTestProperties();
                }
            }

            if (URL == null || USER == null || PASS == null) {
                throw new RuntimeException("Variables de BD no configuradas. Verifica .env.dev o test.properties");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se pudo cargar el driver de MySQL", e);
        }
    }

    private static void loadFromTestProperties() {
        try {
            InputStream input = DbUtil.class.getClassLoader().getResourceAsStream("test.properties");
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                URL = prop.getProperty("DB_URL");
                USER = prop.getProperty("DB_USER");
                PASS = prop.getProperty("DB_PASS");
            }
        } catch (Exception e) {
            // Ignorar si no existe el archivo
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}