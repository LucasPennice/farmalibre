package Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Logger;

import interfaces.AbstractDAO;
import interfaces.GenericDAO;

public class UsuarioDAO extends AbstractDAO implements GenericDAO<Usuario, String> {
    Logger log = Logger.getLogger(UsuarioDAO.class.getName());

    @Override
    public Usuario findById(String id) {
        log.info("Finding user by ID: " + id);
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombreCompletoRes(rs.getString("nombre_completo_responsable"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setFoto_perfil(rs.getBytes("foto_perfil"));
                usuario.setRol(Rol.valueOf(rs.getString("rol")));
                usuario.setNombreUsuario((rs.getString("nombreUsuario")));
                usuario.setPassEncriptada((rs.getString("passEncriptada")));
                usuario.setEmail(rs.getString("email"));
                usuario.setOnboarding_completo(rs.getBoolean("onboarding_completo"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede encontrar el usuario");
        } finally {
            closeConnection();
        }
        return usuario;
    }

    public Usuario findByNombreUsuario(String nombreUsuario) {
        log.info("Finding user by nombre : " + nombreUsuario);
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE nombreUsuario = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, nombreUsuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombreCompletoRes(rs.getString("nombre_completo_responsable"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setFoto_perfil(rs.getBytes("foto_perfil"));
                usuario.setRol(Rol.valueOf(rs.getString("rol")));
                usuario.setNombreUsuario((rs.getString("nombreUsuario")));
                usuario.setPassEncriptada((rs.getString("passEncriptada")));
                usuario.setEmail(rs.getString("email"));
                usuario.setOnboarding_completo(rs.getBoolean("onboarding_completo"));
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede encontrar el usuario");
        } finally {
            closeConnection();
        }
        return usuario;
    }

    @Override
    public LinkedList<Usuario> findAll() {
        log.info("Finding all users");
        LinkedList<Usuario> usuarios = new LinkedList<>();
        String sql = "SELECT * FROM usuario";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombreCompletoRes(rs.getString("nombre_completo_responsable"));
                usuario.setDireccion(rs.getString("direccion"));
                usuario.setEmail(rs.getString("email"));
                usuario.setFoto_perfil(rs.getBytes("foto_perfil"));
                usuario.setRol(Rol.valueOf(rs.getString("rol"))); // ✅ Convertir String a Enum
                usuarios.add(usuario);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se pueden listar los usuarios");
        } finally {
            closeConnection();
        }
        return usuarios;
    }

    @Override
    public void save(Usuario usuario) {
        log.info("Saving user");
        String sql = "INSERT INTO usuario (nombre_completo_responsable, direccion, foto_perfil, rol, nombreUsuario, passEncriptada, onboarding_completo, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombreCompletoRes());
            ps.setString(2, usuario.getDireccion());
            ps.setBytes(3, usuario.getFoto_perfil());
            ps.setString(4, usuario.getRol().name());
            ps.setString(5, usuario.getNombreUsuario());
            ps.setString(6, usuario.getPassEncriptada());
            ps.setBoolean(7, usuario.getOnboarding_completo());
            ps.setString(8, usuario.getEmail());

            ps.executeUpdate();

            // Obtener el ID generado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede guardar el usuario");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void update(Usuario usuario) {
        log.info("Updating user with ID: " + usuario.getId());
        String sql = "UPDATE usuario SET nombre_completo_responsable = ?, direccion = ?, foto_perfil = ?, rol = ? WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, usuario.getNombreCompletoRes());
            ps.setString(2, usuario.getDireccion());
            ps.setBytes(3, usuario.getFoto_perfil());
            ps.setString(4, usuario.getRol().name()); // ✅ Convertir Enum a String
            ps.setInt(5, usuario.getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede actualizar el usuario");
        } finally {
            closeConnection();
        }
    }

    @Override
    public void delete(Usuario usuario) {
        deleteById(String.valueOf(usuario.getId()));
    }

    @Override
    public void deleteById(String id) {
        log.info("Deleting user with ID: " + id);
        String sql = "DELETE FROM usuario WHERE id = ?";

        try {
            startConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("No se puede eliminar el usuario");
        } finally {
            closeConnection();
        }
    }
}