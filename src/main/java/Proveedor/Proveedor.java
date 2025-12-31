package Proveedor;

public class Proveedor {
    private Integer id;
    private String razonSocial;
    private String nombreFantasia;
    private String cuit;
    private TipoPersona tipoPersona;

    public Proveedor() {

    }

    public Proveedor(Integer id, String razonSocial, String nombreFantasia, String cuit, TipoPersona tipoPersona) {
        this.id = id;
        this.razonSocial = razonSocial;
        this.nombreFantasia = nombreFantasia;
        this.cuit = cuit;
        this.tipoPersona = tipoPersona;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreFantasia() {
        return nombreFantasia;
    }

    public void setNombreFantasia(String nombreFantasia) {
        this.nombreFantasia = nombreFantasia;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

}