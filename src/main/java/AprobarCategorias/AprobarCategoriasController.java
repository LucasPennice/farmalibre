package AprobarCategorias;

import java.util.LinkedList;

import CategoriaDroga.CategoriaDroga;
import CategoriaDroga.CategoriaDrogaService;
import Droga.Droga;
import Droga.DrogaService;
import StockDroga.StockDroga;
import StockDroga.StockDrogaService;

public class AprobarCategoriasController {
    public static LinkedList<CategoriaDroga> GetCategoriasAprobadas () {
        LinkedList<CategoriaDroga> result = new LinkedList<>();

        CategoriaDrogaService categoriaDrogaService = new CategoriaDrogaService();

        LinkedList<CategoriaDroga> temp =  categoriaDrogaService.findAll();

        for (CategoriaDroga c : temp) {
            if (!c.getAprobacion_pendiente()) {
                result.add(c);
            }
        }
        return result;
    }

    public static void AprobarCategoria(String categoriaId){
        CategoriaDrogaService categoriaDrogaService = new CategoriaDrogaService();

        CategoriaDroga categoria = categoriaDrogaService.findById(categoriaId);

        categoria.setAprobacion_pendiente(false);

        categoriaDrogaService.update(categoria);
    }
   
    public static void EditarCategoria(String categoriaId, String nuevoNombre){
        CategoriaDrogaService categoriaDrogaService = new CategoriaDrogaService();

        CategoriaDroga categoria = categoriaDrogaService.findById(categoriaId);

        categoria.setNombre(nuevoNombre);

        categoriaDrogaService.update(categoria);
    }
   
    public static void RechazarCategoria(String categoriaId){
        CategoriaDrogaService categoriaDrogaService = new CategoriaDrogaService();

        CategoriaDroga categoria = categoriaDrogaService.findById(categoriaId);

        DrogaService drogaService = new DrogaService();
        
        LinkedList<Droga> drogasABorrar = new LinkedList<>();

        for (Droga droga : drogaService.findAll()) {
            if(droga.getCategoriaDroga().getId() == categoria.getId()){
                drogasABorrar.add(droga);
            }
        }

        StockDrogaService stockDrogaService = new StockDrogaService();

        for (StockDroga stock : stockDrogaService.findAll()) {
            for (Droga droga : drogasABorrar) {
                if(stock.getDroga().getId() == droga.getId()){
                    stockDrogaService.delete(stock);
                }
            }
        }  

        for (Droga droga : drogasABorrar) {
            drogaService.delete(droga);
        }
        
        categoriaDrogaService.delete(categoria);
    }
}
