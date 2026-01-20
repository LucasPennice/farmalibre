package ActionController.actualizarPerfil;

import Proveedor.TipoPersona;

public class PerfilInfoDTO {
    private String nombreDelResponsable;
    private String emailDeContacto;
    private String direccionDelResponsable;
    private byte[] fotoPerfil;
    private String razonSocial;
    private String nombreFantasia;
    private String CUIT;
    private Boolean esProveedor;
    private TipoPersona tipoPersona;
    
    public Boolean getEsProveedor() {
        return esProveedor;
    }

    public void setEsProveedor(Boolean esProveedor) {
        this.esProveedor = esProveedor;
    }

    public String getNombreDelResponsable() {
        return nombreDelResponsable;
    }

    public void setNombreDelResponsable(String nombreDelResponsable) {
        this.nombreDelResponsable = nombreDelResponsable;
    }

    public String getEmailDeContacto() {
        return emailDeContacto;
    }

    public void setEmailDeContacto(String emailDeContacto) {
        this.emailDeContacto = emailDeContacto;
    }

    public String getDireccionDelResponsable() {
        return direccionDelResponsable;
    }

    public void setDireccionDelResponsable(String direccionDelResponsable) {
        this.direccionDelResponsable = direccionDelResponsable;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
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

    public String getCUIT() {
        return CUIT;
    }

    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }
}