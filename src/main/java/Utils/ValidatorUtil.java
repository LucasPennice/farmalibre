package Utils;

public class ValidatorUtil {
    public static Boolean validateCuit(String cuit) {
        // Validar formato básico
        if (cuit == null || !cuit.matches("\\d{2}-\\d{8}-\\d")) {
            return false;
        }

        // Eliminar guiones para el cálculo
        String cuitNumeros = cuit.replace("-", "");
        int[] multiplicadores = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
        int suma = 0;

        for (int i = 0; i < multiplicadores.length; i++) {
            suma += Character.getNumericValue(cuitNumeros.charAt(i)) * multiplicadores[i];
        }

        int resto = suma % 11;
        int digitoVerificadorCalculado = resto == 0 ? 0 : resto == 1 ? 9 : 11 - resto;
        int digitoVerificadorIngresado = Character.getNumericValue(cuitNumeros.charAt(10));

        System.out.println(
                "DV Calculado: " + digitoVerificadorCalculado + ", DV Ingresado: " + digitoVerificadorIngresado);

        return digitoVerificadorCalculado == digitoVerificadorIngresado;
    }

    public static Boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public static Boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        String phoneRegex = "^\\+?\\d{1,3}?[- .]?\\(?(\\d{1,4})\\)?[- .]?\\d{1,4}[- .]?\\d{1,9}$";
        return phoneNumber.matches(phoneRegex);
    }

    public static Boolean validatePostalCode(String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            return false;
        }
        String postalCodeRegex = "^\\d{4}$";
        return postalCode.matches(postalCodeRegex);
    }

    public static Boolean validateNumberGreaterThanZero(Double number) {
        if (number == null) {
            return false;
        }
        return number > 0;
    }

    public static Boolean validateNumberGreaterThanZero(Integer number) {
        if (number == null) {
            return false;
        }
        return number > 0;
    }

    public static Boolean validateNumberGreaterThanZero(String numberStr) {
        if (numberStr == null || numberStr.trim().isEmpty()) {
            return false;
        }
        try {
            Double number = Double.parseDouble(numberStr);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean requireValidId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El id no puede ser nulo o vacío");
        }
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El id debe ser un número válido");
        }
        return true;
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
