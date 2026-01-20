package ActionController.actualizarPerfil;

import java.io.InputStream;

import Proveedor.Proveedor;
import Proveedor.ProveedorService;
import Proveedor.TipoPersona;
import Usuario.Usuario;
import Usuario.UsuarioService;
import jakarta.servlet.http.Part;

public class ActualizarPerfilController {
    public static PerfilInfoDTO getItem(String userId){
        try {
        PerfilInfoDTO result = new PerfilInfoDTO();

        UsuarioService usuarioService = new UsuarioService();
        Usuario usuario = usuarioService.findById(userId);

        usuario.recalcularFlags();

        result.setDireccionDelResponsable(usuario.getDireccion());
        result.setEmailDeContacto(usuario.getEmail());
        result.setEsProveedor(usuario.getEsProveedor());
        result.setFotoPerfil(usuario.getFoto_perfil());
        result.setNombreDelResponsable(usuario.getNombreCompletoRes());
        
        if(!usuario.getEsProveedor()){
            return result;
        }
        
        ProveedorService proveedorService = new ProveedorService();
        Proveedor proveedor = proveedorService.findByUsuarioId(userId);
        
        result.setCUIT(proveedor.getCuit());
        result.setNombreFantasia(proveedor.getNombreFantasia());
        result.setRazonSocial(proveedor.getRazonSocial());
        result.setTipoPersona(proveedor.getTipoPersona());

        return result;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public static void Actualizar(String idUsuario, String nombreUsuario, String emailContacto, String direccionResponsable, Part fotoPerfilInput, Boolean esProveedor, String razonSocial, String nombreFantasia, String CUIT, TipoPersona tipoPersona){
        try {
            UsuarioService usuarioService = new UsuarioService();
            Usuario usuario = usuarioService.findById(idUsuario);

            usuario.setNombreCompletoRes(nombreUsuario);
            usuario.setEmail(emailContacto);
            usuario.setDireccion(direccionResponsable);

            byte[] fotoPerfilBytes = null;

            if (fotoPerfilInput != null && fotoPerfilInput.getSize() > 0) {
                InputStream is = fotoPerfilInput.getInputStream();
                fotoPerfilBytes = is.readAllBytes();
            }
            
            byte[] fotoPerfil = fotoPerfilBytes;

            usuario.setFoto_perfil(fotoPerfil);
            
            usuarioService.update(usuario);

            if(!esProveedor){return;}

            ProveedorService proveedorService = new ProveedorService();
            Proveedor proveedor = proveedorService.findByUsuarioId(idUsuario);

            proveedor.setRazonSocial(razonSocial);
            proveedor.setNombreFantasia(nombreFantasia);
            proveedor.setCuit(CUIT);
            proveedor.setTipoPersona(tipoPersona);

            proveedorService.update(proveedor);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
    }
}
