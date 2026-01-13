package StockDroga;

import java.util.LinkedList;
import java.util.logging.Logger;

import Droga.Droga;
import Proveedor.Proveedor;
import Utils.ValidatorUtil;
import interfaces.GenericService;

public class StockDrogaService implements GenericService<StockDroga, String> {
    Logger log = Logger.getLogger(StockDrogaService.class.getName());

    private final StockDrogaDAO stockDAO = new StockDrogaDAO();

    @Override
    public StockDroga findById(String id) {
        try {
            log.info("Buscando stock por ID: " + id);
            ValidatorUtil.requireValidId(id);
            return stockDAO.findById(id);

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public StockDroga findByProveedorAndDroga(Proveedor proveedor, Droga droga) {
    // Por reglas de negocio solo existe un stock por par de proveedor y droga

        try {
            log.info("Buscando stock por ID prov y droga: " + proveedor.getId() + "" + droga.getId());
            return stockDAO.findByProveedorAndDroga(proveedor.getId(),droga.getId());

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public LinkedList<StockDroga> findByProveedor(Proveedor proveedor) {
        try {
            log.info("Buscando stock por ID proveedor: " + proveedor.getId());
            return stockDAO.findByProveedor(proveedor.getId());

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public LinkedList<StockDroga> findByDroga(Droga droga) {
        try {
            log.info("Buscando stock por ID droga: " + droga.getId());
            return stockDAO.findByDroga(droga.getId());

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public LinkedList<StockDroga> findAll() {
        try {
            log.info("Listando todos los stocks de droga");
            return stockDAO.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void save(StockDroga stock) {
        try {
            log.info("Guardando stock: " + stock);
            validateStock(stock);

            if (stock.getId() != null && stockDAO.findById(stock.getId().toString()) != null) {
                throw new IllegalArgumentException("El stock con id " + stock.getId() + " ya existe");
            }

            stockDAO.save(stock);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(StockDroga stock) {
        try {
            log.info("Actualizando stock: " + stock);
            if (stock == null || stock.getId() == null) {
                throw new IllegalArgumentException("El stock o su id no puede ser nulo");
            }
            validateStock(stock);

            if (stockDAO.findById(stock.getId().toString()) == null) {
                throw new IllegalArgumentException("El stock con id " + stock.getId() + " no existe");
            }

            stockDAO.update(stock);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(StockDroga stock) {
        try {
            log.info("Eliminando stock: " + stock);
            if (stock == null || stock.getId() == null) {
                throw new IllegalArgumentException("El stock o su id no puede ser nulo");
            }
            if (stockDAO.findById(stock.getId().toString()) == null) {
                throw new IllegalArgumentException("El stock con id " + stock.getId() + " no existe");
            }
            stockDAO.delete(stock);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            log.info("Eliminando stock por ID: " + id);
            ValidatorUtil.requireValidId(id);
            if (stockDAO.findById(id) == null) {
                throw new IllegalArgumentException("El stock con id " + id + " no existe");
            }
            stockDAO.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void validateStock(StockDroga stock) {
        if (stock == null) {
            throw new IllegalArgumentException("El stock no puede ser nulo");
        }
        Droga droga = stock.getDroga();
        Proveedor proveedor = stock.getProveedor();
        if (droga == null || droga.getId() == null) {
            throw new IllegalArgumentException("La droga asociada no puede ser nula");
        }
        if (proveedor == null || proveedor.getId() == null) {
            throw new IllegalArgumentException("El proveedor asociado no puede ser nulo");
        }
        if (stock.getDisponible() == null) {
            throw new IllegalArgumentException("La disponibilidad no puede ser nula");
        }
        if (!ValidatorUtil.validateNumberGreaterThanZero(stock.getDisponible())) {
            throw new IllegalArgumentException("La disponibilidad debe ser un número mayor o igual a cero");
        }
        if (stock.getPrecioUnitario() == null) {
            throw new IllegalArgumentException("El precio unitario no puede ser nulo");
        }
        if (!ValidatorUtil.validateNumberGreaterThanZero(stock.getPrecioUnitario())) {
            throw new IllegalArgumentException("El precio unitario debe ser un número mayor a cero");
        }
    }
}
