package in.project.tasktracker.Core;

import in.project.tasktracker.Model.Task;
import in.project.tasktracker.Model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

// This class is responsible for building object
// It builds object from request
public class ObjectBuilder {
    public static Task taskObjectBuilder(HttpServletRequest request, String userId) {
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
        JSONObject json = new JSONObject(jsonStringBuilder.toString());
        Task task = new Task();
        task.setUserId(userId);
        task.setTaskId(
                (!Objects.equals(json.optString("taskId"), ""))?json.getString("taskId"):
                new Authentication().generateUUID());
        task.setTaskTitle(json.getString("new-task-title"));
        String dueParam = json.getString("new-task-due");
        LocalDateTime due = null;
        if (dueParam != null && !dueParam.isEmpty()) {
            due = LocalDateTime.parse(dueParam);
        }
        task.setDue(due);
        task.setDone(false);
        task.setCompletedAt(null);
        return task;
    }

    public static User userObjectBuilder(HttpServletRequest request, String userId) { // if null is returned then password not valid
        Authentication authentication = new Authentication();
        User user = new User();
        user.setUserId((userId == null || userId.isEmpty())?authentication.generateUUID():userId);
        String password = request.getParameter("password");
        boolean isPasswordPresent = !(password == null || password.isEmpty()); // Also using for profile update so something password will not be provided
        if(isPasswordPresent){
            boolean passCheck = authentication.passwordStrengthCheck(password);
            if (!passCheck) return null;
        }
        user.setMail(request.getParameter("mail"));
        user.setUserName(request.getParameter("userName"));
        user.setAvatarURL(request.getParameter("avatarUrl"));
        user.setSalt(
                (isPasswordPresent)?
                        authentication.generateSalt():
                        null
                );
        user.setPassword(
                (isPasswordPresent)?
                        authentication.passwordHash(password, user.getSalt())
                        :null
        );
        return user;
    }
}
