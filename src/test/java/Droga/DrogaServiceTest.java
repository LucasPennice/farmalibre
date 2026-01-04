package Droga;

import CategoriaDroga.CategoriaDroga;
import CategoriaDroga.CategoriaDrogaService;
import db.DatabaseInitializer;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DrogaServiceTest {

    private DrogaService drogaService;
    private CategoriaDrogaService categoriaService;
    private CategoriaDroga categoriaBase;
    private Droga drogaTest;

    @BeforeAll
    void initDb() {
        DatabaseInitializer.init();
        categoriaService = new CategoriaDrogaService();
        categoriaBase = new CategoriaDroga(null, "Categoria Base Test", false);
        categoriaService.save(categoriaBase);
    }

    @BeforeEach
    void setUp() {
        drogaService = new DrogaService();
        drogaTest = new Droga(null, "Ibuprofeno Test", "Ibuprofeno 400mg", "comprimido", categoriaBase);
    }

    @AfterEach
    void cleanUp() {
        try {
            LinkedList<Droga> all = drogaService.findAll();
            for (Droga d : all) {
                if (d.getNombre() != null && d.getNombre().contains("Test")) {
                    drogaService.delete(d);
                }
            }
        } catch (Exception ignored) {
        }
    }

    @AfterAll
    void tearDownAll() {
        try {
            categoriaService.delete(categoriaBase);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("Guardar droga válida")
    void saveDroga() {
        drogaService.save(drogaTest);
        assertNotNull(drogaTest.getId());
        assertTrue(drogaTest.getId() > 0);
    }

    @Test
    @DisplayName("Buscar droga por ID")
    void findById() {
        drogaService.save(drogaTest);
        Droga found = drogaService.findById(String.valueOf(drogaTest.getId()));
        assertNotNull(found);
        assertEquals(drogaTest.getNombre(), found.getNombre());
    }

    @Test
    @DisplayName("Actualizar droga")
    void updateDroga() {
        drogaService.save(drogaTest);
        drogaTest.setUnidad("capsula");
        drogaService.update(drogaTest);
        Droga updated = drogaService.findById(String.valueOf(drogaTest.getId()));
        assertEquals("capsula", updated.getUnidad());
    }

    @Test
    @DisplayName("Listar drogas")
    void listAll() {
        drogaService.save(drogaTest);
        LinkedList<Droga> drogas = drogaService.findAll();
        assertNotNull(drogas);
        assertFalse(drogas.isEmpty());
    }

    @Test
    @DisplayName("Eliminar droga")
    void deleteDroga() {
        drogaService.save(drogaTest);
        Integer id = drogaTest.getId();
        drogaService.delete(drogaTest);
        Droga found = drogaService.findById(String.valueOf(id));
        assertNull(found);
    }
}
