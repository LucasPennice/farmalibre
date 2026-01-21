package Carrito;

import java.util.LinkedList;


public class Carrito {
    private Integer costoDrogas = 0;
    private Integer costoEnvio = 0;
    private Integer total = 0;

    private LinkedList<ItemCarrito> items;

    // Constructors
    public Carrito() {
    };

    public Carrito(Integer id, Integer costoDrogas, Integer costoEnvio, Integer total, LinkedList<ItemCarrito> items) {
        this.costoDrogas = costoDrogas;
        this.costoEnvio = costoEnvio;
        this.total = total;
        this.items = items;
    }

    // Getters and Setters

    public Integer getCostoDrogas() {
        return costoDrogas;
    }

    public void setCostoDrogas(Integer costoDrogas) {
        this.costoDrogas = costoDrogas;
    }

    public Integer getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(Integer costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public LinkedList<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(LinkedList<ItemCarrito> items) {
        this.items = items;
    }

    public void setItem(ItemCarrito item) {
        this.items.add(item);   
    }

}
