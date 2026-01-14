package web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        System.out.println("AAAAA entre aca");

        boolean isPrivate =
            path.startsWith("/carrito") ||
            path.startsWith("/inventario") ||
            path.startsWith("/perfil") ||
            path.startsWith("/aprobar-categorias");

        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);

        if (!loggedIn && isPrivate) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        chain.doFilter(request, response);
    }
}