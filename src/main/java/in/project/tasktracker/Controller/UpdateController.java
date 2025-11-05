package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.EntityBuilder;
import in.project.tasktracker.Enums.Auth.AuthEnum;
import in.project.tasktracker.Enums.Profile.ProfileEnum;
import in.project.tasktracker.Model.Profile.ProfileUsernameUpdateDto;
import in.project.tasktracker.Model.User.PasswordUpdateDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UpdateController",
        urlPatterns = {
        "/update/*"
        }
)
public class UpdateController extends HttpServlet {
    private DBManager dbManager;

    @Override
    public void init() {
        this.dbManager = DBManager.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String endpoint = request.getPathInfo();
        String operation;
        Boolean isOperationSuccess = false;

        HttpSession session = request.getSession();
        String userId = session.getAttribute("userId").toString();

        if (endpoint.equals("/username")) {
            ProfileUsernameUpdateDto profileUsernameUpdateDto = EntityBuilder.profileUsernameUpdateDtoBuilder(request, userId);
            ProfileEnum profileEnum = dbManager.updateUserName(profileUsernameUpdateDto);
            operation = profileEnum.toString();
        } else if (endpoint.equals("/password")) {
            PasswordUpdateDto passwordUpdateDto = EntityBuilder.passwordUpdateDtoBuilder(request, userId);
            if (passwordUpdateDto != null) {
                AuthEnum authEnum = dbManager.changePassword(passwordUpdateDto);
                operation = authEnum.toString();
            }
            else operation = "NOT_VALID_OPERATION";

        } else return;

        session.setAttribute("operation", operation);
        session.setAttribute("isOperationSuccess", isOperationSuccess);
        response.sendRedirect("/");
    }
}
