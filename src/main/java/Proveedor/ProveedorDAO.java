package Proveedor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Logger;

import interfaces.AbstractDAO;
import interfaces.GenericDAO;

public class ProveedorDAO extends AbstractDAO implements GenericDAO<Proveedor, String> {
    Logger log = Logger.getLogger(ProveedorDAO.class.getName());

    @Override
    public Proveedor findById(String id) {
        log.info("Finding proveedor by ID: " + id);
        Proveedor proveedor = null;
        String sql = "SELECT * FROM proveedor WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setRazonSocial(rs.getString("razon_social"));
                proveedor.setNombreFantasia(rs.getString("nombre_fantasia"));
                proveedor.setCuit(rs.getString("cuit"));
                proveedor.setTipoPersona(TipoPersona.valueOf(rs.getString("tipo_persona")));
                proveedor.setUsuarioId(rs.getInt("usuario_id"));
                proveedor.setOnboardingCompleto(rs.getBoolean("onboarding_completo"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede encontrar el proveedor");
        } finally {
            closeConnection();
        }
        return proveedor;
    }

    public Proveedor findByUsuarioId(String usuarioId) {
        Proveedor proveedor = null;
        String sql = "SELECT * FROM proveedor WHERE usuario_id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, usuarioId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setRazonSocial(rs.getString("razon_social"));
                proveedor.setNombreFantasia(rs.getString("nombre_fantasia"));
                proveedor.setCuit(rs.getString("cuit"));
                proveedor.setTipoPersona(TipoPersona.valueOf(rs.getString("tipo_persona")));
                proveedor.setUsuarioId(rs.getInt("usuario_id"));
                proveedor.setOnboardingCompleto(rs.getBoolean("onboarding_completo"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede encontrar el proveedor");
        } finally {
            closeConnection();
        }
        return proveedor;
    }

    @Override
    public LinkedList<Proveedor> findAll() {
        log.info("Finding all proveedores");
        LinkedList<Proveedor> proveedores = new LinkedList<>();
        String sql = "SELECT * FROM proveedor";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setRazonSocial(rs.getString("razon_social"));
                proveedor.setNombreFantasia(rs.getString("nombre_fantasia"));
                proveedor.setCuit(rs.getString("cuit"));
                proveedor.setTipoPersona(TipoPersona.valueOf(rs.getString("tipo_persona")));
                proveedor.setUsuarioId(rs.getInt("usuario_id"));
                proveedor.setOnboardingCompleto(rs.getBoolean("onboarding_completo"));
                proveedores.add(proveedor);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se pueden listar los proveedores");
        } finally {
            closeConnection();
        }
        return proveedores;
    }

    @Override
    public void save(Proveedor proveedor) {
        log.info("Saving proveedor");
        
        String sql = "INSERT INTO proveedor (usuario_id, razon_social, nombre_fantasia, cuit, tipo_persona) VALUES (?, ?, ?, ?, ?)";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, proveedor.getUsuarioId());
            ps.setString(2, proveedor.getRazonSocial());
            ps.setString(3, proveedor.getNombreFantasia());
            ps.setString(4, proveedor.getCuit());
            ps.setString(5, proveedor.getTipoPersona().name());

            ps.executeUpdate();

            // Obtener el ID generado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                proveedor.setId(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede guardar el proveedor");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void update(Proveedor proveedor) {
        log.info("Updating proveedor with ID: " + proveedor.getId());
                
        String sql = "UPDATE proveedor SET razon_social = ?, nombre_fantasia = ?, cuit = ?, tipo_persona = ?, onboarding_completo = ? WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, proveedor.getRazonSocial());
            ps.setString(2, proveedor.getNombreFantasia());
            ps.setString(3, proveedor.getCuit());
            ps.setString(4, proveedor.getTipoPersona().name());
            ps.setBoolean(5, proveedor.getOnboardingCompleto());
            ps.setInt(6, proveedor.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede actualizar el proveedor");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(Proveedor proveedor) {
        deleteById(String.valueOf(proveedor.getId()));
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting proveedor with ID: " + id);
        String sql = "DELETE FROM proveedor WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede eliminar el proveedor");
        } finally {
            closeConnection();
        }
    }
}