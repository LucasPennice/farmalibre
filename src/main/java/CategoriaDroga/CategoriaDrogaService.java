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
        log.info("Buscando categoria_droga por ID: " + id);
        ValidatorUtil.requireValidId(id);
        return categoriaDAO.findById(id);
    }

    @Override
    public LinkedList<CategoriaDroga> findAll() {
        log.info("Listando todas las categorias_droga");
        return categoriaDAO.findAll();
    }

    @Override
    public void save(CategoriaDroga categoria) {
        log.info("Guardando categoria_droga: " + categoria);
        validateCategoria(categoria);

        if (categoria.getId() != null && categoriaDAO.findById(categoria.getId().toString()) != null) {
            throw new IllegalArgumentException("La categoría con id " + categoria.getId() + " ya existe");
        }

        categoriaDAO.save(categoria);
    }

    @Override
    public void update(CategoriaDroga categoria) {
        log.info("Actualizando categoria_droga: " + categoria);
        if (categoria == null || categoria.getId() == null) {
            throw new IllegalArgumentException("La categoría o su id no puede ser nula");
        }
        validateCategoria(categoria);

        if (categoriaDAO.findById(categoria.getId().toString()) == null) {
            throw new IllegalArgumentException("La categoría con id " + categoria.getId() + " no existe");
        }

        categoriaDAO.update(categoria);
    }

    @Override
    public void delete(CategoriaDroga categoria) {
        log.info("Eliminando categoria_droga: " + categoria);
        if (categoria == null || categoria.getId() == null) {
            throw new IllegalArgumentException("La categoría o su id no puede ser nula");
        }
        if (categoriaDAO.findById(categoria.getId().toString()) == null) {
            throw new IllegalArgumentException("La categoría con id " + categoria.getId() + " no existe");
        }
        categoriaDAO.delete(categoria);
    }

    @Override
    public void deleteById(String id) {
        log.info("Eliminando categoria_droga por ID: " + id);
        ValidatorUtil.requireValidId(id);
        if (categoriaDAO.findById(id) == null) {
            throw new IllegalArgumentException("La categoría con id " + id + " no existe");
        }
        categoriaDAO.deleteById(id);
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
