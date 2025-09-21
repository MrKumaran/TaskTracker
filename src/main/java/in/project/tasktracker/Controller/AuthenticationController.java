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

// This servlet is responsible for authentication signing up new user, login in existing user
@WebServlet(name = "AuthenticationController", value = {"/landing", "/login", "/signup"})
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
        // just here as placeholder for now
        // later it will be replaced by email verification
        // when I do, no need of this redirect
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
            session.setAttribute("operation", "greets");
            session.setAttribute("isOperationSuccess", user.getUserName());
            response.sendRedirect("/");
        }
        else {
            request.setAttribute("error", "errorCreatingAccount");
            request.getRequestDispatcher("View/signup.jsp").forward(request, response);
        }
    }

    private void loginHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String userId = dbManager.loginViaMail(
                 request.getParameter("mail"),
                 request.getParameter("password")
         );
         if (userId != null){
             HttpSession session = request.getSession();
             session.setMaxInactiveInterval(600);
             session.setAttribute("user", userId);
             session.setAttribute("operation", "greets");
             session.setAttribute("isOperationSuccess", dbManager.retrieveProfile(userId).getUserName());
             response.sendRedirect("/");
         }
        else {
            request.setAttribute("error", "credentialsNotMatch");
            request.getRequestDispatcher("View/login.jsp").forward(request, response);
        }
    }
}
