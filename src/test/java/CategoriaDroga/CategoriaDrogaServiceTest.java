package CategoriaDroga;

import db.DatabaseInitializer;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoriaDrogaServiceTest {

    private CategoriaDrogaService service;
    private CategoriaDroga categoriaTest;

    @BeforeAll
    void initDb() {
        DatabaseInitializer.init();
    }

    @BeforeEach
    void setUp() {
        service = new CategoriaDrogaService();
        categoriaTest = new CategoriaDroga(null, "Analgesicos Test", false);
    }

    @AfterEach
    void cleanUp() {
        try {
            LinkedList<CategoriaDroga> all = service.findAll();
            for (CategoriaDroga c : all) {
                if (c.getNombre() != null && c.getNombre().contains("Test")) {
                    service.delete(c);
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("Guardar categoria válida")
    void saveCategoria() {
        service.save(categoriaTest);
        assertNotNull(categoriaTest.getId());
        assertTrue(categoriaTest.getId() > 0);
    }

    @Test
    @DisplayName("Buscar categoria por ID")
    void findById() {
        service.save(categoriaTest);
        CategoriaDroga found = service.findById(String.valueOf(categoriaTest.getId()));
        assertNotNull(found);
        assertEquals(categoriaTest.getNombre(), found.getNombre());
    }

    @Test
    @DisplayName("Actualizar categoria")
    void updateCategoria() {
        service.save(categoriaTest);
        categoriaTest.setAprobacion_pendiente(true);
        service.update(categoriaTest);
        CategoriaDroga updated = service.findById(String.valueOf(categoriaTest.getId()));
        assertTrue(updated.getAprobacion_pendiente());
    }

    @Test
    @DisplayName("Listar categorias")
    void listAll() {
        service.save(categoriaTest);
        LinkedList<CategoriaDroga> categorias = service.findAll();
        assertNotNull(categorias);
        assertFalse(categorias.isEmpty());
    }

    @Test
    @DisplayName("Eliminar categoria")
    void deleteCategoria() {
        service.save(categoriaTest);
        Integer id = categoriaTest.getId();
        service.delete(categoriaTest);
        CategoriaDroga found = service.findById(String.valueOf(id));
        assertNull(found);
    }
}
