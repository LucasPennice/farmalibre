package web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (path.equals("/") || path.equals("/index")) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
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

    private void handleCarrito(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/carrito.jsp").forward(request, response);
    }

    private void handleDroga(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/droga.jsp").forward(request, response);
    }

    private void handleUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/usuarios.jsp").forward(request, response);
    }
    
    private void handleAuth(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/auth.jsp").forward(request, response);
    }
    
    private void handleOnboarding(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/onboarding.jsp").forward(request, response);
    }
    
    private void handleInventario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/inventario.jsp").forward(request, response);
    }
    
    private void handlePerfil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/perfil.jsp").forward(request, response);
    }
    
    private void handleAprobarCategorias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/aprobarCategorias.jsp").forward(request, response);
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/404.jsp").forward(request, response);
    }
}