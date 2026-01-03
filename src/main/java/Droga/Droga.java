package Droga;

import CategoriaDroga.CategoriaDroga;

public class Droga {
    private Integer id;
    private String nombre;
    private String composicion;
    private String unidad;

    private CategoriaDroga categoriaDroga;

    // Constructors
    public Droga() {
    }

    public Droga(Integer id, String nombre, String composicion, String unidad, CategoriaDroga categoriaDroga) {
        this.id = id;
        this.nombre = nombre;
        this.composicion = composicion;
        this.unidad = unidad;
        this.categoriaDroga = categoriaDroga;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public CategoriaDroga getCategoriaDroga() {
        return categoriaDroga;
    }

    public void setCategoriaDroga(CategoriaDroga categoriaDroga) {
        this.categoriaDroga = categoriaDroga;
    }

}
