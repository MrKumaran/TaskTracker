package in.project.tasktracker.Filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.util.Objects;

@WebFilter(urlPatterns = {"/signup"})
public class SignUpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String password = servletRequest.getParameter("password");
        String confirmPassword = servletRequest.getParameter("confirm-password");
        if (!Objects.equals(password, confirmPassword)) {
            servletRequest.setAttribute("error", "passwordNotMatch");
            servletRequest.getRequestDispatcher("View/signup.jsp").forward(servletRequest, servletResponse);
        } else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
}
