package Droga;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Logger;

import CategoriaDroga.CategoriaDroga;
import interfaces.AbstractDAO;
import interfaces.GenericDAO;

public class DrogaDAO extends AbstractDAO implements GenericDAO<Droga, String> {

    private static final Logger log = Logger.getLogger(DrogaDAO.class.getName());

    @Override
    public Droga findById(String id) {
        log.info("Finding droga by ID: " + id);
        Droga droga = null;
        String sql = "SELECT * FROM droga WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                droga = mapDroga(rs);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede encontrar la droga");
        } finally {
            closeConnection();
        }

        return droga;
    }

    @Override
    public LinkedList<Droga> findAll() {
        log.info("Finding all drogas");
        LinkedList<Droga> drogas = new LinkedList<>();
        String sql = "SELECT * FROM droga";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                drogas.add(mapDroga(rs));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se pueden listar las drogas");
        } finally {
            closeConnection();
        }

        return drogas;
    }

    @Override
    public void save(Droga droga) {
        log.info("Saving droga");
        String sql = "INSERT INTO droga (nombre, composicion, unidad, categoria_id) VALUES (?, ?, ?, ?)";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, droga.getNombre());
            ps.setString(2, droga.getComposicion());
            ps.setString(3, droga.getUnidad());
            ps.setInt(4, droga.getCategoriaDroga().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                droga.setId(rs.getInt(1));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede guardar la droga");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void update(Droga droga) {
        log.info("Updating droga with ID: " + droga.getId());
        String sql = "UPDATE droga SET nombre = ?, composicion = ?, unidad = ?, categoria_id = ? WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, droga.getNombre());
            ps.setString(2, droga.getComposicion());
            ps.setString(3, droga.getUnidad());
            ps.setInt(4, droga.getCategoriaDroga().getId());
            ps.setInt(5, droga.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede actualizar la droga");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(Droga droga) {
        deleteById(String.valueOf(droga.getId()));
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting droga with ID: " + id);
        String sql = "DELETE FROM droga WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede eliminar la droga");
        } finally {
            closeConnection();
        }
    }

    private Droga mapDroga(ResultSet rs) throws SQLException {
        Droga droga = new Droga();
        droga.setId(rs.getInt("id"));
        droga.setNombre(rs.getString("nombre"));
        droga.setComposicion(rs.getString("composicion"));
        droga.setUnidad(rs.getString("unidad"));

        CategoriaDroga categoria = new CategoriaDroga();
        categoria.setId(rs.getInt("categoria_id"));
        droga.setCategoriaDroga(categoria);
        return droga;
    }

    public LinkedList<Droga> findBySearchQuery(String searchQuery) {
        log.info("Finding drogas by search query: " + searchQuery);
        LinkedList<Droga> drogas = new LinkedList<>();
        String sql = "SELECT * FROM droga WHERE LOWER(nombre) LIKE ? OR LOWER(composicion) LIKE ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            String likePattern = "%" + searchQuery.toLowerCase() + "%";
            ps.setString(1, likePattern);
            ps.setString(2, likePattern);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                drogas.add(mapDroga(rs));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se pueden buscar las drogas");
        } finally {
            closeConnection();
        }

        return drogas;
    }
}
