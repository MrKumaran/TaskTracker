package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Model.Profile;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ProfileController", value = {"/profile", "/logout"})
public class ProfileController extends HttpServlet {
    DBManager dbManager;

    public void init() {
        this.dbManager = DBManager.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();
        if (path.equals("/logout")) {
            session.invalidate();
            response.sendRedirect("/");
        } else if (path.equals("/profile")) {
            Profile profile = dbManager.retrieveProfile((String) session.getAttribute("user"));
            request.setAttribute("user", profile);
            request.getRequestDispatcher("View/profile.jsp").forward(request, response);
        }
    }

}
