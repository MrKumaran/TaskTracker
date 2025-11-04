package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.EntityBuilder;
import in.project.tasktracker.Model.User.User;
import in.project.tasktracker.Model.User.UserAuthReturn;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

// This servlet is responsible for authentication signing up new user, login in existing user
@WebServlet(name = "AuthenticationController", value = {"/landing", "/login", "/signup"})
public class AuthenticationController extends HttpServlet {
    DBManager dbManager;

    @Override
     public void init() {
        this.dbManager = DBManager.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/login" -> request.getRequestDispatcher("View/login.jsp").forward(request, response);
            case "/signup" -> request.getRequestDispatcher("View/signup.jsp").forward(request, response);
            case "/landing" -> request.getRequestDispatcher("View/landing.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        UserAuthReturn user = null;

        if (path.equals("/login")) {
            user = loginHandler(request, response);
        } else if (path.equals("/signup")) {
           user = signupHandler(request, response);
        }

        if(user == null) return;

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(600);
        session.setAttribute("user", user.getId());
        session.setAttribute("operation", "greets");
        session.setAttribute("isOperationSuccess", user.getName());

        response.sendRedirect("/");
    }

    private UserAuthReturn signupHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO:
        //  just here as placeholder for now
        //  later it will be replaced by email verification
        //  when I do, no need of this redirect
        String mail = request.getParameter("mail");
        boolean isPresent = dbManager.isNewMail(mail);
        if (isPresent) {
            response.sendRedirect("/login");
            return null;
        }

        User user = EntityBuilder.userObjectBuilder(request, null);
        if (user == null) {
            request.setAttribute("error", "PasswordNotOK");
            request.getRequestDispatcher("View/signup.jsp").forward(request, response);
            return null;
        }

        boolean isSignUp = dbManager.signupViaMail(user);
        if (!isSignUp) {
            request.setAttribute("error", "errorCreatingAccount");
            request.getRequestDispatcher("View/signup.jsp").forward(request, response);
            return null;
        }

        return new UserAuthReturn(user.getUserId(), user.getUserName());
    }

    private UserAuthReturn loginHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String userId = dbManager.loginViaMail(
                 request.getParameter("mail"),
                 request.getParameter("password")
         );

         if (userId == null){
             request.setAttribute("error", "credentialsNotMatch");
             request.getRequestDispatcher("View/login.jsp").forward(request, response);
             return null;
         }

        return new UserAuthReturn(userId, dbManager.retrieveProfile(userId).getUserName());
    }
}
