package Carrito;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpSession;

import Droga.Droga;

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
            cart.setListDroga(new ArrayList<>());
            session.setAttribute(CART_ATTR, cart);
        }
        return cart;
    }

    /** Agrega una droga al carrito y recalcula totales. */
    public void addDroga(HttpSession session, Droga droga, int precioUnitario) {
        if (droga == null) {
            throw new IllegalArgumentException("La droga no puede ser nula");
        }
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        Carrito cart = getCart(session);
        List<Droga> items = cart.getListDroga();
        if (items == null) {
            items = new ArrayList<>();
            cart.setListDroga(items);
        }
        items.add(droga);

        cart.setCostoDrogas(cart.getCostoDrogas() + precioUnitario);
        recalcTotal(cart);
        log.info("Droga agregada al carrito. Total drogas: " + items.size());
    }

    /** Elimina una droga (primera coincidencia) y recalcula totales. */
    public void removeDroga(HttpSession session, Droga droga, int precioUnitario) {
        if (droga == null) {
            throw new IllegalArgumentException("La droga no puede ser nula");
        }
        Carrito cart = getCart(session);
        List<Droga> items = cart.getListDroga();
        if (items != null && items.remove(droga)) {
            cart.setCostoDrogas(Math.max(0, cart.getCostoDrogas() - Math.max(0, precioUnitario)));
            recalcTotal(cart);
        }
    }

    /** Limpia el carrito. */
    public void clear(HttpSession session) {
        Carrito cart = getCart(session);
        cart.setListDroga(new ArrayList<>());
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
