package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.EntityBuilder;
import in.project.tasktracker.Model.Profile.Profile;
import in.project.tasktracker.Model.Profile.ProfileUpdateDto;
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
                "/updateProfile",
                "/deleteAccount",
                "/update-username"
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
                System.out.println(profile.getUserName());
                request.setAttribute("user", profile);
                request.getRequestDispatcher("View/profileEdit.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();
        String currPswrd = "";
        // This is purely for education purpose using diff option for checking password...
        if(path.equals("/updateProfile")) {
            currPswrd = request.getParameter("current-password");
        } else if(path.equals("/deleteAccount")) {
            String jsonString = EntityBuilder.requestToStringBuilder(request);
            if (jsonString == null) return; // TODO send error
            currPswrd =  jsonString.split(":")[1];
            currPswrd = currPswrd.substring(1, currPswrd.length()-2);
        }
        boolean isAuthenticated = dbManager.loginViaMail(
                dbManager.retrieveProfile((String)session.getAttribute("user")).getMailId(),
                currPswrd
        ) != null;
        if(!isAuthenticated) {
            session.invalidate();
            response.sendRedirect("/landing");
            return;
        }
        if(path.equals("/updateProfile")) {
            ProfileUpdateDto profileUpdateDtoProfile = EntityBuilder.userObjectBuilder(request, (String) session.getAttribute("user"));
            if (profileUpdateDtoProfile == null) { // if user object is null then password not valid
                session.setAttribute("operation", "profileUpdated:password");
                session.setAttribute("isOperationSuccess", false);
                response.sendRedirect("/");
                return;
            }
            boolean isOperationSuccess = dbManager.updateProfile(profileUpdateDtoProfile);
            session.setAttribute("operation", "profileUpdated");
            session.setAttribute("isOperationSuccess", isOperationSuccess);
            response.sendRedirect("/");
        } else if(path.equals("/deleteAccount")) {
            dbManager.deleteAccount((String) session.getAttribute("user"));
            session.invalidate();
            response.sendRedirect("/landing");
        }
    }

}
