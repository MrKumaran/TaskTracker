package in.project.tasktracker.Filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Filter used to check if user is logged in
@WebFilter(
        filterName = "AuthorizationFilter",
        urlPatterns = {
                "/",
                "/newTask",
                "/updateTaskStatus",
                "/deleteTask",
                "/profile",
                "/logout",
                "/upload-profile-pic",
                "/delete-profile-pic"
        }
)
public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(false);
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // declaring no cache to make sure after logout, old page not rendering
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/landing");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
