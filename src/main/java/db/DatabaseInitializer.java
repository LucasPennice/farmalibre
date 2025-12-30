package db;

import Utils.DbUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;

public class DatabaseInitializer {

    public static void init() {
        try (Connection con = DbUtil.getConnection()) {
            run(con, "./src/main/resources/schema.sql");
        } catch (Exception e) {
            throw new RuntimeException("Error inicializando la base", e);
        }
    }

    public static void run(Connection con, String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(path));
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
