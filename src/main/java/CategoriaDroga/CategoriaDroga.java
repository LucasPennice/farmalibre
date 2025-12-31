package CategoriaDroga;

public class CategoriaDroga {
    private Integer id;
    private String nombre;
    private Boolean aprobacion_pendiente;

    public CategoriaDroga() {
    }

    public CategoriaDroga(Integer id, String nombre, Boolean aprobacion_pendiente) {
        super();
        this.id = id;
        this.nombre = nombre;
        this.aprobacion_pendiente = aprobacion_pendiente;
    }

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

    public Boolean getAprobacion_pendiente() {
        return aprobacion_pendiente;
    }

    public void setAprobacion_pendiente(Boolean aprobacion_pendiente) {
        this.aprobacion_pendiente = aprobacion_pendiente;
    }

}
