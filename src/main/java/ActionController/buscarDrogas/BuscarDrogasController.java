package ActionController.buscarDrogas;

import java.util.LinkedList;

import Droga.Droga;

public class BuscarDrogasController {
    LinkedList<DrogaDTO> drogasDTO;
    
    public static LinkedList<DrogaDTO> BuscarDrogas(LinkedList<Droga> drogas, String categorias, String nombre, String composicion){
        Boolean noFiltrando = categorias == null && nombre == null && composicion == null;
        
        LinkedList<DrogaDTO> resultado = new LinkedList<>();

        if(noFiltrando) {
            for (Droga droga : drogas) {
               resultado.add(new DrogaDTO(droga));
            }

            return resultado;
        }

        return resultado;
    }
}

