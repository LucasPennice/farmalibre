package Droga;

import java.util.LinkedList;
import java.util.logging.Logger;

import CategoriaDroga.CategoriaDroga;
import Utils.ValidatorUtil;
import interfaces.GenericService;

public class DrogaService implements GenericService<Droga, String> {
    Logger log = Logger.getLogger(DrogaService.class.getName());

    private final DrogaDAO drogaDAO = new DrogaDAO();

    @Override
    public Droga findById(String id) {
        try {
            log.info("Buscando droga por ID: " + id);
            ValidatorUtil.requireValidId(id);
            return drogaDAO.findById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public LinkedList<Droga> findAll() {
        try {
            log.info("Listando todas las drogas");
            return drogaDAO.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void save(Droga droga) {
        try {
            log.info("Guardando droga: " + droga);
            validateDroga(droga);

            if (droga.getId() != null && drogaDAO.findById(droga.getId().toString()) != null) {
                throw new IllegalArgumentException("La droga con id " + droga.getId() + " ya existe");
            }

            drogaDAO.save(droga);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Droga droga) {
        try {
            log.info("Actualizando droga: " + droga);
            if (droga == null || droga.getId() == null) {
                throw new IllegalArgumentException("La droga o el id no puede ser nulo");
            }
            validateDroga(droga);

            if (drogaDAO.findById(droga.getId().toString()) == null) {
                throw new IllegalArgumentException("La droga con id " + droga.getId() + " no existe");
            }

            drogaDAO.update(droga);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Droga droga) {
        try {
            log.info("Eliminando droga: " + droga);
            if (droga == null || droga.getId() == null) {
                throw new IllegalArgumentException("La droga o el id no puede ser nulo");
            }
            if (drogaDAO.findById(droga.getId().toString()) == null) {
                throw new IllegalArgumentException("La droga con id " + droga.getId() + " no existe");
            }
            drogaDAO.delete(droga);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            log.info("Eliminando droga por ID: " + id);
            ValidatorUtil.requireValidId(id);
            if (drogaDAO.findById(id) == null) {
                throw new IllegalArgumentException("La droga con id " + id + " no existe");
            }
            drogaDAO.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public LinkedList<Droga> findBySearchQuery(String searchQuery) {
        try {
            log.info("Buscando drogas con query: " + searchQuery);
            if (ValidatorUtil.isBlank(searchQuery)) {
                return findAll();
            }
            return drogaDAO.findBySearchQuery(searchQuery);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void validateDroga(Droga droga) {
        if (droga == null) {
            throw new IllegalArgumentException("La droga no puede ser nula");
        }
        if (ValidatorUtil.isBlank(droga.getNombre())) {
            throw new IllegalArgumentException("El nombre de la droga no puede ser nulo o vacío");
        }
        if (ValidatorUtil.isBlank(droga.getComposicion())) {
            throw new IllegalArgumentException("La composición no puede ser nula o vacía");
        }
        if (ValidatorUtil.isBlank(droga.getUnidad())) {
            throw new IllegalArgumentException("La unidad no puede ser nula o vacía");
        }
        if (ValidatorUtil.validateNumberGreaterThanZero(droga.getUnidad())) {
            throw new IllegalArgumentException("La unidad debe ser un número mayor a cero");
        }
        CategoriaDroga cat = droga.getCategoriaDroga();
        if (cat == null || cat.getId() == null) {
            throw new IllegalArgumentException("La categoría de la droga no puede ser nula");
        }
    }

}
