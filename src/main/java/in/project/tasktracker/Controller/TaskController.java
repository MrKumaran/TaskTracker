package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.ObjectBuilder;
import in.project.tasktracker.Model.Profile;
import in.project.tasktracker.Model.Task;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

// This servlet is responsible for managing all task related operations
@WebServlet(name = "TaskController", value = {
        "/",
        "/newTask",
        "/updateTaskStatus",
        "/deleteTask"
})
public class TaskController extends HttpServlet {
    DBManager dbManager;

    @Override
    public void init() {
        this.dbManager = DBManager.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();
        Profile profile = dbManager.retrieveProfile((String) session.getAttribute("user"));
        request.setAttribute("user", profile);
        if (path.equals("/")) {
            List<Task> tasks = dbManager.retrieveUsersTasks(profile.getUserId());
            request.setAttribute("tasks", tasks);
            request.getRequestDispatcher("View/taskPage.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();
        boolean isOperationSuccess = false;
        Profile profile = dbManager.retrieveProfile((String) session.getAttribute("user"));
        response.setContentType("application/json"); // This is here to send response to JS, so that it will alert user about operation
        switch (path) {
            case "/newTask" -> {
                Task task = ObjectBuilder.taskObjectBuilder(request, profile.getUserId());
                if (task != null) isOperationSuccess = dbManager.addTask(task);
                session.setAttribute("operation", "newTaskAdded");
            }
            case "/updateTaskStatus" -> {
                try {
                    isOperationSuccess = dbManager.updateTaskStatus(
                            profile.getUserId(),
                            request.getParameter("taskId"),
                            Boolean.parseBoolean(request.getParameter("isDone"))
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                session.setAttribute("operation", "updatedTaskStatus");
            }
            case "/deleteTask" -> {
                isOperationSuccess = dbManager.deleteTask(profile.getUserId(), request.getParameter("taskId"));
                session.setAttribute("operation", "deletedTask");
            }
        }
        session.setAttribute("isOperationSuccess", isOperationSuccess);
    }
}
