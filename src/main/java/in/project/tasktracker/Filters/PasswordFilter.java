package in.project.tasktracker.Filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Objects;

// Filter to check about form submitted on signup
public class PasswordFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String password = servletRequest.getParameter("password");
        String confirmPassword = servletRequest.getParameter("confirm-password");
        String path = ((HttpServletRequest) servletRequest).getServletPath();
        if (!Objects.equals(password, confirmPassword)) {
            if(path.equals("/signup")){
                servletRequest.setAttribute("error", "passwordNotMatch");
                servletRequest.getRequestDispatcher("View/signup.jsp").forward(servletRequest, servletResponse);
            } else if(path.equals("/updateProfile")) {
                HttpServletRequest request = (HttpServletRequest) servletRequest;
                HttpSession session = request.getSession(false);
                session.setAttribute("error", "passwordNotMatch");
                ((HttpServletResponse) servletResponse).sendRedirect("/edit-profile");
            }
        } else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
}
