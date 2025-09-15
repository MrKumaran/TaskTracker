package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.ObjectBuilder;
import in.project.tasktracker.Model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "Authentication", value = {"/landing", "/login", "/signup"})
public class AuthenticationController extends HttpServlet {
    DBManager dbManager;

     public void init() {
        this.dbManager = DBManager.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/login" -> request.getRequestDispatcher("View/login.jsp").forward(request, response);
            case "/signup" -> request.getRequestDispatcher("View/signup.jsp").forward(request, response);
            case "/landing" -> request.getRequestDispatcher("View/landing.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/login")) {
            loginHandler(request, response);
        } else if (path.equals("/signup")) {
           signupHandler(request, response);
        }
    }

    private void signupHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mail = request.getParameter("mail");
        boolean isPresent = dbManager.isNewMail(mail);
        if (isPresent) {
            response.sendRedirect("/login");
            return;
        }
        User user = ObjectBuilder.userObjectBuilder(request);
        boolean isSignUp = dbManager.signupViaMail(user);
        if (isSignUp) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(600);
            session.setAttribute("user", user.getUserId());
            response.sendRedirect("/");
        }
        else request.getRequestDispatcher("View/signup.jsp").forward(request, response);
    }

    private void loginHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPresent = dbManager.isNewMail(request.getParameter("mail"));
        if (!isPresent) {
            response.sendRedirect("/signup");
            return;
        }
        String userId = dbManager.loginViaMail(
                request.getParameter("mail"),
                request.getParameter("password")
        );
        if (userId != null){
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(600);
            session.setAttribute("user", userId);
            response.sendRedirect("/");
        }
        else request.getRequestDispatcher("View/login.jsp").forward(request, response);
    }
}
