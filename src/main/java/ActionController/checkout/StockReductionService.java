package ActionController.checkout;

import java.util.LinkedList;
import java.util.logging.Logger;

import Carrito.ItemCarrito;
import StockDroga.StockDroga;
import StockDroga.StockDrogaService;

/**
 * Servicio para manejar la reducción de stock después de confirmar un pago
 */
public class StockReductionService {
    private static final Logger log = Logger.getLogger(StockReductionService.class.getName());
    
    /**
     * Reduce el stock de los productos en el carrito
     * Se ejecuta cuando el pago es confirmado por Mercado Pago
     * 
     * @param items Items del carrito a reducir
     * @throws Exception si hay error al reducir el stock
     */
    public static void reducirStock(LinkedList<ItemCarrito> items) {
        try {
            if (items == null || items.isEmpty()) {
                throw new IllegalArgumentException("No hay items para reducir stock");
            }
            
            log.info("Iniciando reducción de stock para " + items.size() + " items");
            
            StockDrogaService stockService = new StockDrogaService();
            
            for (ItemCarrito item : items) {
                try {
                    reducirStockItem(item, stockService);
                } catch (Exception e) {
                    log.warning("Error al reducir stock del item: " + item.getDroga().getNombre() + " - " + e.getMessage());
                    // Continuar con el siguiente item, pero registrar el error
                }
            }
            
            log.info("✓ Reducción de stock completada");
            
        } catch (Exception e) {
            log.severe("Error al reducir stock: " + e.getMessage());
            throw new RuntimeException("Error al reducir stock: " + e.getMessage());
        }
    }
    
    /**
     * Reduce el stock de un item individual
     */
    private static void reducirStockItem(ItemCarrito item, StockDrogaService stockService) {
        try {
            // Obtener todos los stocks de esta droga
            LinkedList<StockDroga> todosLosStocks = (LinkedList<StockDroga>) stockService.findAll();
            
            // Buscar el stock específico del proveedor
            StockDroga stockAReducir = null;
            for (StockDroga stock : todosLosStocks) {
                if (stock.getDroga().getId().equals(item.getDroga().getId()) &&
                    stock.getProveedor().getId().equals(item.getProveedor().getId())) {
                    stockAReducir = stock;
                    break;
                }
            }
            
            if (stockAReducir == null) {
                throw new RuntimeException("No se encontró stock para: " + item.getDroga().getNombre() 
                    + " del proveedor " + item.getProveedor().getNombreFantasia());
            }
            
            // Validar que hay suficiente stock
            if (stockAReducir.getDisponible() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + item.getDroga().getNombre() 
                    + ". Disponible: " + stockAReducir.getDisponible() + ", Solicitado: " + item.getCantidad());
            }
            
            // Reducir el stock
            Integer nuevoStock = stockAReducir.getDisponible() - item.getCantidad();
            stockAReducir.setDisponible(nuevoStock);
            
            // Actualizar en la BD
            stockService.update(stockAReducir);
            
            log.info("Stock reducido - Droga: " + item.getDroga().getNombre() 
                + ", Proveedor: " + item.getProveedor().getNombreFantasia()
                + ", Cantidad: " + item.getCantidad()
                + ", Stock anterior: " + (nuevoStock + item.getCantidad())
                + ", Stock nuevo: " + nuevoStock);
            
        } catch (Exception e) {
            log.severe("Error al reducir stock individual: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * Restaura el stock si un pago fue rechazado (rollback)
     * Actualmente no se utiliza pero está disponible para funcionalidad futura
     */
    public static void restaurarStock(LinkedList<ItemCarrito> items) {
        try {
            if (items == null || items.isEmpty()) {
                return;
            }
            
            log.info("Restaurando stock para " + items.size() + " items");
            
            StockDrogaService stockService = new StockDrogaService();
            
            for (ItemCarrito item : items) {
                try {
                    LinkedList<StockDroga> todosLosStocks = (LinkedList<StockDroga>) stockService.findAll();
                    
                    for (StockDroga stock : todosLosStocks) {
                        if (stock.getDroga().getId().equals(item.getDroga().getId()) &&
                            stock.getProveedor().getId().equals(item.getProveedor().getId())) {
                            
                            Integer nuevoStock = stock.getDisponible() + item.getCantidad();
                            stock.setDisponible(nuevoStock);
                            stockService.update(stock);
                            
                            log.info("Stock restaurado - Droga: " + item.getDroga().getNombre() 
                                + ", Cantidad: " + item.getCantidad());
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.warning("Error al restaurar stock: " + e.getMessage());
                }
            }
            
            log.info("✓ Restauración de stock completada");
            
        } catch (Exception e) {
            log.severe("Error al restaurar stock: " + e.getMessage());
        }
    }
}
