package db;

import Utils.DbUtil;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

public class DatabaseInitializer {

    private static final boolean DISABLED = "true".equalsIgnoreCase(
            System.getenv("DB_INIT_DISABLED"));

    public static void init() {
        if (DISABLED) return;
        try (Connection con = DbUtil.getConnection()) {
            run(con, "schema.sql");
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando la base", e);
        }
    }

    public static void run(Connection con, String resource) throws Exception {
        InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream(resource);
        if (is == null) {
            throw new RuntimeException("No se encontro " + resource + " en el classpath");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sql = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            if (!line.trim().startsWith("--")) {
                sql.append(line);
                if (line.trim().endsWith(";")) {
                    con.createStatement().execute(sql.toString());
                    sql.setLength(0);
                }
            }
        }

        reader.close();
    }
}
