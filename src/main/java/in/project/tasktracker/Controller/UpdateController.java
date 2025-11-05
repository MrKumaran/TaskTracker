package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.EntityBuilder;
import in.project.tasktracker.Enums.Auth.AuthEnum;
import in.project.tasktracker.Enums.Error.ErrorEnum;
import in.project.tasktracker.Enums.Profile.ProfileEnum;
import in.project.tasktracker.Enums.Task.TaskEnum;
import in.project.tasktracker.Model.Profile.ProfileDto;
import in.project.tasktracker.Model.Profile.Profile;
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
        String endpoint = request.getPathInfo();
        HttpSession session = request.getSession(false);
        String userId = session.getAttribute("user").toString();

        if (endpoint.equals("/password")) {
            Profile profile = dbManager.retrieveProfile(userId);
            ProfileDto profileDto = new ProfileDto(
                    profile.getUserName(),
                    profile.getAvatarURL()
            );
            request.setAttribute("profile", profileDto);
            request.getRequestDispatcher("/View/updatePassword.jsp").forward(request, response);
            return;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String endpoint = request.getPathInfo();
        Enum operation;
        boolean isOperationSuccess = false;

        HttpSession session = request.getSession(false);
        String userId = session.getAttribute("user").toString();

        switch(endpoint){
            case "/username" -> {
                ProfileUsernameUpdateDto profileUsernameUpdateDto = EntityBuilder.profileUsernameUpdateDtoBuilder(request, userId);
                operation = dbManager.updateUserName(profileUsernameUpdateDto);
                isOperationSuccess = operation.equals(ProfileEnum.USERNAME_UPDATED);
            }

            case "/password" -> {
                if (!request.getParameter("confirm-password").equals(request.getParameter("password"))){
                    operation = AuthEnum.PASSWORD_NOT_MATCH;
                } else {
                    PasswordUpdateDto passwordUpdateDto = EntityBuilder.passwordUpdateDtoBuilder(request, userId);
                    if (passwordUpdateDto != null) {
                        operation = dbManager.changePassword(passwordUpdateDto);
                        if (operation.equals(AuthEnum.CREDENTIALS_NOT_MATCH) || operation.equals(AuthEnum.USER_NOT_FOUND)) {
                            response.sendRedirect("/logout", true);
                            return;
                        }
                        isOperationSuccess = operation.equals(AuthEnum.PASSWORD_UPDATED);
                    }
                    else operation = AuthEnum.PASSWORD_NOT_COMPLEX;
                }
            }

            case "/taskStatus" -> {
                String taskId = request.getParameter("taskId");
                operation = dbManager.updateTaskStatus(userId, taskId);
                isOperationSuccess = operation.equals(TaskEnum.TASK_STATUS_UPDATED);
            }

            default -> operation = ErrorEnum.ILLEGAL_OPERATION_REQUEST;
        }

        session.setAttribute("operation", operation.toString());
        session.setAttribute("isOperationSuccess", isOperationSuccess);
        response.sendRedirect("/");
    }
}
