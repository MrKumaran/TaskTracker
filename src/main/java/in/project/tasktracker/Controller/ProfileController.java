package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.ObjectBuilder;
import in.project.tasktracker.Model.Profile;
import in.project.tasktracker.Model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// This servlet is responsible for profile related operation except profile image -> it was managed by API/ProfileImageUpdater.java
@WebServlet(name = "ProfileController",
        value = {
                "/profile",
                "/logout",
                "/edit-profile",
                "/updateProfile"
})
public class ProfileController extends HttpServlet {
    DBManager dbManager;

    @Override
    public void init() {
        this.dbManager = DBManager.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();
        switch (path) {
            case "/logout" -> {
                session.invalidate();
                response.sendRedirect("/landing");
            }
            case "/profile" -> {
                Profile profile = dbManager.retrieveProfile((String) session.getAttribute("user"));
                request.setAttribute("user", profile);
                request.getRequestDispatcher("View/profile.jsp").forward(request, response);
            }
            case "/edit-profile" -> {
                Profile profile = dbManager.retrieveProfile((String) session.getAttribute("user"));
                request.setAttribute("user", profile);
                request.getRequestDispatcher("View/profileEdit.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();
        if(path.equals("/updateProfile")) {
            User userProfile = ObjectBuilder.userObjectBuilder(request, (String) session.getAttribute("user"));
            boolean isOperationSuccess = dbManager.updateProfile(userProfile);
            session.setAttribute("operation", "profileUpdated");
            session.setAttribute("isOperationSuccess", isOperationSuccess);
            response.sendRedirect("/");
        }
    }

}
