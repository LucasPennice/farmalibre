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

        boolean isPrivate = path.startsWith("/carrito") ||
                path.startsWith("/inventario") ||
                path.startsWith("/perfil") ||
                path.startsWith("/aprobar-categorias");

        boolean blockIfLogged = path.startsWith("/auth") && !path.startsWith("/auth/do-logout");

        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("usuario_id") != null);

        if(loggedIn && blockIfLogged){
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        if (!loggedIn && isPrivate) {
            response.sendRedirect(request.getContextPath() + "/auth/register");
            return;
        }

        chain.doFilter(request, response);
    }
}