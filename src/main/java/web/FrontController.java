package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

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

        if (path.equals("/") || path.equals("/index")) {
            request.setAttribute("pageTitle", "Inicio");
            request.setAttribute("content", "/WEB-INF/views/pages/index.jsp");
            request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
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
        // request.setAttribute("content", "/WEB-INF/views/pages/usuarios.jsp");
        request.getRequestDispatcher("/WEB-INF/views/pages/usuarios.jsp").forward(request, response);
        // request.getRequestDispatcher("/WEB-INF/views/layouts/main.jsp").forward(request, response);
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
}