package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import jakarta.servlet.annotation.MultipartConfig;
import java.io.InputStream;

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
import Proveedor.TipoPersona;
import StockDroga.StockDroga;
import StockDroga.StockDrogaService;
import Usuario.Usuario;
import Usuario.UsuarioService;
import db.DatabaseInitializer;

// TODO: Hacer vista onboarding comprador
// TODO: Hacer vista onboarding proveedor
// TODO: Cuando el usuario se loguea checkear si tiene onb completo y redirigir (a onv usuario o onb proveedor) ✅
// TODO: Redirigir automaticamente a onb comprador si usuario log y no termino onb 
// TODO: Redirigir automaticamente a onb proveedor si usuario log y es proveedor y no termino onb proveedor

@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class FrontController extends HttpServlet {
    LinkedList<String> errores = new LinkedList<String>();
    LinkedList<CategoriaDroga> categorias = new LinkedList<CategoriaDroga>();
    LinkedList<Droga> drogas = new LinkedList<Droga>();
    LinkedList<Proveedor> proveedores = new LinkedList<Proveedor>();
    LinkedList<StockDroga> stockDrogas = new LinkedList<StockDroga>();

    public FrontController() {
        super();
        
        // Fetch de Cateogrias con error handling
        try {
            CategoriaDrogaService categoriaDrogaService;
            categoriaDrogaService = new CategoriaDrogaService();
            categorias.addAll(categoriaDrogaService.findAll());
            
        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        // Fetch de Drogas con error handling
        
        try {
            DrogaService drogaService;
            drogaService = new DrogaService();
            drogas.addAll(drogaService.findAll());

        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        // Fetch de Proveedores con error handling

        try {
            ProveedorService proveedorService;
            proveedorService = new ProveedorService();
            proveedores.addAll(proveedorService.findAll());
            
        } catch (Exception e) {
            errores.add(e.getMessage());
        }

        // Fetch de Stock Drogas con error handling

        try {
            StockDrogaService stockDrogaService;
            stockDrogaService = new StockDrogaService();
            stockDrogas.addAll(stockDrogaService.findAll());
            
        } catch (Exception e) {
            errores.add(e.getMessage());
        }
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

        if (path.startsWith("/usuario-foto")) {
            handleUsuarioFoto(request, response);
            return;
        }

        request.setAttribute("categorias", categorias);
        request.setAttribute("drogas", drogas);
        request.setAttribute("proveedores", proveedores);
        request.setAttribute("stockDrogas", stockDrogas);
        request.setAttribute("errores", errores);

        if (path.startsWith("/auth/do-register")) {
            doRegister(request, response);
            return;
        }

        if (path.startsWith("/auth/do-login")) {
            doLogin(request, response);
            return;
        }

        if (path.startsWith("/auth/do-logout")) {
            doLogout(request, response);
            return;
        }

        if (path.startsWith("/do-filter")) {
            handleHomepage(request, response);
            return;
        }
        
        if (path.startsWith("/do-complete-onboarding-proveedor")) {
            doCompleteOnboardingProveedor(request, response);
            return;
        }
        
        if (path.startsWith("/do-complete-onboarding-usuario")) {
            doCompleteOnboardingUsuario(request, response);
            return;
        }

        onboardingFilter(request, response);

        if (path.equals("/") || path.equals("/index")) {
            handleHomepage(request, response);
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

    private void onboardingFilter(HttpServletRequest request, HttpServletResponse response)
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
            handleHomepage(request, response);
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

    private void handleHomepage(HttpServletRequest request, HttpServletResponse response)
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
        
        HttpSession session = request.getSession(false);

        if(session != null && session.getAttribute("usuario_id") != null) {
            String usuarioId = session.getAttribute("usuario_id").toString();
            UsuarioService usuarioService = new UsuarioService();
            Usuario usuario = usuarioService.findById(usuarioId);

            request.setAttribute("usuario", usuario);
        }

        request.setAttribute("pageTitle", "Inicio");
        request.setAttribute("content", "/WEB-INF/views/pages/index.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            errores.add("Verbo incorrecto para /auth/do-login");
            request.setAttribute("errores", errores);
            // Redirige a homepage
            handleHomepage(request, response);
            return;
        }

        String nombre = request.getParameter("nombre");
        String password = request.getParameter("password");

        try {
            Usuario usuarioAutenticado = UsuarioService.autenticar(nombre, password);

            HttpSession session = request.getSession(true);
            session.setAttribute("usuario_id", usuarioAutenticado.getId());

            onboardingFilter(request, response);
            return;
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
        } finally {
            handleHomepage(request, response);
        }

    }

    private void doRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            errores.add("Verbo incorrecto para /auth/register");
            request.setAttribute("errores", errores);
            // Redirige a homepage
            handleHomepage(request, response);
            return;
        }

        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UsuarioService.registrar(nombre, email, password);
            doLogin(request, response);
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
            handleHomepage(request, response);
        }
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            session.invalidate();
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
        } finally {
            handleHomepage(request, response);
        }
    }

    private void doCompleteOnboardingUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            errores.add("Verbo incorrecto para /do-complete-onboarding-usuario");
            request.setAttribute("errores", errores);
            // Redirige a homepage
            handleHomepage(request, response);
            return;
        }

        HttpSession session = request.getSession(false);

        if(session == null) return;

        if(session.getAttribute("usuario_id") == null) return;

        String usuarioId = session.getAttribute("usuario_id").toString();

        String direccion = request.getParameter("direccion");

        byte[] fotoPerfilBytes = null;

        try {
            if (request.getPart("fotoPerfil") != null && request.getPart("fotoPerfil").getSize() > 0) {
                InputStream is = request.getPart("fotoPerfil").getInputStream();
                fotoPerfilBytes = is.readAllBytes();
            }
        } catch (Exception e) {
            errores.add("Error al procesar foto de perfil: " + e.getMessage());
        }

        byte[] fotoPerfil = fotoPerfilBytes;

        Boolean esProveedor = Boolean.parseBoolean(request.getParameter("esProveedor"));

        try {
            UsuarioService usuarioService = new UsuarioService();
        
            Usuario usuario = usuarioService.findById(usuarioId);
        
            usuario.setDireccion(direccion);
            usuario.setFoto_perfil(fotoPerfil);
            usuario.setOnboarding_completo(true);

            usuarioService.update(usuario);

            if(!esProveedor){
                handleHomepage(request, response);
                return;
            }

            Proveedor proveedor = new Proveedor();
            proveedor.setUsuarioId(usuario.getId());
            ProveedorService proveedorService = new ProveedorService();
            proveedorService.save(proveedor);

            handleOnboardingProveedor(request, response);
            return;
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
            handleHomepage(request, response);
        } 
    }

