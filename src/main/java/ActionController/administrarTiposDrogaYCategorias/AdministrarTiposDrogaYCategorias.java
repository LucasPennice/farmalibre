package ActionController.administrarTiposDrogaYCategorias;

import CategoriaDroga.CategoriaDroga;
import CategoriaDroga.CategoriaDrogaService;
import Droga.Droga;
import Droga.DrogaService;

public class AdministrarTiposDrogaYCategorias {
    public static void CargarNuevoTipoDroga (String userId, String nombreDroga, String composicion, String unidad, String nombreCategoria) {
        try {
            CategoriaDrogaService categoriaDrogaService = new CategoriaDrogaService();
                
            CategoriaDroga categoria = categoriaDrogaService.findByName(nombreCategoria);

            if(categoria == null){
                categoria = CargarNuevaCategoria(nombreCategoria);
            }

            DrogaService drogaService = new DrogaService();

            Droga nuevaDroga = new Droga();

            nuevaDroga.setCategoriaDroga(categoria);
            nuevaDroga.setComposicion(composicion);
            nuevaDroga.setNombre(nombreDroga);
            nuevaDroga.setUnidad(unidad);

            drogaService.save(nuevaDroga);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static CategoriaDroga CargarNuevaCategoria(String nombreCategoria){
        try {
            CategoriaDrogaService categoriaDrogaService = new CategoriaDrogaService();
            
            CategoriaDroga categoriaExistente = categoriaDrogaService.findByName(nombreCategoria);

            if(categoriaExistente != null){
                throw new RuntimeException("La categoria " + nombreCategoria + " ya existe");
            }

            CategoriaDroga nuevaCategoria = new CategoriaDroga();
            nuevaCategoria.setAprobacion_pendiente(true);
            nuevaCategoria.setNombre(nombreCategoria);

            categoriaDrogaService.save(nuevaCategoria);   

            return nuevaCategoria;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
