package in.project.tasktracker.Filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(
        filterName = "AuthenticationFilter",
        urlPatterns = {
                "/",
                "/newTask",
                "/updateTaskStatus",
                "/deleteTask",
                "/profile",
                "/logout"
        }
)
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            ((HttpServletResponse) servletResponse).sendRedirect("/landing");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
