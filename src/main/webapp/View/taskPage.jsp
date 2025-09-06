<%@ page import="in.project.tasktracker.Model.Task" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Tasks</title>
        <script src="../Scripts/taskPage.js"></script>
    </head>
    <body>
        <%
            @SuppressWarnings("unchecked") List<Task> tasks = (List<Task>) request.getAttribute("tasks");
            if(tasks == null) tasks = new ArrayList<>();
        %>
        <div>
            <div id="status">
            </div>
            <button id="new-task-btn">Add new task</button>
            <dialog id="new-task">
                <form id="new-task-form" method="post">
                    <ul>
                        <li>
                            <label for="new-task-title">Task Title:</label>
                            <input type="text" name="new-task-title" id="new-task-title"/>
                        </li>
                        <li>
                            <label for="new-task-due">Task Due:</label>
                            <input type="datetime-local" name="new-task-due" id="new-task-due"/>
                        </li>
                        <li>
                            <button type="button" id="task-submit-btn">Submit</button>
                        </li>
                    </ul>
                </form>
            </dialog>
            <% if (!tasks.isEmpty()) {%>
                <div>
                    <% for(Task task : tasks) {
                    %>
                    <div>
                        <h4><%= task.getTask_title() %></h4>
                        <h3><%= task.getDue() %></h3>
                    </div>
                    <% }
            } else {%>
                    <div>
                        <h1>WOW! such empty!... add new task to track your life the way you want >< </h1>
                    </div>
                    <% }%>
                </div>
        </div>
    </body>
</html>
