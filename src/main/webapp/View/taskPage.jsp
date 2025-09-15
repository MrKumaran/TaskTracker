<%@ page import="in.project.tasktracker.Model.Task" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="in.project.tasktracker.Model.User" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tasks</title>
    <script src="../Scripts/taskPage.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/taskPage.css" type="text/css">
</head>
<body>
<%
    User user = (User) request.getAttribute("user");
    @SuppressWarnings("unchecked") List<Task> tasks = (List<Task>) request.getAttribute("tasks");
    if (tasks == null) tasks = new ArrayList<>();
    else tasks = tasks.stream().sorted(Comparator.comparing(Task::isDone).thenComparing(Task::getDue)).toList();
    int totalTask = tasks.size();
    long taskDoneCount = tasks.stream().filter(Task::isDone).count();
%>
<div class="container">
    <div class="top-bar">
        <div class="app-title">
            <a href="" target="_self" class="app-title">
                <%@include file="../assets/titleLogo.svg" %>
                <h1>Task Tracker</h1>
            </a>
    </div>
        <a href="profile" target="_self">
            <div class="user">
                <p><%=user.getUserName()%>
                </p>
                <% if (user.getAvatarURL() == null || user.getAvatarURL().isEmpty()) { %>
                <%@include file="../assets/userAvatar.svg" %>
                <% } else { %>
                <img src="<%=user.getAvatarURL()%>" alt="">
                <% } %>
            </div>
        </a>
    </div>
    <div id="status">
        <%@include file="../assets/trackerLogo.svg" %>
        <p id="totalTask">Total task: <%=totalTask%>
        </p>
        <p id="completedTask">Task Completed:<span style="color: #1cb954;"> <%=taskDoneCount%></span></p>
    </div>
    <button id="new-task-btn"><%@include file="../assets/addIcon.svg"%></button>
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
    <%
        LocalDateTime now = LocalDateTime.now();
        if (!tasks.isEmpty()) {
    %>
    <div class="tasks">
        <%
            for (Task task : tasks) {
                String formatter = null;
                boolean isToday = false;
                boolean overdue = !task.isDone() && (now.isAfter(task.getDue()));
                String timeLeft = null;
                if (now.getMonthValue() == task.getDue().getMonthValue() && now.getDayOfMonth() == task.getDue().getDayOfMonth() && now.getYear() == task.getDue().getYear()) { // today
                    isToday = true;
                    timeLeft = Math.abs(task.getDue().getHour() - now.getHour()) + ":" + Math.abs(task.getDue().getMinute() - now.getMinute());
                } else if (now.getYear() == task.getDue().getYear()) { // same year
                    formatter = "HH:mm dd-MM";
                } else { // diff year
                    formatter = "HH:mm dd-MM-yyyy";
                }
        %>
        <div class="task" id="<%=task.getTaskId()%>" <%= (overdue) ? "style=\"color: #ba5656\"" : "" %>>
            <label class="custom-checkbox">
                <input type="checkbox" class="task-checkbox"
                       value="<%=task.getTaskId()%>" <%=task.isDone() ? "checked" : ""%>>
                <span class="circle"></span>
            </label>
            <h4><%= task.getTaskTitle() %>
            </h4>
            <h3>
                <%= (overdue) ?
                        (
                                isToday ?
                                        "<span style=\"font-weight:bold;\"> After target time: </span> " +
                                                "<span style=\"color:red\">" + timeLeft + "</span>" :
                                        "<span style=\"font-weight:bold;\">Should've completed by: </span> " +
                                                "<span style=\"color:red\">" + task.getDue().format(DateTimeFormatter.ofPattern(formatter)) + "</span>"
                        ) :
                        (
                                (task.isDone()) ?
                                        (
                                                "<span style=\"font-weight:bold;\">Completed At: </span> " +
                                                        "<span style=\"color:green\">" + task.getCompletedAt().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + "</span>"
                                        )
                                        : (
                                        isToday ? "<span style=\"font-weight:bold;\">Time left:" +
                                                " </span> " + timeLeft + "</span>" :
                                                task.getDue().format(DateTimeFormatter.ofPattern(formatter))
                                )
                        )
                %>
            </h3>
            <div class="editOptions">
                <div class="editTask" id="<%=task.getTaskId()%>">
                    <%@include file="../assets/editVector.svg" %>
                </div>
                <div class="deleteTask" id="<%=task.getTaskId()%>">
                    <%@include file="../assets/deleteVector.svg" %>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <div class="emptyTask">
            <img id="such-empty" src="../assets/cheems.png" alt="">
            <h1>WOW, such empty!...<br> add new task to track your life the way you want >< </h1>
        </div>
        <% }%>
    </div>
</div>
</body>
</html>
