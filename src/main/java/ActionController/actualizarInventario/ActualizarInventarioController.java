package ActionController.actualizarInventario;

import java.util.LinkedList;

import CategoriaDroga.CategoriaDroga;
import Droga.Droga;
import Droga.DrogaService;
import Proveedor.Proveedor;
import Proveedor.ProveedorService;
import StockDroga.StockDroga;
import StockDroga.StockDrogaService;

public class ActualizarInventarioController {
    public static LinkedList<ItemInventarioDTO> getItems(String userId){
        try {
            LinkedList<ItemInventarioDTO> result = new LinkedList<>();

            ProveedorService proveedorService = new ProveedorService();
            Proveedor proveedor = proveedorService.findByUsuarioId(userId);
            
            StockDrogaService stockDrogaService = new StockDrogaService();
            
            LinkedList<StockDroga> stockDrogaDelProveedor = new LinkedList<>();
            stockDrogaDelProveedor.addAll(stockDrogaService.findByProveedor(proveedor));

            for (StockDroga stockDroga : stockDrogaDelProveedor) {
                ItemInventarioDTO nuevoItem = new ItemInventarioDTO();
                Droga droga = stockDroga.getDroga();
                CategoriaDroga categoriaDroga = droga.getCategoriaDroga();
                
                nuevoItem.setComposicion(droga.getComposicion());
                nuevoItem.setDisponible(stockDroga.getDisponible());
                nuevoItem.setNombreCategoria(categoriaDroga.getNombre());
                nuevoItem.setAprobacion_pendiente(categoriaDroga.getAprobacion_pendiente());
                nuevoItem.setNombreDroga(droga.getNombre());
                nuevoItem.setPrecioUnitario(stockDroga.getPrecioUnitario());
                nuevoItem.setUnidad(droga.getUnidad());

                result.add(nuevoItem);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }

    public static ItemInventarioDTO getItemToUpdate(String stockDrogaId){
        try {
            StockDrogaService stockDrogaService = new StockDrogaService();
            StockDroga stockDroga = stockDrogaService.findById(stockDrogaId);

            ItemInventarioDTO result = new ItemInventarioDTO();
            Droga droga = stockDroga.getDroga();
            CategoriaDroga categoriaDroga = droga.getCategoriaDroga();
                
            result.setComposicion(droga.getComposicion());
            result.setDisponible(stockDroga.getDisponible());
            result.setNombreCategoria(categoriaDroga.getNombre());
            result.setAprobacion_pendiente(categoriaDroga.getAprobacion_pendiente());
            result.setNombreDroga(droga.getNombre());
            result.setPrecioUnitario(stockDroga.getPrecioUnitario());
            result.setUnidad(droga.getUnidad());
            result.setDrogaId(droga.getId().toString());

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void DeleteSelectedInventoryItems(String userId, String[] drogasIds){
        // Recibimos las ids de las drogas del stock del proveedor. Esos stocks son los que tenemos que borrar.
        try {
            StockDrogaService stockDrogaService = new StockDrogaService();
    
            ProveedorService proveedorService = new ProveedorService();
            Proveedor proveedor = proveedorService.findByUsuarioId(userId);
            DrogaService drogaService = new DrogaService();
    
            for (String drogaId : drogasIds) {
                Droga droga = drogaService.findById(drogaId);    
                
                StockDroga stock = stockDrogaService.findByProveedorAndDroga(proveedor, droga);
                stockDrogaService.delete(stock);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
