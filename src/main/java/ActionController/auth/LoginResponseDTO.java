package ActionController.auth;

import Usuario.Usuario;

public class LoginResponseDTO {
    private Usuario usuario;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Usuario usuario) {
        this.usuario = usuario;
    }

        public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}
