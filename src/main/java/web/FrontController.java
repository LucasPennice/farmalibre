package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import ActionController.buscarDrogas.BuscarDrogasController;
import ActionController.buscarDrogas.DrogaDTO;
import CategoriaDroga.CategoriaDroga;
import CategoriaDroga.CategoriaDrogaService;
import Droga.Droga;
import Droga.DrogaService;
import Proveedor.Proveedor;
import Proveedor.ProveedorService;
import StockDroga.StockDroga;
import StockDroga.StockDrogaService;
import Usuario.Usuario;
import Usuario.UsuarioService;
import db.DatabaseInitializer;

// TODO: Hacer vista onboarding comprador
// TODO: Hacer vista onboarding proveedor
// TODO: Cuando el usuario se loguea checkear si tiene onb completo y redirigir (a onv usuario o onb proveedor)
// TODO: Redirigir automaticamente a onb comprador si usuario log y no termino onb
// TODO: Redirigir automaticamente a onb proveedor si usuario log y es proveedor y no termino onb proveedor

public class FrontController extends HttpServlet {

    public FrontController() {
        super(); // preserves the default HttpServlet initialization behavior
        
        // TODO: place custom initialization logic here that should run at construction time
    }
    Logger log = Logger.getLogger(FrontController.class.getName());

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

        if (path.startsWith("/auth/do-register")) {
            doRegister(request, response, errores, drogas);
            return;
        }

        if (path.startsWith("/auth/do-login")) {
            doLogin(request, response, errores, drogas);
            return;
        }

        if (path.startsWith("/auth/do-logout")) {
            doLogout(request, response, errores, drogas);
            return;
        }

        if (path.startsWith("/do-filter")) {
            handleHomepage(request, response, drogas);
            return;
        }

        onboardingFilter(request, response, errores, drogas);

        if (path.equals("/") || path.equals("/index")) {
            handleHomepage(request, response, drogas);
        } else if (path.startsWith("/carrito")) {
            handleCarrito(request, response);
        } else if (path.startsWith("/auth/login")) {
            handleLogin(request, response);
        } else if (path.startsWith("/auth/register")) {
            handleRegister(request, response);
        } else if (path.startsWith("/onboarding_usuario")) {
            handleOnboardingUsuario(request, response);
        } else if (path.startsWith("/onboarding_proveedor")) {
            handleOnboardingProveedor(request, response);
        } else if (path.startsWith("/inventario")) {
            handleInventario(request, response);
        } else if (path.startsWith("/comprar-droga")) {
            handleDroga(request, response);
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

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Register");
        request.getRequestDispatcher("/WEB-INF/views/pages/register.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Login");
        request.getRequestDispatcher("/WEB-INF/views/pages/login.jsp").forward(request, response);
    }

    private void onboardingFilter(HttpServletRequest request, HttpServletResponse response, LinkedList<String> errores, LinkedList<Droga> drogas)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);

            if(session == null) return;

            if(session.getAttribute("usuario_id") == null) return;

            String userId = session.getAttribute("usuario_id").toString();
            
            UsuarioService usuarioService = new UsuarioService();
            Usuario usuario = usuarioService.findById(userId);

            if(!usuario.getOnboarding_completo()) {
                handleOnboardingUsuario(request, response);
                return;
            }

            ProveedorService proveedorService = new ProveedorService();
            Proveedor proveedor = proveedorService.findByUsuarioId(userId);

            if(proveedor != null && !proveedor.getOnboardingCompleto()) {
                handleOnboardingProveedor(request, response);
                return;
            }

        } catch (Exception e) {
            errores.add(e.getMessage()); 
            request.setAttribute("errores", errores);
            handleHomepage(request, response, drogas);
        } 
    }

    private void handleOnboardingUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Onboarding Usuario");
        request.getRequestDispatcher("/WEB-INF/views/pages/onboarding_usuario.jsp").forward(request, response);
    }

    private void handleOnboardingProveedor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Onboarding Proveedor");
        request.getRequestDispatcher("/WEB-INF/views/pages/onboarding_proveedor.jsp").forward(request, response);
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

    private void handleHomepage(HttpServletRequest request, HttpServletResponse response, LinkedList<Droga> drogas)
            throws ServletException, IOException {

        String searchQuery = request.getParameter("filter");
        String categoriaId = request.getParameter("categoriaId");

        LinkedList<DrogaDTO> drogaDTOs;

        // Filtrar por categoría
        if (categoriaId != null && !categoriaId.isEmpty()) {
            LinkedList<Droga> drogasFiltradas = new LinkedList<>();
            for (Droga droga : drogas) {
                if (droga.getCategoriaDroga().getId().toString().equals(categoriaId)) {
                    drogasFiltradas.add(droga);
                }
            }
            drogaDTOs = BuscarDrogasController.BuscarDrogas(drogasFiltradas, null);
        }
        // Filtrar por búsqueda
        else if (searchQuery != null && !searchQuery.isEmpty()) {
            drogaDTOs = BuscarDrogasController.BuscarDrogas(drogas, searchQuery);
        }
        // Sin filtros
        else {
            drogaDTOs = BuscarDrogasController.BuscarDrogas(drogas, null);
        }

        request.setAttribute("drogaDTOs", drogaDTOs);
        request.setAttribute("pageTitle", "Inicio");
        request.setAttribute("content", "/WEB-INF/views/pages/index.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response, LinkedList<String> errores,
            LinkedList<Droga> drogas)
            throws ServletException, IOException {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            errores.add("Verbo incorrecto para /auth/do-login");
            request.setAttribute("errores", errores);
            // Redirige a homepage
            handleHomepage(request, response, drogas);
            return;
        }

        String nombre = request.getParameter("nombre");
        String password = request.getParameter("password");

        try {
            Usuario usuarioAutenticado = UsuarioService.autenticar(nombre, password);

            HttpSession session = request.getSession(true);
            session.setAttribute("usuario_id", usuarioAutenticado.getId());

            return onboardingFilter(request, response);
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
        } finally {
            handleHomepage(request, response, drogas);
        }

    }

    private void doRegister(HttpServletRequest request, HttpServletResponse response, LinkedList<String> errores,
            LinkedList<Droga> drogas)
            throws ServletException, IOException {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            errores.add("Verbo incorrecto para /auth/register");
            request.setAttribute("errores", errores);
            // Redirige a homepage
            handleHomepage(request, response, drogas);
            return;
        }

        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UsuarioService.registrar(nombre, email, password);
            doLogin(request, response, errores, drogas);
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
            handleHomepage(request, response, drogas);
        }
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response, LinkedList<String> errores,
            LinkedList<Droga> drogas)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            session.invalidate();
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
        } finally {
            handleHomepage(request, response, drogas);
        }
    }
}