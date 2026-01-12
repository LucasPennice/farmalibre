package Usuario;

public class Usuario {
    private Integer id;
    private String nombreCompletoRes;
    private String direccion;
    private String nombreUsuario;
    @SuppressWarnings("unused")
    private String passEncriptada;
    private byte[] foto_perfil;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(Integer id, String NombreCompletoRes, String direccion, byte[] foto_perfil, Rol rol, String nombreUsuario, String passEncriptada) {
        this.id = id;
        this.nombreCompletoRes = NombreCompletoRes;
        this.direccion = direccion;
        this.foto_perfil = foto_perfil;
        this.rol = rol;
        this.nombreUsuario = nombreUsuario;
        this.passEncriptada = passEncriptada;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}
