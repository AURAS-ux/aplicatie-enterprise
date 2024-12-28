package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        String role = (session != null) ? (String) session.getAttribute("role") : null;
        String path = request.getRequestURI();

        if(path.contains("adminPage") && !"admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/unauthorized.xhtml");
            return;
        }

        if (path.equals(request.getContextPath() + "/") || path.contains("index.xhtml") || path.contains("/unauthorized.xhtml")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!path.contains("noaccount") && !"user".equals(role) && !"admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/unauthorized.xhtml");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
