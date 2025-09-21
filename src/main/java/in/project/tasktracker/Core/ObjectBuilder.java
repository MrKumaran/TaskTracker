package in.project.tasktracker.Core;

import in.project.tasktracker.Model.Task;
import in.project.tasktracker.Model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

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
        task.setTaskId(Authentication.generateUUID());
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

    public static User userObjectBuilder(HttpServletRequest request) {
        User user = new User();
        user.setUserId(Authentication.generateUUID());
        user.setMail(request.getParameter("mail"));
        user.setUserName(request.getParameter("userName"));
        user.setAvatarURL(request.getParameter("avatarUrl"));
        user.setSalt(Authentication.generateSalt());
        user.setPassword(Authentication.passwordHash(request.getParameter("password"), user.getSalt()));
        return user;
    }


}
