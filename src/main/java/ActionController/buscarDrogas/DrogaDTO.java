package ActionController.buscarDrogas;

import Droga.Droga;
import Proveedor.Proveedor;
import StockDroga.StockDroga;
import StockDroga.StockDrogaService;

import java.util.List;
import java.util.LinkedList;

public class DrogaDTO {
    private final String nombre;
    private final String formula;
    private final String categoria;
    private final String unidad;
    private final int idDroga;
    private final int stockTotal;
    private final int cantidadProveedores;
    private final  LinkedList<ProveedorInfo> proveedores;

    public DrogaDTO(Droga droga) {
        StockDrogaService stockDrogaService = new StockDrogaService();
        LinkedList<StockDroga> stocksDroga;
        
        try {
            stocksDroga = stockDrogaService.findByDroga(droga);

            if(stocksDroga == null) throw new RuntimeException("No se encontró el stock");

            this.proveedores = new LinkedList<>();
            this.nombre = droga.getNombre();
            this.formula = droga.getComposicion();
            this.categoria = droga.getCategoriaDroga().getNombre();
            this.stockTotal = calcularStockTotalDisponible(stocksDroga);
            this.cantidadProveedores = stocksDroga.size();
            this.unidad = droga.getUnidad();
            this.idDroga = droga.getId();

            stocksDroga.sort((a, b) -> Double.compare( b.getPrecioUnitario(),a.getPrecioUnitario()));
            for (StockDroga stockDroga : stocksDroga) {
                Proveedor proveedor = stockDroga.getProveedor();

                if(proveedor == null ) throw new RuntimeException("No existe proveedor para el stock");

                this.proveedores.add(new ProveedorInfo(proveedor.getId(), proveedor.getNombreFantasia(), stockDroga.getPrecioUnitario()));

                // ESTO DA NULL.
                System.out.println("AAAAAAA"  + proveedor.getNombreFantasia());            
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
    public String getUnidad() { return unidad; }
    public int getIdDroga() { return idDroga; }
    public int getStockTotal() { return stockTotal; }
    public LinkedList<ProveedorInfo> getProveedores() { return proveedores; }
    public double getCantidadProveedores() { return cantidadProveedores; }

    public static class ProveedorInfo {
        private final Integer idProveedor;
        private final String nombre;
        private final double precioUnitario;

        public ProveedorInfo(Integer idProveedor, String nombre, double precioUnitario) {
            this.idProveedor = idProveedor;
            this.nombre = nombre;
            this.precioUnitario = precioUnitario;
        }

        public String getNombre() { return nombre; }
        public Integer geIdProveedor() { return idProveedor; }
        public double getPrecioUnitario() { return precioUnitario; }
    }
}