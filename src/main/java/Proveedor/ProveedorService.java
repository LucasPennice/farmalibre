package Proveedor;

import java.util.LinkedList;
import java.util.logging.Logger;

import Utils.ValidatorUtil;
import interfaces.GenericService;

public class ProveedorService implements GenericService<Proveedor, String> {
    Logger log = Logger.getLogger(ProveedorService.class.getName());

    private ProveedorDAO proveedorDAO = new ProveedorDAO();

    @Override
    public Proveedor findById(String id) {
        try {
            log.info("Buscando proveedor por ID: " + id);
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("El id del proveedor no puede ser nulo o vacío");
            }
            try {
                Integer.parseInt(id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El id debe ser un número válido");
            }
            return proveedorDAO.findById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
  
    public Proveedor findByUsuarioId(String usuarioId) {
        try {
            log.info("Buscando proveedor por usuario ID: " + usuarioId);
            if (usuarioId == null || usuarioId.trim().isEmpty()) {
                throw new IllegalArgumentException("El usuarioId del proveedor no puede ser nulo o vacío");
            }
            try {
                return proveedorDAO.findByUsuarioId(usuarioId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El usuarioId debe ser valido");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public LinkedList<Proveedor> findAll() {
        try {
            log.info("Listando todos los proveedores");
            return proveedorDAO.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void save(Proveedor proveedor) {
        try {
            log.info("Guardando proveedor: " + proveedor);
            validateProveedor(proveedor);

            if (proveedor.getId() != null) {
                Proveedor existing = proveedorDAO.findById(proveedor.getId().toString());
                if (existing != null) {
                    throw new IllegalArgumentException("El proveedor con id " + proveedor.getId() + " ya existe");
                }
            }

            proveedorDAO.save(proveedor);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(Proveedor proveedor) {
        try {
            log.info("Actualizando proveedor: " + proveedor);
            if (proveedor == null || proveedor.getId() == null) {
                throw new IllegalArgumentException("El proveedor o el id del proveedor no puede ser nulo");
            }
            validateProveedor(proveedor);

            Proveedor existing = proveedorDAO.findById(proveedor.getId().toString());
            if (existing == null) {
                throw new IllegalArgumentException("El proveedor con id " + proveedor.getId() + " no existe");
            }

            proveedorDAO.update(proveedor);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Proveedor proveedor) {
        try {
            log.info("Eliminando proveedor: " + proveedor);
            if (proveedor == null || proveedor.getId() == null) {
                throw new IllegalArgumentException("El proveedor o el id del proveedor no puede ser nulo");
            }

            Proveedor existing = proveedorDAO.findById(proveedor.getId().toString());
            if (existing == null) {
                throw new IllegalArgumentException("El proveedor con id " + proveedor.getId() + " no existe");
            }

            proveedorDAO.delete(proveedor);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            log.info("Eliminando proveedor por ID: " + id);
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("El id del proveedor no puede ser nulo o vacío");
            }
            try {
                Integer.parseInt(id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("El id debe ser un número válido");
            }

            Proveedor existing = proveedorDAO.findById(id);
            if (existing == null) {
                throw new IllegalArgumentException("El proveedor con id " + id + " no existe");
            }

            proveedorDAO.deleteById(id);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void validateProveedor(Proveedor proveedor) {
        if (proveedor == null) {
            throw new IllegalArgumentException("El proveedor no puede ser nulo");
        }
        if (proveedor.getRazonSocial() == null || proveedor.getRazonSocial().trim().isEmpty()) {
            throw new IllegalArgumentException("La razón social no puede ser nula o vacía");
        }
        if (proveedor.getCuit() == null || proveedor.getCuit().trim().isEmpty()) {
            throw new IllegalArgumentException("El CUIT no puede ser nulo o vacío");
        }
        if (!ValidatorUtil.validateCuit(proveedor.getCuit())) {
            throw new IllegalArgumentException("El CUIT no es válido");
        }
        if (proveedor.getTipoPersona() == null) {
            throw new IllegalArgumentException("El tipo de persona no puede ser nulo");
        }
    }
}
