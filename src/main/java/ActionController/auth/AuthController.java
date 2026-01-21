package ActionController.auth;

import java.util.logging.Logger;
import Usuario.Usuario;
import Usuario.UsuarioService;

public class AuthController {
    private static final Logger log = Logger.getLogger(AuthController.class.getName());
    
    public static LoginResponseDTO doLogin(LoginRequestDTO request) {
        try {
            log.info("Iniciando proceso de login para usuario: " + (request != null ? request.getNombre() : "null"));
            
            // Validaciones de entrada
            if (request == null) {
                throw new IllegalArgumentException("La solicitud de login no puede ser nula");
            }
            if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de usuario no puede ser vacío");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("La contraseña no puede ser vacía");
            }
            
            // Lógica de negocio usando el service existente
            Usuario usuario = UsuarioService.autenticar(request.getNombre(), request.getPassword());
            
            log.info("Login exitoso para usuario: " + usuario.getNombreUsuario());
            return new LoginResponseDTO(usuario);
            
        } catch (RuntimeException e) {
            log.warning("Error en login: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.severe("Error inesperado en login: " + e.getMessage());
            throw new RuntimeException("Error al procesar el login: " + e.getMessage());
        }
    }
    
    public static RegisterResponseDTO doRegister(RegisterRequestDTO request) {
        try {
            log.info("Iniciando proceso de registro para usuario: " + (request != null ? request.getNombre() : "null"));
            
            // Validaciones de entrada
            if (request == null) {
                throw new IllegalArgumentException("La solicitud de registro no puede ser nula");
            }
            if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre completo no puede ser vacío");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("El email no puede ser vacío");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("La contraseña no puede ser vacía");
            }
            
            // Lógica de negocio usando el service existente
            Usuario nuevoUsuario = UsuarioService.registrar(
                request.getNombre(),
                request.getEmail(), 
                request.getPassword()
            );
            
            log.info("Registro exitoso para usuario: " + nuevoUsuario.getNombreUsuario());
            
            // Retornar respuesta con el usuario creado
            return new RegisterResponseDTO(nuevoUsuario);
            
        } catch (RuntimeException e) {
            log.warning("Error en registro: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.severe("Error inesperado en registro: " + e.getMessage());
            throw new RuntimeException("Error al procesar el registro: " + e.getMessage());
        }
    }
    
    public static void doLogout() {
        try {
            log.info("Iniciando proceso de logout");
            
            // Por ahora, la lógica de logout (invalidar sesión) 
            // permanece en FrontController ya que maneja el contexto HTTP
            // Este método existe por consistencia y para futura expansión
            
            log.info("Logout procesado");
            
        } catch (Exception e) {
            log.severe("Error en logout: " + e.getMessage());
            throw new RuntimeException("Error al procesar el logout: " + e.getMessage());
        }
    }
}
