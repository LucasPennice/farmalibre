package StockDroga;

import CategoriaDroga.CategoriaDroga;
import CategoriaDroga.CategoriaDrogaService;
import Droga.Droga;
import Droga.DrogaService;
import Proveedor.Proveedor;
import Proveedor.ProveedorService;
import Proveedor.TipoPersona;
import db.DatabaseInitializer;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockDrogaServiceTest {

    private StockDrogaService stockService;
    private DrogaService drogaService;
    private ProveedorService proveedorService;
    private CategoriaDrogaService categoriaService;

    private CategoriaDroga categoriaBase;
    private Droga drogaBase;
    private Proveedor proveedorBase;
    private StockDroga stockTest;

    @BeforeAll
    void initDb() {
        DatabaseInitializer.init();

        categoriaService = new CategoriaDrogaService();
        drogaService = new DrogaService();
        proveedorService = new ProveedorService();

        categoriaBase = new CategoriaDroga(null, "Categoria Stock Test", false);
        categoriaService.save(categoriaBase);

        drogaBase = new Droga(null, "Amoxicilina Test", "Amoxicilina 500mg", "capsula", categoriaBase);
        drogaService.save(drogaBase);

        proveedorBase = new Proveedor(null, "Proveedor Stock Test SA", "ProvStock", "20-34342578-4",
                TipoPersona.JURIDICA);
        proveedorService.save(proveedorBase);
    }

    @BeforeEach
    void setUp() {
        stockService = new StockDrogaService();
        stockTest = new StockDroga(null, drogaBase, proveedorBase, 50, 120.50);
    }

    @AfterEach
    void cleanUp() {
        try {
            LinkedList<StockDroga> all = stockService.findAll();
            for (StockDroga s : all) {
                if (s.getDroga() != null && drogaBase.getId().equals(s.getDroga().getId()) &&
                        s.getProveedor() != null && proveedorBase.getId().equals(s.getProveedor().getId())) {
                    stockService.delete(s);
                }
            }
        } catch (Exception ignored) {
        }
    }

    @AfterAll
    void tearDownAll() {
        try {
            stockService = new StockDrogaService();
            LinkedList<StockDroga> all = stockService.findAll();
            for (StockDroga s : all) {
                if (s.getDroga() != null && s.getDroga().getId().equals(drogaBase.getId())) {
                    stockService.delete(s);
                }
            }
        } catch (Exception ignored) {
        }
        try {
            drogaService.delete(drogaBase);
        } catch (Exception ignored) {
        }
        try {
            proveedorService.delete(proveedorBase);
        } catch (Exception ignored) {
        }
        try {
            categoriaService.delete(categoriaBase);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("Guardar stock válido")
    void saveStock() {
        stockService.save(stockTest);
        assertNotNull(stockTest.getId());
        assertTrue(stockTest.getId() > 0);
    }

    @Test
    @DisplayName("Buscar stock por ID")
    void findById() {
        stockService.save(stockTest);
        StockDroga found = stockService.findById(String.valueOf(stockTest.getId()));
        assertNotNull(found);
        assertEquals(stockTest.getDisponible(), found.getDisponible());
    }

    @Test
    @DisplayName("Actualizar stock")
    void updateStock() {
        stockService.save(stockTest);
        stockTest.setDisponible(75);
        stockService.update(stockTest);
        StockDroga updated = stockService.findById(String.valueOf(stockTest.getId()));
        assertEquals(75, updated.getDisponible());
    }

    @Test
    @DisplayName("Listar stocks")
    void listAll() {
        stockService.save(stockTest);
        LinkedList<StockDroga> stocks = stockService.findAll();
        assertNotNull(stocks);
        assertFalse(stocks.isEmpty());
    }

    @Test
    @DisplayName("Eliminar stock")
    void deleteStock() {
        stockService.save(stockTest);
        Integer id = stockTest.getId();
        stockService.delete(stockTest);
        StockDroga found = stockService.findById(String.valueOf(id));
        assertNull(found);
    }
}