// PEDNIENTE! hookear estos dos do con 1. el front controller 2- los forms de las respectivas pgians

    private void doCompleteOnboardingProveedor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            errores.add("Verbo incorrecto para /do-complete-onboarding-proveedor");
            request.setAttribute("errores", errores);
            // Redirige a homepage
            handleHomepage(request, response);
            return;
        }

        String proveedorId = request.getParameter("proveedorId");
        String razonSocial = request.getParameter("razonSocial");
        String nombreFantasia = request.getParameter("nombreFantasia");
        String CUIT = request.getParameter("CUIT");
        String tipoPersona = request.getParameter("tipoPersona");
        // TODO: Añadir validador de cuit (ValidadorUtil.java)

        try {
            ProveedorService proveedorService = new ProveedorService();
            Proveedor proveedor = proveedorService.findById(proveedorId);
            
            proveedor.setRazonSocial(razonSocial);
            proveedor.setNombreFantasia(nombreFantasia);
            proveedor.setCuit(CUIT);
            proveedor.setTipoPersona(tipoPersona.toLowerCase() == "fisica"? TipoPersona.FISICA: TipoPersona.JURIDICA);
            proveedor.setOnboardingCompleto(true);
            proveedorService.update(proveedor);

            return;
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
        } finally{
            handleHomepage(request, response);
        }
    }
    private void handleUsuarioFoto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        if (id == null) {
            response.sendError(400, "Falta parametro id");
            return;
        }

        UsuarioService usuarioService = new UsuarioService();
        Usuario usuario = usuarioService.findById(id);

        if (usuario == null || usuario.getFoto_perfil() == null) {
            response.sendError(404, "Foto no encontrada");
            return;
        }

        byte[] foto = usuario.getFoto_perfil();

        // Intentar detectar tipo; por defecto usar JPEG
        String contentType = request.getServletContext().getMimeType("foto.jpg");
        if (contentType == null) {
            contentType = "image/jpeg";
        }

        response.setContentType(contentType);
        response.setContentLength(foto.length);

        response.getOutputStream().write(foto);
        response.getOutputStream().flush();
    }
}