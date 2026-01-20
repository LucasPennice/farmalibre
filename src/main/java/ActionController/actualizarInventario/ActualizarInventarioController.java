package ActionController.actualizarInventario;

import java.util.LinkedList;

import CategoriaDroga.CategoriaDroga;
import CategoriaDroga.CategoriaDrogaService;
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
            CategoriaDrogaService categoriaDrogaService = new CategoriaDrogaService();
            
            LinkedList<StockDroga> stockDrogaDelProveedor = new LinkedList<>();
            stockDrogaDelProveedor.addAll(stockDrogaService.findByProveedor(proveedor));

            DrogaService drogaService = new DrogaService();
            
            for (StockDroga stockDroga : stockDrogaDelProveedor) {
                ItemInventarioDTO nuevoItem = new ItemInventarioDTO();
                Integer drogaId = stockDroga.getDroga().getId();
                Droga droga = drogaService.findById(drogaId.toString());

                CategoriaDroga categoriaDroga = categoriaDrogaService.findById(droga.getCategoriaDroga().getId().toString());

                nuevoItem.setComposicion(droga.getComposicion());
                nuevoItem.setDisponible(stockDroga.getDisponible());
                nuevoItem.setNombreCategoria(categoriaDroga.getNombre());
                nuevoItem.setAprobacion_pendiente(categoriaDroga.getAprobacion_pendiente());
                nuevoItem.setNombreDroga(droga.getNombre());
                nuevoItem.setPrecioUnitario(stockDroga.getPrecioUnitario());
                nuevoItem.setUnidad(droga.getUnidad());
                nuevoItem.setStockDrogaId(stockDroga.getId().toString());

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

    public static void DeleteSelectedInventoryItems(String userId, String[] stockDrogaId){
        // Recibimos las ids de las drogas del stock del proveedor. Esos stocks son los que tenemos que borrar.
        try {
            StockDrogaService stockDrogaService = new StockDrogaService();
    
            for (String item : stockDrogaId) {
                StockDroga stock = stockDrogaService.findById(item);
                stockDrogaService.delete(stock);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public static void ActualizarItemInventario(String usuarioId, String drogaId, Integer disponible, Double precioUnitario){
        try {
            ProveedorService proveedorService = new ProveedorService();
            Proveedor proveedor = proveedorService.findByUsuarioId(usuarioId);

            DrogaService drogaService = new DrogaService();
            Droga droga = drogaService.findById(drogaId);

            StockDrogaService stockDrogaService = new StockDrogaService();
            StockDroga stockDroga = stockDrogaService.findByProveedorAndDroga(proveedor, droga);

            stockDroga.setDisponible(disponible);
            stockDroga.setPrecioUnitario(precioUnitario);

            stockDrogaService.update(stockDroga);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void AddItemInventario(String usuarioId, String drogaId, Integer disponible, Double precioUnitario){
        try {
            ProveedorService proveedorService = new ProveedorService();
            Proveedor proveedor = proveedorService.findByUsuarioId(usuarioId);
            if(proveedor == null) throw new RuntimeException("Proveedor es null");

            DrogaService drogaService = new DrogaService();
            Droga droga = drogaService.findById(drogaId);
            if(droga == null) throw new RuntimeException("Droga es null");

            StockDrogaService stockDrogaService = new StockDrogaService();
            
            StockDroga stockDroga = new StockDroga();
            stockDroga.setDisponible(disponible);
            stockDroga.setPrecioUnitario(precioUnitario);
            stockDroga.setDroga(droga);
            stockDroga.setProveedor(proveedor);
            
            stockDrogaService.save(stockDroga);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
