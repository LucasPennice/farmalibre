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
import ActionController.actualizarPerfil.ActualizarPerfilController;
import ActionController.actualizarPerfil.PerfilInfoDTO;
import ActionController.administrarTiposDrogaYCategorias.AdministrarTiposDrogaYCategorias;
import ActionController.auth.AuthController;
import ActionController.checkout.CheckoutController;
import ActionController.checkout.CheckoutItemDTO;
import ActionController.checkout.CheckoutRequestDTO;
import ActionController.checkout.CheckoutResponseDTO;
import ActionController.checkout.StockReductionService;
import ActionController.auth.LoginRequestDTO;
import ActionController.auth.LoginResponseDTO;
import ActionController.auth.RegisterRequestDTO;
import ActionController.auth.RegisterResponseDTO;
import ActionController.buscarDrogas.BuscarDrogasController;
import ActionController.buscarDrogas.DrogaDTO;
import AprobarCategorias.AprobarCategoriasController;
import Carrito.Carrito;
import Carrito.CarritoService;
import Carrito.ItemCarrito;
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

// NOTA: La refactorización se ha completado. Las funciones de autenticación (doLogin, doRegister, doLogout)
// han sido extraídas a ActionController.auth.AuthController. Este archivo ahora solo se encarga del ruteo
// y manejo de respuestas HTTP, siguiendo el patrón MVC.

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
            try {
                // Extraer parámetros del request
                String nombre = request.getParameter("nombre");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                
                // Crear DTO y llamar al controlador
                RegisterRequestDTO registerRequest = new RegisterRequestDTO(nombre, email, password);
                
                @SuppressWarnings("unused")
                RegisterResponseDTO registerResponse = AuthController.doRegister(registerRequest);
                
                // Auto-login después del registro
                LoginRequestDTO loginRequest = new LoginRequestDTO(nombre, password);
                LoginResponseDTO loginResponse = AuthController.doLogin(loginRequest);
                
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario_id", loginResponse.getUsuario().getId());
                setUserAttribute(request);
                
                // Redirigir al homepage después del registro exitoso
                handleHomepage(request, response);
                return;
                
            } catch (Exception e) {
                errores.add(e.getMessage());
                request.setAttribute("errores", errores);
                handleHomepage(request, response);
                return;
            }
        }

        if (path.startsWith("/auth/do-login")) {
            try {
                // Extraer parámetros del request
                String nombre = request.getParameter("nombre");
                String password = request.getParameter("password");
                
                // Crear DTO y llamar al controlador
                LoginRequestDTO loginRequest = new LoginRequestDTO(nombre, password);
                LoginResponseDTO loginResponse = AuthController.doLogin(loginRequest);
                
                // Manejar sesión
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario_id", loginResponse.getUsuario().getId());
                setUserAttribute(request);
                
                // Redirigir al homepage después del login exitoso
                handleHomepage(request, response);
                return;
                
            } catch (Exception e) {
                errores.add(e.getMessage());
                request.setAttribute("errores", errores);
                handleLogin(request, response);
                return;
            }
        }

        if (path.startsWith("/auth/do-logout")) {
            try {
                AuthController.doLogout();
                
                HttpSession session = request.getSession(false);
                session.invalidate();
                setUserAttribute(request);
                
            } catch (Exception e) {
                errores.add(e.getMessage());
                request.setAttribute("errores", errores);
            } finally {
                handleHomepage(request, response);
            }
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

        if (path.startsWith("/do-actualizar-perfil")) {
            try {
                String usuarioId = getUserIdFromSession(request);
                String nombreDelResponsable = request.getParameter("nombreDelResponsable");
                String emailDeContacto = request.getParameter("emailDeContacto");
                String direccionDelResponsable = request.getParameter("direccionDelResponsable");
                Part fotoPerfil = request.getPart("fotoPerfil");
                
                UsuarioService usuarioService = new UsuarioService();
                Usuario usuario = usuarioService.findById(usuarioId);
                usuario.recalcularFlags();

                Boolean esProveedor = usuario.getEsProveedor();

                String razonSocial = request.getParameter("razonSocial");
                String nombreFantasia = request.getParameter("nombreFantasia");
                String CUIT = request.getParameter("CUIT");
                TipoPersona tipoPersona = request.getParameter("tipoPersona") == TipoPersona.FISICA.toString() ? TipoPersona.FISICA : TipoPersona.JURIDICA;

                ActualizarPerfilController.Actualizar(usuarioId, nombreDelResponsable, emailDeContacto,direccionDelResponsable, fotoPerfil, esProveedor, razonSocial, nombreFantasia, CUIT, tipoPersona);

                fetchNewState();
                updateState(request);

                handleHomepage(request, response);
                return;
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleHomepage(request, response);
                return;
            }
        }

        if (path.startsWith("/do-agregar-carrito")) {
            try {
                // Inicializo servicios
                CarritoService carritoService = new CarritoService();
                DrogaService drogaService = new DrogaService();

                
                String drogaId = request.getParameter("drogaId");
                String distribucionJSON = request.getParameter("distribucion");
                String action = request.getParameter("accion");

                // Busco droga
                Droga droga = drogaService.findById(drogaId);

                if (carritoService.existeDrogaEnCarrito(request.getSession(), droga.getId())) {
                    if ("agregar".equals(action)) {
                        errores.add("Ya agrego esta droga al carrito, si desea modificar la cantidad que pidio, eliminela del carrito y vuelva aqui");
                    } else if ("comprar".equals(action)) {
                        handleCarrito(request, response);
                        return;
                    }
                }

                // Parseo distribucion: {"0":20,"1":30} -> 0:20,1:30
                String jsonLimpio = distribucionJSON.replace("{", "").replace("}", "").replace("\"", "");
                String[] pares = jsonLimpio.split(",");

                // pares = ["0:20", "1:30"]
                // Lo primero es la pos del proveedor y lo segundo la cantidad de droga elegida por proveedor

                // Filtro el stock de droga 

                LinkedList<StockDroga> stocksDeEstaDroga = new LinkedList<>();
                for (StockDroga stock : stockDrogas) {
                    if (stock.getDroga().getId().equals(Integer.parseInt(drogaId))) {
                        stocksDeEstaDroga.add(stock);
                    }
                }

                for (String par : pares) {
                    if (par.trim().isEmpty()) continue;
                    
                    String[] keyValue = par.split(":");
                    int indiceProveedor = Integer.parseInt(keyValue[0].trim());
                    int cantidadProveedor = Integer.parseInt(keyValue[1].trim());
                    
                    // Obtener el stock del proveedor usando el índice
                    StockDroga stockProveedor = stocksDeEstaDroga.get(indiceProveedor);
                    
                    // Crear ItemCarrito
                    ItemCarrito item = new ItemCarrito();
                    item.setDroga(droga);
                    item.setCantidad(cantidadProveedor);
                    item.setProveedor(stockProveedor.getProveedor());
                    item.setPrecioUnitario(stockProveedor.getPrecioUnitario());
                    
                    // Agregar al carrito
                    int precioTotal = (int)(stockProveedor.getPrecioUnitario() * cantidadProveedor);
                    carritoService.addItemDroga(request.getSession(), item, precioTotal);
                }

                
                
                if ("comprar".equals(action)) {
                    handleCarrito(request, response);
                } else {
                    handleDroga(request, response);
                }
                return;
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleHomepage(request, response);
                return;
            }
        }

        if (path.startsWith("/do-eliminar-item-droga")) {
            try {
                CarritoService carritoService = new CarritoService();
                Carrito carrito = carritoService.getCart(request.getSession());
                LinkedList<ItemCarrito> items = carrito.getItems();
                LinkedList<ItemCarrito> itemsAEliminar = new LinkedList<>();
                String drogaId = request.getParameter("drogaId");

                for(ItemCarrito item : items) {
                    if(item.getDroga().getId().equals(Integer.parseInt(drogaId))){
                        itemsAEliminar.add(item);
                    }
                }   

                for(ItemCarrito item: itemsAEliminar){
                    int precioTotal = item.getCantidad() * item.getPrecioUnitario().intValue();
                    carritoService.removeItem(request.getSession(), item, precioTotal);
                }

                handleCarrito(request, response);
                return;
                
            } catch (Exception e) {
                errores.add(e.getMessage());
                handleCarrito(request, response);
                return;
            }
        }

        if (path.startsWith("/do-checkout")) {
            try {
                // Obtener datos del carrito y usuario
                String usuarioId = getUserIdFromSession(request);
                if (usuarioId == null) {
                    errores.add("Debes estar logueado para proceder al pago");
                    handleLogin(request, response);
                    return;
                }
                
                // Obtener carrito de sesión
                CarritoService carritoService = new CarritoService();
                Carrito carrito = carritoService.getCart(request.getSession());
                
                if (carrito == null || carrito.getItems() == null || carrito.getItems().isEmpty()) {
                    errores.add("El carrito está vacío");
                    handleCarrito(request, response);
                    return;
                }
                
                // Construir los items para el checkout
                LinkedList<CheckoutItemDTO> checkoutItems = new LinkedList<>();
                for (ItemCarrito item : carrito.getItems()) {
                    CheckoutItemDTO checkoutItem = new CheckoutItemDTO(
                        item.getDroga().getId().toString(),
                        item.getDroga().getNombre(),
                        item.getCantidad(),
                        item.getPrecioUnitario().doubleValue()
                    );
                    checkoutItems.add(checkoutItem);
                }
                
                // Crear request de checkout
                CheckoutRequestDTO checkoutRequest = new CheckoutRequestDTO(
                    usuarioId,
                    carrito.getTotal().doubleValue(),
                    "Compra de medicamentos Farmalibre",
                    checkoutItems
                );
                
                // Llamar al controlador de checkout
                CheckoutResponseDTO checkoutResponse = CheckoutController.crearPreferenciaPago(checkoutRequest);
                
                // Guardar preferenceId en sesión para uso posterior
                request.getSession().setAttribute("checkoutPreferenceId", checkoutResponse.getPreferenceId());
                
                // Redirigir a Mercado Pago (preferir sandbox si está disponible en desarrollo)
                String redirectUrl = checkoutResponse.getSandboxInitPoint() != null 
                    ? checkoutResponse.getSandboxInitPoint() 
                    : checkoutResponse.getInitPoint();
                
                response.sendRedirect(redirectUrl);
                return;
                
            } catch (Exception e) {
                errores.add("Error al crear preferencia de pago: " + e.getMessage());
                request.setAttribute("errores", errores);
                handleCarrito(request, response);
                return;
            }
        }
        
        if (path.startsWith("/checkout/success")) {
            try {
                String preferenceId = (String) request.getSession().getAttribute("checkoutPreferenceId");
                String paymentId = request.getParameter("payment_id");
                
                // Obtener carrito antes de limpiarlo
                CarritoService carritoService = new CarritoService();
                Carrito carrito = carritoService.getCart(request.getSession());
                
                // Procesar el pago exitoso
                CheckoutController.procesarRetornoExitoso(preferenceId, paymentId);
                
                // Reducir stock de los productos
                if (carrito != null && carrito.getItems() != null && !carrito.getItems().isEmpty()) {
                    try {
                        StockReductionService.reducirStock(carrito.getItems());
                        log.info("Stock reducido exitosamente");
                    } catch (Exception e) {
                        log.warning("Error al reducir stock, pero el pago fue exitoso: " + e.getMessage());
                        errores.add("Pago completado pero hubo un error al actualizar el inventario");
                    }
                }
                
                // Limpiar el carrito
                carritoService.clear(request.getSession());
                
                request.setAttribute("pageTitle", "Pago Exitoso");
                request.setAttribute("content", "/WEB-INF/views/pages/checkout-success.jsp");
                request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
                return;
                
            } catch (Exception e) {
                errores.add("Error al procesar pago: " + e.getMessage());
                request.setAttribute("errores", errores);
                handleHomepage(request, response);
                return;
            }
        }
        
        if (path.startsWith("/checkout/failure")) {
            try {
                String preferenceId = (String) request.getSession().getAttribute("checkoutPreferenceId");
                
                // Procesar el pago fallido
                CheckoutController.procesarRetornoFallido(preferenceId);
                
                errores.add("El pago fue cancelado. Por favor, intenta de nuevo.");
                request.setAttribute("errores", errores);
                handleCarrito(request, response);
                return;
                
            } catch (Exception e) {
                errores.add("Error al procesar fallo de pago: " + e.getMessage());
                request.setAttribute("errores", errores);
                handleCarrito(request, response);
                return;
            }
        }
        
        if (path.startsWith("/checkout/pending")) {
            try {
                request.setAttribute("pageTitle", "Pago Pendiente");
                request.setAttribute("content", "/WEB-INF/views/pages/checkout-pending.jsp");
                request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
                return;
                
            } catch (Exception e) {
                errores.add("Error: " + e.getMessage());
                request.setAttribute("errores", errores);
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

        // Obtener la sesión
        HttpSession session = request.getSession(true);
        
        // Intentar recuperar el forward de la sesión
        String forward = (String) session.getAttribute("carritoForward");
        
        // Si no hay forward en sesión, usar el Referer del request actual
        // Esto sucede cuando entras al carrito por primera vez
        if (forward == null || forward.isEmpty()) {
            String referer = request.getHeader("Referer");
            
            // Validar que el referer sea válido y no sea un endpoint de acción
            if (referer != null && !referer.contains("/do-") && !referer.contains("/auth/")) {
                forward = referer;
                session.setAttribute("carritoForward", forward);
            }
        }
        
        // Si aún no hay forward válido, usar el homepage
        if (forward == null || forward.isEmpty()) {
            forward = request.getContextPath() + "/";
        }

        // Obtener el carrito de la sesión
        CarritoService carritoService = new CarritoService();
        Carrito carrito = carritoService.getCart(request.getSession());
        
        request.setAttribute("carrito", carrito);
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

        if (drogaId == null || drogaId.trim().isEmpty()) {
            handleHomepage(request, response);
            return;
        }

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
        String userId = getUserIdFromSession(request);
        PerfilInfoDTO perfilData = ActualizarPerfilController.getItem(userId);
        request.setAttribute("perfilData", perfilData);

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

        boolean hasSearch = searchQuery != null && !searchQuery.isBlank();
        boolean hasCategoria = categoriaId != null && !categoriaId.isBlank();

        // Obtener el carrito de la sesión
        CarritoService carritoService = new CarritoService();
        Carrito carrito = carritoService.getCart(request.getSession());
        int itemsEnElCarrito = carrito.getItems().size();

        LinkedList<DrogaDTO> drogaDTOs;

        if (hasSearch && hasCategoria) {
            doFilter(request, response);
            return;
        }

        // Filtrar solo por categoría
        if (hasCategoria) {
            LinkedList<Droga> drogasFiltradas = new LinkedList<>();
            for (Droga droga : drogas) {
                if (droga.getCategoriaDroga().getId().toString().equals(categoriaId)) {
                    drogasFiltradas.add(droga);
                }
            }
            drogaDTOs = BuscarDrogasController.buscarDrogas(drogasFiltradas, null);
        }
        // Filtrar solo por búsqueda
        else if (hasSearch) {
            drogaDTOs = BuscarDrogasController.buscarDrogas(drogas, searchQuery);
        }
        // Sin filtros
        else {
            drogaDTOs = BuscarDrogasController.buscarDrogas(drogas, null);
        }

        request.setAttribute("drogaDTOs", drogaDTOs);
        request.setAttribute("itemsEnElCarrito", itemsEnElCarrito);
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
            LinkedList<DrogaDTO> drogaDTOsTemporales = BuscarDrogasController.buscarDrogas(drogas, searchQuery);

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