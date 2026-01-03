package Usuario;

public class Usuario {
    private Integer id;
    private String nombre_completo_responsable;
    private String direccion;
    private byte[] foto_perfil;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(Integer id, String nombre_completo_responsable, String direccion, byte[] foto_perfil, Rol rol) {
        this.id = id;
        this.nombre_completo_responsable = nombre_completo_responsable;
        this.direccion = direccion;
        this.foto_perfil = foto_perfil;
        this.rol = rol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre_completo_responsable() {
        return nombre_completo_responsable;
    }

    public void setNombre_completo_responsable(String nombre_completo_responsable) {
        this.nombre_completo_responsable = nombre_completo_responsable;
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
}
