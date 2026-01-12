package Usuario;

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

        /*
         * TODO el usuario al crear un perfil, eligue si o si un rol de proovedor o
         * comprador?
         */
        // TODO Revisar roles, solo tenemos ADMIN y USUARIO, que pasa con otros roles?
        // if (!usuario.getRol().equals(Rol.ADMIN) ||
        // !usuario.getRol().equals(Rol.USUARIO)) {
        // throw new IllegalArgumentException("El rol del usuario debe ser ADMIN o
        // USUARIO para poder ser guardado");
        // }

    }
}
