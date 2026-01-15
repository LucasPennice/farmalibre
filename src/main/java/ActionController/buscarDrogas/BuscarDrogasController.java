package ActionController.buscarDrogas;

import java.util.LinkedList;

import CategoriaDroga.CategoriaDroga;
import CategoriaDroga.CategoriaDrogaService;
import Droga.Droga;

public class BuscarDrogasController {
    LinkedList<DrogaDTO> drogasDTO;

    public static LinkedList<DrogaDTO> BuscarDrogas(LinkedList<Droga> drogas, String searchQuery) {
        LinkedList<DrogaDTO> resultado = new LinkedList<>();

        if (searchQuery == null || searchQuery.isEmpty()) {
            for (Droga droga : drogas) {
                resultado.add(new DrogaDTO(droga));
            }
            return resultado;
        }

        CategoriaDrogaService categoriaDrogaService = new CategoriaDrogaService();
        String query = searchQuery.toLowerCase();
        // Filtrar las drogas que coincidan con el searchQuery
        for (Droga droga : drogas) {
            String nombre = droga.getNombre().toLowerCase();
            String composicion = droga.getComposicion().toLowerCase();
            String categoriaId = droga.getCategoriaDroga().getId().toString();

            CategoriaDroga categoria = categoriaDrogaService.findById(categoriaId);
            String categoriaNombre = categoria.getNombre().toLowerCase();

            if (nombre.contains(query) || composicion.contains(query) || categoriaNombre.contains(query)) {

                resultado.add(new DrogaDTO(droga));
            }
        }

        return resultado;
    }
}
