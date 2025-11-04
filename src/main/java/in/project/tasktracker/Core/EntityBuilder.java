package in.project.tasktracker.Core;

import in.project.tasktracker.Enums.Task.TaskBuilderEnum;
import in.project.tasktracker.Model.Task.Task;
import in.project.tasktracker.Model.User.AuthEntity;
import in.project.tasktracker.Model.User.UserRegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

// This class is responsible for building object
// It builds object from request
public class EntityBuilder {

    // Task related builders may undergo further changes
    public static Task taskEntityBuilder(HttpServletRequest request, String userId, TaskBuilderEnum taskBuilderEnum) {
        String jsonString = requestToStringBuilder(request);
        if (jsonString == null) return null;
        JSONObject requestJson = new JSONObject(jsonString);

        String dueParam = requestJson.getString("new-task-due");
        LocalDateTime due = null;
        if (dueParam != null && !dueParam.isEmpty()) {
            due = LocalDateTime.parse(dueParam);
        }

        if (taskBuilderEnum.equals(TaskBuilderEnum.BUILD_TASK)) {
            return newTaskBuilder(requestJson, userId, due);
        } else if (taskBuilderEnum.equals(TaskBuilderEnum.EDIT_TASK)) {
            return modifyTaskBuilder(requestJson, userId, due);
        }

        return null;
    }

    public static Task modifyTaskBuilder(JSONObject requestJson, String userId, LocalDateTime due) {
        return Task.builder()
                .setUserId(userId)
                .setTaskId(requestJson.getString("taskId"))
                .setTaskTitle(requestJson.getString("new-task-title"))
                .setDue(due)
                .setDone(false)
                .setCompletedAt(null)
                .build();
    }

    public static Task newTaskBuilder(JSONObject requestJson, String userId,  LocalDateTime due) {
        return Task.builder()
                .setUserId(userId)
                .setTaskId(new Authentication().generateUUID())
                .setTaskTitle(requestJson.getString("new-task-title"))
                .setDue(due)
                .setDone(false)
                .setCompletedAt(null)
                .build();
    }

    public static UserRegisterDto userRegisterDtoBuilder(HttpServletRequest request) {
        return UserRegisterDto.builder()
                .setMail(request.getParameter("mail"))
                .setPassword(request.getParameter("password"))
                .setUserName(request.getParameter("userName"))
                .build();
    }

    public static AuthEntity userObjectBuilder(HttpServletRequest request, String userId) { // if null is returned then password not valid
        Authentication authentication = new Authentication();
        AuthEntity authEntity = new AuthEntity();

        authEntity.setUserId((userId == null || userId.isEmpty())?authentication.generateUUID():userId);
        String password = request.getParameter("password");
        boolean isPasswordPresent = !(password == null || password.isEmpty()); // Also using for profile update so something password will not be provided
        if(isPasswordPresent){
            boolean passCheck = authentication.passwordStrengthCheck(password);
            if (!passCheck) return null;
        }
        authEntity.setMail(request.getParameter("mail"));
//        authEntity.setUserName(request.getParameter("userName"));
        authEntity.setSalt(
                (isPasswordPresent)?
                        authentication.generateSalt():
                        null
                );
        authEntity.setPassword(
                (isPasswordPresent)?
                        authentication.passwordHash(password, authEntity.getSalt())
                        :null
        );
        return authEntity;
    }

    private static String requestToStringBuilder(HttpServletRequest request) {
        StringBuilder jsonStringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonStringBuilder.toString();
    }
}
