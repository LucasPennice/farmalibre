package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.LinkedList;

import CategoriaDroga.CategoriaDroga;
import CategoriaDroga.CategoriaDrogaService;
import Droga.Droga;
import Droga.DrogaService;
import Proveedor.Proveedor;
import Proveedor.ProveedorService;
import StockDroga.StockDroga;
import StockDroga.StockDrogaService;
import db.DatabaseInitializer;

public class FrontController extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            DatabaseInitializer.init();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar la base de datos", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (path.startsWith("/assets/")) {
            // Delegate static resources to the container's default servlet
            getServletContext()
                .getNamedDispatcher("default")
                .forward(request, response);
            return;
        }

    LinkedList<String> errores = new LinkedList<String>();

    // Fetch de CategoriaDroga con error handling

    CategoriaDrogaService categoriaDrogaService;
    LinkedList<CategoriaDroga> categorias = new LinkedList<CategoriaDroga>();
    
    try {
        categoriaDrogaService = new CategoriaDrogaService();
        categorias.addAll(categoriaDrogaService.findAll());
        request.setAttribute("categorias", categorias);
    } catch (Exception e) {
        errores.add(e.getMessage());
    }

    // Fetch de Drogas con error handling
    
    DrogaService drogaService;
    LinkedList<Droga> drogas = new LinkedList<Droga>();
    
    try {
        drogaService = new DrogaService();
        drogas.addAll(drogaService.findAll());
        request.setAttribute("drogas", drogas);
    } catch (Exception e) {
        errores.add(e.getMessage());
    }

    // Fetch de Proveedores con error handling
    
    ProveedorService proveedorService;
    LinkedList<Proveedor> proveedores = new LinkedList<Proveedor>();
    
    try {
        proveedorService = new ProveedorService();
        proveedores.addAll(proveedorService.findAll());
        request.setAttribute("proveedores", proveedores);
    } catch (Exception e) {
        errores.add(e.getMessage());
    }

    // Fetch de Stock Drogas con error handling
    
    StockDrogaService stockDrogaService;
    LinkedList<StockDroga> stockDrogas = new LinkedList<StockDroga>();
    
    try {
        stockDrogaService = new StockDrogaService();
        stockDrogas.addAll(stockDrogaService.findAll());
        request.setAttribute("stockDrogas", stockDrogas);
    } catch (Exception e) {
        errores.add(e.getMessage());
    }

    request.setAttribute("errores", errores);

        if (path.equals("/") || path.equals("/index")) {
            handleHomepage(request, response);
        } else if (path.startsWith("/carrito")) {
            handleCarrito(request, response);
        } else if (path.startsWith("/auth")) {
            handleAuth(request, response);
        } else if (path.startsWith("/onboarding")) {
            handleOnboarding(request, response);
        } else if (path.startsWith("/inventario")) {
            handleInventario(request, response);
        } else if (path.startsWith("/comprar-droga")) {
            handleDroga(request, response);
        } else if (path.startsWith("/usuario")) {
            handleUsuario(request, response);
        } else if (path.startsWith("/perfil")) {
            handlePerfil(request, response);
        } else if (path.startsWith("/aprobar-categorias")) {
            handleAprobarCategorias(request, response);
        } else {
            handleError(request, response);
        }
    }

    private void handleCarrito(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Carrito");
        request.setAttribute("content", "/WEB-INF/views/pages/carrito.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void handleDroga(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Compra de Drogas");
        request.setAttribute("content", "/WEB-INF/views/pages/droga.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void handleUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Usuarios");
        request.setAttribute("content", "/WEB-INF/views/pages/usuarios.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void handleAuth(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Autenticación");
        request.setAttribute("content", "/WEB-INF/views/pages/auth.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void handleOnboarding(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Onboarding");
        request.setAttribute("content", "/WEB-INF/views/pages/onboarding.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void handleInventario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Inventario");
        request.setAttribute("content", "/WEB-INF/views/pages/inventario.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void handlePerfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Perfil");
        request.setAttribute("content", "/WEB-INF/views/pages/perfil.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void handleAprobarCategorias(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        request.setAttribute("pageTitle", "Aprobar Categorías");
        request.setAttribute("content", "/WEB-INF/views/pages/aprobarCategorias.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "404");
        request.getRequestDispatcher("/404.jsp").forward(request, response);
    }
    
    
    private void handleHomepage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Inicio");
        request.setAttribute("content", "/WEB-INF/views/pages/index.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }
}