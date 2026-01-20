package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import jakarta.servlet.annotation.MultipartConfig;
import java.io.InputStream;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import ActionController.actualizarInventario.ActualizarInventarioController;
import ActionController.actualizarInventario.ItemInventarioDTO;
import ActionController.administrarTiposDrogaYCategorias.AdministrarTiposDrogaYCategorias;
import ActionController.buscarDrogas.BuscarDrogasController;
import ActionController.buscarDrogas.DrogaDTO;
import AprobarCategorias.AprobarCategoriasController;
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

// TODO: Habría que refactorizar el codigo cosa de que funciones como doLogin, doRegister y esas solo se preocupen por la logica que les corresponde y extraerlas a un controlador que tenga sentido
// Entonces en este archivo quedaria algo como. La funcion (doLogin) no deberia conocer handleHomePage ni nada de ruteo en general

// if(ruta){
//     try{
//         Controlador.doLogin()
//     }catch{
//         errores.add(etc)
//         handleLogin
//     }
// }

@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class FrontController extends HttpServlet {
    LinkedList<String> errores = new LinkedList<String>();
    LinkedList<CategoriaDroga> categorias = new LinkedList<CategoriaDroga>();
    LinkedList<CategoriaDroga> categoriasAprobadas = new LinkedList<CategoriaDroga>();
    LinkedList<Droga> drogas = new LinkedList<Droga>();
    LinkedList<Proveedor> proveedores = new LinkedList<Proveedor>();
    LinkedList<StockDroga> stockDrogas = new LinkedList<StockDroga>();

    public FrontController() {
        super();

       fetchNewState();
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

        errores.clear();

        setUserAttribute(request);

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

        updateState(request);

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
        
        if (path.startsWith("/do-actualizar-inventario")) {
            try {
                String userId = getUserIdFromSession(request);
                String drogaId = request.getParameter("drogaId");
                Integer disponible = Integer.parseInt(request.getParameter("disponible")); 
                Double precioUnitario = Double.parseDouble(request.getParameter("precioUnitario"));

                ActualizarInventarioController.ActualizarItemInventario(userId, drogaId, disponible, precioUnitario);

                fetchNewState();
                updateState(request);

                handleInventario(request, response);
                return;
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleHomepage(request, response);
                return;
            }
        }
        
        if (path.startsWith("/do-add-item-to-inventario")) {
            try {
                String userId = getUserIdFromSession(request);
                String drogaId = request.getParameter("drogaId");
                Integer disponible = Integer.parseInt(request.getParameter("disponible")); 
                Double precioUnitario = Double.parseDouble(request.getParameter("precioUnitario"));

                ActualizarInventarioController.AddItemInventario(userId, drogaId, disponible, precioUnitario);
                
                fetchNewState();
                updateState(request);

                handleInventario(request, response);
                return;
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleHomepage(request, response);
                return;
            }
        }

        if (path.startsWith("/do-cargar-nuevo-tipo-droga")) {
            try {
                String userId = getUserIdFromSession(request);
                String nombreDroga = request.getParameter("nombreDroga");
                String composicion = request.getParameter("composicion");
                String unidad = request.getParameter("unidad");
                String nombreCategoria = request.getParameter("nombreCategoria");

                if(nombreCategoria.isEmpty() || nombreCategoria.isBlank()){
                    throw new RuntimeException("La categoría no puede estar vacia");
                }

                AdministrarTiposDrogaYCategorias.CargarNuevoTipoDroga(userId,nombreDroga,composicion,unidad,nombreCategoria);

                fetchNewState();
                updateState(request);

                handleAddItemAInventario(request, response);
                return;
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleHomepage(request, response);
                return;
            }
        }
        
        if (path.startsWith("/do-delete-selected-items")) {
            try {
                String[] seleccionados = request.getParameterValues("selectedItems");
                
                if(seleccionados == null){
                    throw new RuntimeException("No selecciono ningun item para borrar");
                }

                String userId = getUserIdFromSession(request);
                ActualizarInventarioController.DeleteSelectedInventoryItems(userId, seleccionados);
                handleInventario(request, response);
                return;
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleHomepage(request, response);
                return;
            }
        }
        
        if (path.startsWith("/do-aprobar-categoria")) {
            try {
                String categoriaId = request.getParameter("categoriaId");
                
                AprobarCategoriasController.AprobarCategoria(categoriaId);

                fetchNewState();
                updateState(request);

                handleAprobarCategorias(request, response);
                return;
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleHomepage(request, response);
                return;
            }
        }
     
        if (path.startsWith("/do-rechazar-categoria")) {
            try {
                String categoriaId = request.getParameter("categoriaId");
                
                AprobarCategoriasController.RechazarCategoria(categoriaId);

                fetchNewState();
                updateState(request);

                handleAprobarCategorias(request, response);
                return;
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleHomepage(request, response);
                return;
            }
        }

        if (path.startsWith("/do-editar-categoria")) {
            try {
                String categoriaId = request.getParameter("categoriaId");
                String nuevoNombre = request.getParameter("nombreCategoria");
                
                AprobarCategoriasController.EditarCategoria(categoriaId, nuevoNombre);

                fetchNewState();
                updateState(request);

                handleAprobarCategorias(request, response);
                return;
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleHomepage(request, response);
                return;
            }
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
        } else if (path.startsWith("/actualizar-item")) {
            handleActualizarItem(request, response);
        } else if (path.startsWith("/add-item-a-inventario")) {
            handleAddItemAInventario(request, response);
        } else if (path.startsWith("/editar-categoria")) {
            handleEditarCategoria(request, response);
        } else if (path.startsWith("/cargar-nuevo-tipo-droga")) {
            handleCargarNuevoTipoDroga(request, response);
        } else if (path.startsWith("/administrar-categorias")) {
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
    
    private void handleCargarNuevoTipoDroga(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Cargar Nuevo Tipo Droga");
        request.setAttribute("content", "/WEB-INF/views/pages/cargar-nuevo-tipo-droga.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }
    
    private void handleActualizarItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String itemIdAActualizar = request.getParameter("itemId");
        ItemInventarioDTO itemToUpdate = ActualizarInventarioController.getItemToUpdate(itemIdAActualizar);
        
        request.setAttribute("itemToUpdate", itemToUpdate);

        request.setAttribute("pageTitle", "Actualizar Item");
        request.setAttribute("content", "/WEB-INF/views/pages/actualizar-item.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }
    
    private void handleAddItemAInventario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "Añadir item a inventario");
        request.setAttribute("content", "/WEB-INF/views/pages/add-item-a-inventario.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    private void handleDroga(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                
        String drogaId = request.getParameter("drogaId");
        DrogaService drogaService = new DrogaService();
        Droga droga = drogaService.findById(drogaId);
        DrogaDTO drogaAComprar = BuscarDrogasController.buscarDroga(droga);

        Integer cantidadStockDroga = 0;

        ProveedorService proveedorService = new ProveedorService();
        for (StockDroga stock : stockDrogas) {
            if (stock.getDroga().getId().equals(Integer.parseInt(drogaId))) {
                stock.setDroga(droga); 
                stock.setProveedor(proveedorService.findById(String.valueOf(stock.getProveedor().getId())));
                cantidadStockDroga += stock.getDisponible();
            }
        }
        
        request.setAttribute("droga", drogaAComprar);
        request.setAttribute("stockDrogas", stockDrogas);
        request.setAttribute("cantidadStockDroga", cantidadStockDroga);
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

        LinkedList<ItemInventarioDTO> inventoryItems = new LinkedList<>();
        try {
            inventoryItems.addAll(ActualizarInventarioController.getItems(getUserIdFromSession(request)));
            request.setAttribute("inventoryItems", inventoryItems);        
        } catch (Exception e) {
            errores.add(e.getMessage());
            handleHomepage(request, response);
            return;
        }

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
        request.setAttribute("content", "/WEB-INF/views/pages/administrar-categorias.jsp");
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

        if (searchQuery != null && categoriaId != null) {
            doFilter(request, response);
            return;
        }

        // Filtrar solo por categoría
        if (categoriaId != null && !categoriaId.isEmpty()) {
            LinkedList<Droga> drogasFiltradas = new LinkedList<>();
            for (Droga droga : drogas) {
                if (droga.getCategoriaDroga().getId().toString().equals(categoriaId)) {
                    drogasFiltradas.add(droga);
                }
            }
            drogaDTOs = BuscarDrogasController.BuscarDrogas(drogasFiltradas, null);
        }
        // Filtrar solo por búsqueda
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

    
    private void handleEditarCategoria(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String categoriaId = request.getParameter("categoriaId");
        String nombreCategoria = request.getParameter("nombreCategoria");
        request.setAttribute("categoriaId", categoriaId);
        request.setAttribute("nombreCategoria", nombreCategoria);
        
        request.setAttribute("pageTitle", "Editar Categoria");
        request.setAttribute("content", "/WEB-INF/views/pages/editar-categoria.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
    }

    

    private void onboardingFilter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userId = getUserIdFromSession(request);

            if(userId == null) return;

            UsuarioService usuarioService = new UsuarioService();
            Usuario usuario = usuarioService.findById(userId);

            if (!usuario.getOnboarding_completo()) {
                handleOnboardingUsuario(request, response);
                return;
            }

        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
            handleHomepage(request, response);
            return;
        }
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("filter");
        String categoriaId = request.getParameter("categoriaId");

        LinkedList<DrogaDTO> drogaDTOs = null;

        // Filtrar por búsqueda Y categoría
        if (categoriaId != null && !categoriaId.isEmpty() && searchQuery != null && !searchQuery.isEmpty()) {
            // Primero buscar por texto
            LinkedList<DrogaDTO> drogaDTOsTemporales = BuscarDrogasController.BuscarDrogas(drogas, searchQuery);

            // Luego filtrar por categoría
            drogaDTOs = new LinkedList<>();
            for (DrogaDTO drogaDto : drogaDTOsTemporales) {
                // Buscar la droga original para verificar su categoría
                for (Droga droga : drogas) {
                    if (droga.getId().equals(drogaDto.getIdDroga()) &&
                            droga.getCategoriaDroga().getId().toString().equals(categoriaId)) {
                        drogaDTOs.add(drogaDto);
                        break;
                    }
                }
            }
        }

        request.setAttribute("drogaDTOs", drogaDTOs);
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
            setUserAttribute(request);

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
            return;
        }
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession(false);
            session.invalidate();
            setUserAttribute(request);
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

        String usuarioId = getUserIdFromSession(request);

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

            if (!esProveedor) {
                handleHomepage(request, response);
                return;
            }

            handleOnboardingProveedor(request, response);
            return;
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
            handleHomepage(request, response);
            return;
        }
    }

    private void doCompleteOnboardingProveedor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            errores.add("Verbo incorrecto para /do-complete-onboarding-proveedor");
            request.setAttribute("errores", errores);
            // Redirige a homepage
            handleHomepage(request, response);
            return;
        }

        String userId = getUserIdFromSession(request);
        String razonSocial = request.getParameter("razonSocial");
        String nombreFantasia = request.getParameter("nombreFantasia");
        String CUIT = request.getParameter("CUIT");
        String tipoPersona = request.getParameter("tipoPersona");

        try {
            ProveedorService proveedorService = new ProveedorService();
            Proveedor proveedor = new Proveedor();

            proveedor.setUsuarioId(Integer.valueOf(userId));
            proveedor.setRazonSocial(razonSocial);
            proveedor.setNombreFantasia(nombreFantasia);
            proveedor.setCuit(CUIT);
            proveedor.setTipoPersona(tipoPersona.toLowerCase() == "fisica" ? TipoPersona.FISICA : TipoPersona.JURIDICA);
            proveedor.setOnboardingCompleto(true);
            proveedorService.save(proveedor);

            setUserAttribute(request);

            return;
        } catch (Exception e) {
            errores.add(e.getMessage());
            request.setAttribute("errores", errores);
        } finally {
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

    private String getUserIdFromSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if (session == null){
            return null;
        }
                
        if (session.getAttribute("usuario_id") == null){
            return null;
        }

        String userId = session.getAttribute("usuario_id").toString();

        return userId;
    }

    private void setUserAttribute(HttpServletRequest request) {
        try {
            String usuarioId = getUserIdFromSession(request);

            if (usuarioId == null) {
                request.setAttribute("usuario", null);
                return;
            }
            
            UsuarioService usuarioService = new UsuarioService();
            Usuario usuario = usuarioService.findById(usuarioId);
            usuario.recalcularFlags();
            request.setAttribute("usuario", usuario);
        } catch (Exception e) {
            errores.add(e.getMessage());
        }
    }

    private void fetchNewState(){
        // IMPORTANT: This function does not update the request attributes. So even after it runs the changes might not be reflected in the UI
        // for that run the updateState function as well

        errores.clear();
        drogas.clear();
        proveedores.clear();
        stockDrogas.clear();
        stockDrogas.clear();
        categorias.clear();
        categoriasAprobadas.clear();

        try {
            CategoriaDrogaService categoriaDrogaService;
            categoriaDrogaService = new CategoriaDrogaService();
            categorias.addAll(categoriaDrogaService.findAll());
        } catch (Exception e) {
            errores.add(e.getMessage());
        }
        
        try {
            categoriasAprobadas.addAll(AprobarCategoriasController.GetCategoriasAprobadas());
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

    private void updateState(HttpServletRequest request){
        request.setAttribute("categorias", categorias);
        request.setAttribute("drogas", drogas);
        request.setAttribute("stockDrogas", stockDrogas);
        request.setAttribute("proveedores", proveedores);
        request.setAttribute("errores", errores);
        request.setAttribute("categoriasAprobadas", categoriasAprobadas);

        Integer cantidadCategoriasPendientes = 0;

        for (CategoriaDroga categoriaDroga : categorias) {
            if(categoriaDroga.getAprobacion_pendiente()){
                cantidadCategoriasPendientes++;
            }
        }

        request.setAttribute("cantidadCategoriasPendientes", cantidadCategoriasPendientes);
    }
}