package StockDroga;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Logger;

import Droga.Droga;
import Proveedor.Proveedor;
import interfaces.AbstractDAO;
import interfaces.GenericDAO;

public class StockDrogaDAO extends AbstractDAO implements GenericDAO<StockDroga, String> {

    private static final Logger log = Logger.getLogger(StockDrogaDAO.class.getName());

    @Override
    public StockDroga findById(String id) {
        log.info("Finding stock_droga_proveedor by ID: " + id);
        StockDroga stock = null;
        String sql = "SELECT * FROM stock_droga_proveedor WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stock = mapStock(rs);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.severe("Ha ocurrido un error debido a: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return stock;
    }

    @Override
    public LinkedList<StockDroga> findAll() {
        log.info("Finding all stock_droga_proveedor");
        LinkedList<StockDroga> stocks = new LinkedList<>();
        String sql = "SELECT * FROM stock_droga_proveedor";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                stocks.add(mapStock(rs));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.severe("Ha ocurrido un error debido a: " + e.getMessage());
        } finally {
            closeConnection();
        }

        return stocks;
    }

    @Override
    public void save(StockDroga stock) {
        log.info("Saving stock_droga_proveedor");
        String sql = "INSERT INTO stock_droga_proveedor (droga_id, proveedor_id, disponible, precio_unitario) VALUES (?, ?, ?, ?)";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, stock.getDroga().getId());
            ps.setInt(2, stock.getProveedor().getId());
            ps.setInt(3, stock.getDisponible());
            ps.setDouble(4, stock.getPrecioUnitario());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                stock.setId(rs.getInt(1));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.severe("Ha ocurrido un error debido a: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public void update(StockDroga stock) {
        log.info("Updating stock_droga_proveedor with ID: " + stock.getId());
        String sql = "UPDATE stock_droga_proveedor SET droga_id = ?, proveedor_id = ?, disponible = ?, precio_unitario = ? WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, stock.getDroga().getId());
            ps.setInt(2, stock.getProveedor().getId());
            ps.setInt(3, stock.getDisponible());
            ps.setDouble(4, stock.getPrecioUnitario());
            ps.setInt(5, stock.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.severe("Ha ocurrido un error debido a: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(StockDroga stock) {
        deleteById(String.valueOf(stock.getId()));
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting stock_droga_proveedor with ID: " + id);
        String sql = "DELETE FROM stock_droga_proveedor WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.severe("Ha ocurrido un error debido a: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private StockDroga mapStock(ResultSet rs) throws SQLException {
        StockDroga stock = new StockDroga();
        stock.setId(rs.getInt("id"));
        stock.setDisponible(rs.getInt("disponible"));
        stock.setPrecioUnitario(rs.getDouble("precio_unitario"));

        Droga droga = new Droga();
        droga.setId(rs.getInt("droga_id"));
        stock.setDroga(droga);

        Proveedor proveedor = new Proveedor();
        proveedor.setId(rs.getInt("proveedor_id"));
        stock.setProveedor(proveedor);
        return stock;
    }
}
