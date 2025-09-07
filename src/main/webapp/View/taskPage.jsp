<%@ page import="in.project.tasktracker.Model.Task" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="in.project.tasktracker.Model.User" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Comparator" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Tasks</title>
        <script src="../Scripts/taskPage.js"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/taskPage.css" type="text/css">
    </head>
    <body>
        <%
            User user = (User) request.getAttribute("user");
            @SuppressWarnings("unchecked") List<Task> tasks = (List<Task>) request.getAttribute("tasks");
            if(tasks == null) tasks = new ArrayList<>();
            else tasks = tasks.stream().sorted(Comparator.comparing(Task::getDue)).toList();
            int totalTask = tasks.size();
            long taskDoneCount = tasks.stream().filter(Task::isDone).count();
        %>
        <div class="container">
            <div class="top-bar">
                <div class="app-title">
                    <%@include file="../assets/titleLogo.svg"%>
                    <h1>Task Tracker</h1>
                </div>
                <div class="user">
                    <p><%=user.getUserName()%></p>
                    <% if(user.getAvatar_url()==null || user.getAvatar_url().isEmpty()) { %>
                        <%@include file="../assets/userAvatar.svg"%>
                    <% } else { %>
                        <img src="<%=user.getAvatar_url()%>" alt="">
                    <% } %>
                </div>
            </div>
            <div id="status">
                <%@include file="../assets/trackerLogo.svg"%>
                <p>Total task: <%=totalTask%></p>
                <p>Task Completed: <%=taskDoneCount%></p>
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
                <div class="tasks">
                    <% for(Task task : tasks) {
                    %>
                    <div class="task">
                        <h4><%= task.getTask_title() %></h4>
                        <h3><%= task.getDue().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")) %></h3>
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
