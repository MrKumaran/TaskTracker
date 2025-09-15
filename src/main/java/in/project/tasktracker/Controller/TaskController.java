package in.project.tasktracker.Controller;

import in.project.tasktracker.Core.DBManager;
import in.project.tasktracker.Core.ObjectBuilder;
import in.project.tasktracker.Model.Task;
import in.project.tasktracker.Model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "TaskController", value = {
            "/",
            "/newTask",
            "/updateTaskStatus",
            "/deleteTask"
        })
public class TaskController extends HttpServlet {
    DBManager dbManager;

    public void init() {
        this.dbManager = DBManager.getInstance();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("/landing");
        } else {
            String path = request.getServletPath();
            User user = dbManager.retrieveUser((String) session.getAttribute("user"));
            request.setAttribute("user", user);
            if(path.equals("/")) {
                List<Task> tasks = dbManager.retrieveUsersTasks(user.getUserId());
                request.setAttribute("tasks", tasks);
                request.getRequestDispatcher("View/taskPage.jsp").forward(request, response);
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null){
            response.sendRedirect("/landing");
        } else {
            String path = request.getServletPath();
            boolean isOperationSuccess = false;
            User user = dbManager.retrieveUser((String) session.getAttribute("user"));
            response.setContentType("application/json");
            switch (path) {
                case "/newTask" -> {
                    Task task = ObjectBuilder.taskObjectBuilder(request, user.getUserId());
                    if (task != null) isOperationSuccess = dbManager.addTask(task);
                    session.setAttribute("code", 1);
                }
                case "/updateTaskStatus" -> {
                    try {
                        isOperationSuccess = dbManager.updateCompletedTask(
                                user.getUserId(),
                                request.getParameter("taskId"),
                                Boolean.parseBoolean(request.getParameter("isDone"))
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    session.setAttribute("code", 2);
                }
                case "/deleteTask" -> {
                    isOperationSuccess = dbManager.deleteTask(user.getUserId(), request.getParameter("taskId"));
                    session.setAttribute("code", 3);
                }
            }
            session.setAttribute("isOperationSuccess", isOperationSuccess);
        }
    }
}
