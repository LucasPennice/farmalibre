package CategoriaDroga;

import java.util.LinkedList;
import java.util.logging.Logger;

import Utils.ValidatorUtil;
import interfaces.GenericService;

public class CategoriaDrogaService implements GenericService<CategoriaDroga, String> {
    Logger log = Logger.getLogger(CategoriaDrogaService.class.getName());

    private final CategoriaDrogaDAO categoriaDAO = new CategoriaDrogaDAO();

    @Override
    public CategoriaDroga findById(String id) {
        try {
            log.info("Buscando categoria_droga por ID: " + id);
            ValidatorUtil.requireValidId(id);
            return categoriaDAO.findById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public LinkedList<CategoriaDroga> findAll() {
        try {
            log.info("Listando todas las categorias_droga");
            return categoriaDAO.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void save(CategoriaDroga categoria) {
        try {
            log.info("Guardando categoria_droga: " + categoria);
            validateCategoria(categoria);

            if (categoria.getId() != null && categoriaDAO.findById(categoria.getId().toString()) != null) {
                throw new IllegalArgumentException("La categoría con id " + categoria.getId() + " ya existe");
            }

            categoriaDAO.save(categoria);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(CategoriaDroga categoria) {
        try {
            log.info("Actualizando categoria_droga: " + categoria);
            if (categoria == null || categoria.getId() == null) {
                throw new IllegalArgumentException("La categoría o su id no puede ser nula");
            }
            validateCategoria(categoria);

            if (categoriaDAO.findById(categoria.getId().toString()) == null) {
                throw new IllegalArgumentException("La categoría con id " + categoria.getId() + " no existe");
            }

            categoriaDAO.update(categoria);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(CategoriaDroga categoria) {
        try {
            log.info("Eliminando categoria_droga: " + categoria);
            if (categoria == null || categoria.getId() == null) {
                throw new IllegalArgumentException("La categoría o su id no puede ser nula");
            }
            if (categoriaDAO.findById(categoria.getId().toString()) == null) {
                throw new IllegalArgumentException("La categoría con id " + categoria.getId() + " no existe");
            }
            categoriaDAO.delete(categoria);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            log.info("Eliminando categoria_droga por ID: " + id);
            ValidatorUtil.requireValidId(id);
            if (categoriaDAO.findById(id) == null) {
                throw new IllegalArgumentException("La categoría con id " + id + " no existe");
            }
            categoriaDAO.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void validateCategoria(CategoriaDroga categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo o vacío");
        }
        if (categoria.getAprobacion_pendiente() == null) {
            throw new IllegalArgumentException("El estado de aprobación no puede ser nulo");
        }
    }

}
