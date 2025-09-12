package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.ObjectBuilder;
import in.project.tasktracker.Model.Task;
import in.project.tasktracker.Model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "UpdateTask", value = {"/newTask", "/updateTask"})
public class UpdateTask extends HttpServlet {
    DBManager dbManager;

    public void init() {
        this.dbManager = DBManager.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null){
            response.sendRedirect("/landing");
        } else {
            String path = request.getServletPath();
            User user = dbManager.retrieveUser((String) session.getAttribute("user"));
            response.setContentType("application/json");
            if (path.equals("/newTask")) {
                boolean isInserted;
                Task task = ObjectBuilder.taskObjectBuilder(request, user.getUserId());
                if (task == null) isInserted = false;
                else isInserted = dbManager.addTask(task);
                String responseJSON = "{\"status\": " + isInserted + "}";
                response.getWriter().write(responseJSON);
            } else if(path.equals("/updateTask")) {
                boolean isUpdated;
                try {
                    isUpdated = dbManager.updateCompletedTask(
                            user.getUserId(),
                            request.getParameter("taskId"),
                            Boolean.parseBoolean(request.getParameter("isDone"))
                    );
                } catch (Exception e) {
                    isUpdated = false;
                }
                String responseJSON = "{\"status\": " + isUpdated + "}";
                response.getWriter().write(responseJSON);
            }
        }
    }
}
