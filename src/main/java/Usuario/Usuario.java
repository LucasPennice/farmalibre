package Usuario;

import java.util.LinkedList;

import Proveedor.Proveedor;
import Proveedor.ProveedorService;

public class Usuario {
    private Integer id;
    private String nombreCompletoRes;
    private String direccion;
    private String email;

    private String nombreUsuario;
    private String passEncriptada;
    private byte[] foto_perfil;
    private Rol rol;
    private Boolean onboarding_completo;
    private Boolean esAdmin;
    private Boolean esProveedor;

    private LinkedList<Integer> stockProveedorWishlistIds;



    public Usuario() {
    }

    public Usuario(Integer id, String NombreCompletoRes, String direccion, byte[] foto_perfil, Rol rol,
            String nombreUsuario, String passEncriptada, LinkedList<Integer> stockProveedorWishlistIds) {
        this.id = id;
        this.nombreCompletoRes = NombreCompletoRes;
        this.direccion = direccion;
        this.foto_perfil = foto_perfil;
        this.rol = rol;
        this.nombreUsuario = nombreUsuario;
        this.passEncriptada = passEncriptada;
        this.stockProveedorWishlistIds = stockProveedorWishlistIds != null ? stockProveedorWishlistIds : new LinkedList<Integer>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreCompletoRes() {
        return nombreCompletoRes;
    }

    public void setNombreCompletoRes(String nombreCompletoRes) {
        this.nombreCompletoRes = nombreCompletoRes;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public byte[] getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(byte[] foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setPassEncriptada(String passEncriptada) {
        this.passEncriptada = passEncriptada;
    }

    public String getPassEncriptada() {
        return passEncriptada;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public Boolean getOnboarding_completo() {
        return onboarding_completo;
    }

    public void setOnboarding_completo(Boolean onboarding_completo) {
        this.onboarding_completo = onboarding_completo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEsAdmin(){
        return esAdmin;
    }

    public Boolean getEsProveedor(){
        return esProveedor;
    }
    
    public void recalcularFlags() {
        if( this.getRol() == Rol.ADMIN){
            this.esAdmin = true;
        }else{
            this.esAdmin = false;
        }

        ProveedorService proveedorService = new ProveedorService();
        
        try {
            Proveedor proveedor = proveedorService.findByUsuarioId(this.getId().toString());
            
            if(proveedor == null){
                this.esProveedor = false;
            }else{
                this.esProveedor = true;
            }
        } catch (Exception e) {
            this.esProveedor = false;
            throw new RuntimeException(e.getMessage());
        }
    }

    public LinkedList<Integer> getStockProveedorWishlistIds() {
        return stockProveedorWishlistIds;
    }

    public void setStockProveedorWishlistIds(LinkedList<Integer> stockProveedorWishlistIds) {
        this.stockProveedorWishlistIds = stockProveedorWishlistIds;
    }
}
