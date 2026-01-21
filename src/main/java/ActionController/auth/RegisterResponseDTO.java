package ActionController.auth;

import Usuario.Usuario;

public class RegisterResponseDTO {
    private Usuario usuario;

    public RegisterResponseDTO() {
    }

    public RegisterResponseDTO(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
