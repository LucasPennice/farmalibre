package CategoriaDroga;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Logger;

import interfaces.AbstractDAO;
import interfaces.GenericDAO;

public class CategoriaDrogaDAO extends AbstractDAO implements GenericDAO<CategoriaDroga, String> {

    private static final Logger log = Logger.getLogger(CategoriaDrogaDAO.class.getName());

    @Override
    public CategoriaDroga findById(String id) {
        log.info("Finding categoria_droga by ID: " + id);
        CategoriaDroga categoria = null;
        String sql = "SELECT * FROM categoria_droga WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                categoria = mapCategoria(rs);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se pudieron cargar la categoría.");
        } finally {
            closeConnection();
        }
        return categoria;
    }

    @Override
    public LinkedList<CategoriaDroga> findAll() {
        log.info("Finding all categorias_droga");
        LinkedList<CategoriaDroga> categorias = new LinkedList<>();
        String sql = "SELECT * FROM categoria_droga";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categorias.add(mapCategoria(rs));
            }

            rs.close();
            ps.close();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se pudieron cargar las categorías.");
        } finally {
            closeConnection();
        }
        return categorias;
    }

    @Override
    public void save(CategoriaDroga categoria) {
        log.info("Saving categoria_droga");
        String sql = "INSERT INTO categoria_droga (nombre, aprobacion_pendiente) VALUES (?, ?)";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, categoria.getNombre());
            ps.setBoolean(2, categoria.getAprobacion_pendiente());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                categoria.setId(rs.getInt(1));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede guardar la categoría");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void update(CategoriaDroga categoria) {
        log.info("Updating categoria_droga with ID: " + categoria.getId());
        String sql = "UPDATE categoria_droga SET nombre = ?, aprobacion_pendiente = ? WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, categoria.getNombre());
            ps.setBoolean(2, categoria.getAprobacion_pendiente());
            ps.setInt(3, categoria.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede actualizar la categoría");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(CategoriaDroga categoria) {
        deleteById(String.valueOf(categoria.getId()));
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting categoria_droga with ID: " + id);
        String sql = "DELETE FROM categoria_droga WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede borrar la categoria");
        } finally {
            closeConnection();
        }
    }

    private CategoriaDroga mapCategoria(ResultSet rs) throws SQLException {
        CategoriaDroga categoria = new CategoriaDroga();
        categoria.setId(rs.getInt("id"));
        categoria.setNombre(rs.getString("nombre"));
        categoria.setAprobacion_pendiente(rs.getBoolean("aprobacion_pendiente"));
        return categoria;
    }
}
