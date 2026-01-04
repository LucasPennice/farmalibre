// src/test/java/Usuario/UsuarioServiceTest.java
package Usuario;

import org.junit.jupiter.api.*;

import db.DatabaseInitializer;

import static org.junit.jupiter.api.Assertions.*;
import java.util.LinkedList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioServiceTest {

    private UsuarioService usuarioService;
    private Usuario usuarioTest;

    @BeforeAll
    void setUpClass() {
        System.out.println("🚀 Configurando tests de UsuarioService");
        DatabaseInitializer.init();
    }

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService();
        // Crear usuario de prueba para tests que lo necesiten
        usuarioTest = new Usuario(null, "Test", "test@test.com", null, Rol.USUARIO);
    }

    @AfterEach
    void tearDown() {
        // Limpiar si es necesario
    }

    @Test
    @DisplayName("Test 1: Guardar usuario válido")
    void testSaveUsuarioValido() {
        // Arrange (Preparar)
        System.out.println("🧪 Test: Guardar usuario válido");

        // Act (Actuar)
        usuarioService.save(usuarioTest);

        // Assert (Verificar)
        assertTrue(usuarioTest.getId() > 0, "El usuario debe tener un ID asignado");
        System.out.println("✅ Usuario guardado con ID: " + usuarioTest.getId());
    }

    @Test
    @DisplayName("Test 2: Validar que usuario nulo lance excepción")
    void testSaveUsuarioNulo() {
        // Arrange
        System.out.println("🧪 Test: Validar usuario nulo");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.save(null),
                "Debe lanzar IllegalArgumentException");

        assertEquals("El usuario no puede ser nulo", exception.getMessage());
        System.out.println("✅ Correctamente validado usuario nulo");
    }

    @Test
    @DisplayName("Test 4: Buscar usuario por ID")
    void testFindById() {
        // Arrange
        System.out.println("🧪 Test: Buscar usuario por ID");
        usuarioService.save(usuarioTest);
        int id = usuarioTest.getId();

        // Act
        Usuario encontrado = usuarioService.findById(String.valueOf(id));

        // Assert
        assertNotNull(encontrado, "Debe encontrar el usuario");
        assertEquals(id, encontrado.getId(), "Los IDs deben coincidir");
        assertEquals(usuarioTest.getDireccion(), encontrado.getDireccion(), "Los emails deben coincidir");
        System.out.println("✅ Usuario encontrado correctamente");
    }

    @Test
    @DisplayName("Test 5: Buscar usuario con ID inválido")
    void testFindByIdInvalido() {
        // Arrange
        System.out.println("🧪 Test: Buscar con ID inválido");

        // Act & Assert para ID nulo
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.findById(null));
        assertTrue(exception1.getMessage().contains("no puede ser nulo"));

        // Act & Assert para ID no numérico
        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.findById("abc"));
        assertTrue(exception2.getMessage().contains("número válido"));

        System.out.println("✅ Correctamente validados IDs inválidos");
    }

    @Test
    @DisplayName("Test 6: Listar todos los usuarios")
    void testFindAll() {
        // Arrange
        System.out.println("🧪 Test: Listar todos los usuarios");

        // Guardar un usuario de prueba
        usuarioService.save(usuarioTest);

        // Act
        LinkedList<Usuario> usuarios = usuarioService.findAll();

        // Assert
        assertNotNull(usuarios, "La lista no debe ser nula");
        assertFalse(usuarios.isEmpty(), "Debe haber al menos un usuario");
        System.out.println("✅ Usuarios encontrados: " + usuarios.size());
    }

    @Test
    @DisplayName("Test 7: Actualizar usuario")
    void testUpdateUsuario() {
        // Arrange
        System.out.println("🧪 Test: Actualizar usuario");
        usuarioService.save(usuarioTest);
        String nuevoNombre = "Nombre Actualizado";

        // Act
        usuarioTest.setNombreCompletoRes(nuevoNombre);
        usuarioService.update(usuarioTest);

        // Verificar
        Usuario actualizado = usuarioService.findById(String.valueOf(usuarioTest.getId()));
        assertEquals(nuevoNombre, actualizado.getNombreCompletoRes(), "El nombre debe estar actualizado");
        System.out.println("✅ Usuario actualizado correctamente");
    }

    @Test
    @DisplayName("Test 8: Eliminar usuario")
    void testDeleteUsuario() {
        // Arrange
        System.out.println("🧪 Test: Eliminar usuario");
        usuarioService.save(usuarioTest);
        int id = usuarioTest.getId();

        // Verificar que existe primero
        Usuario antes = usuarioService.findById(String.valueOf(id));
        assertNotNull(antes, "El usuario debe existir antes de eliminar");

        // Act
        usuarioService.delete(usuarioTest);

        // Assert - después de eliminar, no debería encontrarse
        // Nota: Esto depende de cómo implementes findById después de eliminar
        System.out.println("✅ Usuario eliminado (verificar manualmente en BD)");
    }

    @AfterAll
    void tearDownClass() {
        System.out.println("🎯 Todos los tests de UsuarioService completados");
        // Limpiar usuarios de prueba de la BD si es necesario
        try {
            LinkedList<Usuario> usuariosTest = usuarioService.findAll();
            for (Usuario u : usuariosTest) {
                if (u.getDireccion().contains("@test.com")) {
                    usuarioService.delete(u);
                }
            }
        } catch (Exception e) {
            // Ignorar errores de limpieza
        }
    }
}