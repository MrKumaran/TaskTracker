package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.EntityBuilder;
import in.project.tasktracker.Enums.Task.TaskBuilderEnum;
import in.project.tasktracker.Enums.Task.TaskEnum;
import in.project.tasktracker.Model.Profile.Profile;
import in.project.tasktracker.Model.Task.Task;
import in.project.tasktracker.Model.Task.UserTasks;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// This servlet is responsible for managing all task related operations
@WebServlet(name = "TaskController", value = {
        "/",
        "/newTask",
        "/deleteTask",
        "/editTask"
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
            UserTasks userTasks = dbManager.retrieveUsersTasks(profile.getUserId());
            if ((userTasks.getTasksCount() == -1) || (userTasks.getTasksDoneCount() == -1)) {
                session.setAttribute("operation", "DataFetch");
                session.setAttribute("isOperationSuccess", "false");
            }
            request.setAttribute("tasks", userTasks.getTasks());
            request.setAttribute("tasksCount",
                    (userTasks.getTasksCount() == -1)? "0":
                            String.valueOf(userTasks.getTasksCount())
            );
            request.setAttribute("tasksDoneCount",
                    (userTasks.getTasksDoneCount() == -1)? "0":
                            String.valueOf(userTasks.getTasksDoneCount())
            );
            request.getRequestDispatcher("View/taskPage.jsp").forward(request, response);
        } else if (path.equals("/editTask")) {
            String taskId = request.getParameter("taskId");
            Task task = dbManager.getTaskByID(taskId, profile.getUserId());
            request.setAttribute("task", task);
            request.getRequestDispatcher("View/taskEdit.jsp").forward(request, response);
        } else {
            response.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();
        boolean isOperationSuccess = false;
        String userId = session.getAttribute("user").toString();
        response.setContentType("application/json"); // This is here to send response to JS, so that it will alert user about operation

        switch (path) {
            case "/newTask" -> {
                Task task = EntityBuilder.taskEntityBuilder(request, userId, TaskBuilderEnum.BUILD_TASK);
                if (task != null) isOperationSuccess = dbManager.upsertTask(task);
                session.setAttribute("operation", "newTaskAdded");
            }
            case "/deleteTask" -> {
                isOperationSuccess = dbManager.deleteTask(userId, request.getParameter("taskId"));
                session.setAttribute("operation", "deletedTask");
            }
            case "/editTask" -> {
                Task task = EntityBuilder.taskEntityBuilder(request, userId, TaskBuilderEnum.EDIT_TASK);
                session.setAttribute("operation", "taskUpdate");
                if(task != null) isOperationSuccess = dbManager.upsertTask(task);
                session.setAttribute("isOperationSuccess", isOperationSuccess);
                response.sendRedirect("/");
                return;
            }
        }
        session.setAttribute("isOperationSuccess", isOperationSuccess);
    }
}
