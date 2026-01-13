package ActionController.buscarDrogas;

import Droga.Droga;
import Proveedor.Proveedor;
import StockDroga.StockDroga;
import StockDroga.StockDrogaService;

import java.util.List;
import java.util.Map;

import java.util.HashMap;
import java.util.LinkedList;

public class DrogaDTO {
    private final String nombre;
    private final String formula;
    private final String categoria;
    private final int stockTotal;
    private final int cantProveedores;
    private final Map<Integer, ProveedorInfo> proveedores;

    public DrogaDTO(Droga droga) {
        StockDrogaService stockDrogaService = new StockDrogaService();
        LinkedList<StockDroga> stocksDroga;
        
        try {
            stocksDroga = stockDrogaService.findByDroga(droga);

            if(stocksDroga == null) throw new RuntimeException("No se encontró el stock");

            this.proveedores = new HashMap<>();
            this.nombre = droga.getNombre();
            this.formula = droga.getComposicion();
            this.categoria = droga.getCategoriaDroga().getNombre();
            this.stockTotal = calcularStockTotalDisponible(stocksDroga);
            this.cantProveedores = stocksDroga.size();

            for (StockDroga stockDroga : stocksDroga) {
                Proveedor proveedor = stockDroga.getProveedor();
                this.proveedores.put(proveedor.getId(), new ProveedorInfo(proveedor.getNombreFantasia(), stockDroga.getPrecioUnitario()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private int calcularStockTotalDisponible(List<StockDroga> stocks) {
        Integer cantidadStock = 0;

        for (StockDroga stockDroga : stocks) {
            cantidadStock += stockDroga.getDisponible();
        }

        return cantidadStock;
    }

    public String getNombre() { return nombre; }
    public String getFormula() { return formula; }
    public String getCategoria() { return categoria; }
    public int getStockTotal() { return stockTotal; }
    public double getCantidadProveedores() { return cantProveedores; }

    public Map<Integer, ProveedorInfo> getProveedores() {
        return proveedores;
    }

    public static class ProveedorInfo {
        private final String nombre;
        private final double precioUnitario;

        public ProveedorInfo(String nombre, double precioUnitario) {
            this.nombre = nombre;
            this.precioUnitario = precioUnitario;
        }

        public String getNombre() { return nombre; }
        public double getPrecioUnitario() { return precioUnitario; }
    }
}