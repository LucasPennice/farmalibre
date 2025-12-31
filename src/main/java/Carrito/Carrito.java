package Carrito;

import java.util.List;

import Droga.Droga;

public class Carrito {
    private Integer costoDrogas = 0;
    private Integer costoEnvio = 0;
    private Integer total = 0;

    private List<Droga> listDroga;

    // Constructors
    public Carrito() {
    };

    public Carrito(Integer id, Integer costoDrogas, Integer costoEnvio, Integer total, List<Droga> listDroga) {
        this.costoDrogas = costoDrogas;
        this.costoEnvio = costoEnvio;
        this.total = total;
        this.listDroga = listDroga;
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

    public List<Droga> getListDroga() {
        return listDroga;
    }

    public void setListDroga(List<Droga> listDroga) {
        this.listDroga = listDroga;
    }

}
