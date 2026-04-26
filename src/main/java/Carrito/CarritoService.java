package Carrito;

import java.util.LinkedList;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpSession;

/**
 * Servicio de carrito almacenado en la sesión del usuario (sin base de datos).
 */
public class CarritoService {

    private static final String CART_ATTR = "SESSION_CART";
    private static final Logger log = Logger.getLogger(CarritoService.class.getName());

    /** Obtiene el carrito de la sesión o crea uno nuevo. */
    public Carrito getCart(HttpSession session) {
        Carrito cart = (Carrito) session.getAttribute(CART_ATTR);
        if (cart == null) {
            cart = new Carrito();
            cart.setItems(new LinkedList<>());
            session.setAttribute(CART_ATTR, cart);
        }
        return cart;
    }

    /** Verifica si ya existe una droga en el carrito por su ID. */
    public boolean existeDrogaEnCarrito(HttpSession session, Integer drogaId) {
        Carrito cart = getCart(session);
        for (ItemCarrito item : cart.getItems()) {
            if (item.getDroga().getId().equals(drogaId)) {
                return true;
            }
        }
        return false;
    }

    /** Agrega un Item al carrito y recalcula totales. */
    public void addItemDroga(HttpSession session, ItemCarrito item, int precioUnitario) {
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser nulo");
        }
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        Carrito cart = getCart(session);
        LinkedList<ItemCarrito> items = cart.getItems();
        if (items == null) {
            items = new LinkedList<>();
            cart.setItems(items);
        }

        // Allow multiple providers for the same drug - check for duplicate by (drugId + providerId)
        for (ItemCarrito existingItem : items) {
            boolean mismaDroga = existingItem.getDroga().getId().equals(item.getDroga().getId());
            boolean mismoProveedor = existingItem.getProveedor().getId().equals(item.getProveedor().getId());
            if (mismaDroga && mismoProveedor) {
                log.info("El item (droga + proveedor) ya existe en el carrito, no se agrega duplicado");
                return;
            }
        }

        items.add(item);

        cart.setCostoDrogas(cart.getCostoDrogas() + precioUnitario);
        recalcTotal(cart);
        log.info("Item agregado al carrito. Total items: " + items.size());
    }

    /** Elimina un item del carrito (primera coincidencia) y recalcula totales. */
    public void removeItem(HttpSession session, ItemCarrito item, int precioUnitario) {
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser nulo");
        }
        Carrito cart = getCart(session);
        LinkedList<ItemCarrito> items = cart.getItems();
        if (items != null && items.remove(item)) {
            cart.setCostoDrogas(Math.max(0, cart.getCostoDrogas() - Math.max(0, precioUnitario)));
            recalcTotal(cart);
        }
    }

    /** Limpia el carrito. */
    public void clear(HttpSession session) {
        Carrito cart = getCart(session);
        cart.setItems(new LinkedList<>());
        cart.setCostoDrogas(0);
        cart.setCostoEnvio(0);
        cart.setTotal(0);
    }

    /** Permite fijar el costo de envío manualmente y recalcula total. */
    public void setCostoEnvio(HttpSession session, int costoEnvio) {
        if (costoEnvio < 0) {
            throw new IllegalArgumentException("El costo de envío no puede ser negativo");
        }
        Carrito cart = getCart(session);
        cart.setCostoEnvio(costoEnvio);
        recalcTotal(cart);
    }

    private void recalcTotal(Carrito cart) {
        int total = safe(cart.getCostoDrogas()) + safe(cart.getCostoEnvio());
        cart.setTotal(total);
    }

    private int safe(Integer value) {
        return value == null ? 0 : value;
    }
}
