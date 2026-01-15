package Usuario;

import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.logging.Logger;

import interfaces.GenericService;

public class UsuarioService implements GenericService<Usuario, String> {
    Logger log = Logger.getLogger(UsuarioService.class.getName());

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    public Usuario findById(String id) {
        try {
            log.info("Buscando usuario por ID: " + id);
            // Validaciones de negocio
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("El id del usuario no puede ser nulo o vacío");
            }

            try {
                Integer.parseInt(id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El id debe ser un número válido");
            }

            return usuarioDAO.findById(id);

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public LinkedList<Usuario> findAll() {
        try {
            log.info("Listando todos los usuarios");
            return usuarioDAO.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void save(Usuario usuario) {
        try {
            log.info("Guardando usuario: " + usuario);
            validateSaveUsuario(usuario);

            // Verifico si el usuario ya existe
            if (usuario.getId() != null) {
                Usuario existingUsuario = usuarioDAO.findById(usuario.getId().toString());
                if (existingUsuario != null) {
                    throw new IllegalArgumentException("El usuario con id " + usuario.getId() + " ya existe");
                }
            }

            usuarioDAO.save(usuario);

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Usuario usuario) {
        try {
            log.info("Actualizando usuario: " + usuario);
            if (usuario == null || usuario.getId() == null) {
                throw new IllegalArgumentException("El usuario o el id del usuario no puede ser nulo");
            }

            // Verifico si el usuario existe
            Usuario existingUsuario = usuarioDAO.findById(usuario.getId().toString());
            if (existingUsuario == null) {
                throw new IllegalArgumentException("El usuario con id " + usuario.getId() + " no existe");
            }

            usuarioDAO.update(usuario);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Usuario usuario) {
        try {
            log.info("Eliminando usuario: " + usuario);
            if (usuario == null || usuario.getId() == null) {
                throw new IllegalArgumentException("El usuario o el id del usuario no puede ser nulo");
            }

            // Verifico si el usuario existe
            Usuario existingUsuario = usuarioDAO.findById(usuario.getId().toString());
            if (existingUsuario == null) {
                throw new IllegalArgumentException("El usuario con id " + usuario.getId() + " no existe");
            }

            usuarioDAO.delete(usuario);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            log.info("Eliminando usuario por ID: " + id);
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("El id del usuario no puede ser nulo o vacío");
            }

            try {
                Integer.parseInt(id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El id debe ser un número válido");
            }

            // Verifico si el usuario existe
            Usuario existingUsuario = usuarioDAO.findById(id);
            if (existingUsuario == null) {
                throw new IllegalArgumentException("El usuario con id " + id + " no existe");
            }

            usuarioDAO.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    static public Usuario registrar(String nombreCompleto, String email, String password) {
        // log.info("Registrando nuevo usuario: " + email);

        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo no puede ser vacío");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser vacío");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser vacía");
        }

        // Verifico que no exista otro usuario con el mismo email (nombreUsuario)
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario existente = usuarioDAO.findByNombreUsuario(nombreCompleto);
        
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese nombre de usuario");
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombreCompletoRes(nombreCompleto);
        nuevo.setNombreUsuario(nombreCompleto);
        nuevo.setEmail(email);
        nuevo.setPassEncriptada(hashPassword(password));
        nuevo.setRol(Rol.USUARIO);
        nuevo.setOnboarding_completo(false);

        usuarioDAO.save(nuevo);

        return nuevo;
    }

    static public Usuario autenticar(String nombreCompleto, String password){
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser vacía");
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.findByNombreUsuario(nombreCompleto);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        String passwordHasheada = hashPassword(password);

        if (!passwordHasheada.equals(usuario.getPassEncriptada())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        return usuario;
    }


    private void validateSaveUsuario(Usuario usuario) {
        log.info("Entrando a: " + getClass().getName() + ".validateSaveUsuario");
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        if (usuario.getNombreCompletoRes() == null || usuario.getNombreCompletoRes().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario no puede ser nulo o vacío");
        }

        if (usuario.getDireccion() == null || usuario.getDireccion().trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección del usuario no puede ser nulo o vacío");
        }

        if (usuario.getRol() == null) {
            throw new IllegalArgumentException("El rol del usuario no puede ser nulo o vacío");
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error encriptando la contraseña", e);
        }
    }
}
