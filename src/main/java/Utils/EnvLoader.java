package Utils;

import java.io.*;
import java.util.*;

public class EnvLoader {

    public static void load(String path) {
        try (InputStream input = new FileInputStream(path)) {

            Properties props = new Properties();
            props.load(input);

            for (String key : props.stringPropertyNames()) {
                System.setProperty(key, props.getProperty(key));
            }

        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar el archivo de entorno: " + path, e);
        }
    }
}
