package Proveedor;

import db.DatabaseInitializer;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProveedorServiceTest {

    private ProveedorService service;
    private Proveedor proveedorTest;

    @BeforeAll
    void initDb() {
        DatabaseInitializer.init();
    }

    @BeforeEach
    void setUp() {
        service = new ProveedorService();
        proveedorTest = new Proveedor(null, "Proveedor Test SA", "ProvTest", "20-44631353-4", TipoPersona.JURIDICA);
    }

    @AfterEach
    void cleanUp() {
        try {
            LinkedList<Proveedor> all = service.findAll();
            for (Proveedor p : all) {
                if (p.getRazonSocial() != null && p.getRazonSocial().contains("Proveedor Test")) {
                    service.delete(p);
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("Guardar proveedor válido")
    void saveProveedor() {
        service.save(proveedorTest);
        assertNotNull(proveedorTest.getId());
        assertTrue(proveedorTest.getId() > 0);
    }

    @Test
    @DisplayName("Buscar proveedor por ID")
    void findById() {
        service.save(proveedorTest);
        Proveedor found = service.findById(String.valueOf(proveedorTest.getId()));
        assertNotNull(found);
        assertEquals(proveedorTest.getCuit(), found.getCuit());
    }

    @Test
    @DisplayName("Actualizar proveedor")
    void updateProveedor() {
        service.save(proveedorTest);
        proveedorTest.setNombreFantasia("Nuevo Nombre Fantasia");
        service.update(proveedorTest);
        Proveedor updated = service.findById(String.valueOf(proveedorTest.getId()));
        assertEquals("Nuevo Nombre Fantasia", updated.getNombreFantasia());
    }

    @Test
    @DisplayName("Listar proveedores")
    void listAll() {
        service.save(proveedorTest);
        LinkedList<Proveedor> proveedores = service.findAll();
        assertNotNull(proveedores);
        assertFalse(proveedores.isEmpty());
    }

    @Test
    @DisplayName("Eliminar proveedor")
    void deleteProveedor() {
        service.save(proveedorTest);
        Integer id = proveedorTest.getId();
        service.delete(proveedorTest);
        Proveedor found = service.findById(String.valueOf(id));
        assertNull(found);
    }
}
