<%@ page import="in.project.tasktracker.Model.Task" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Task Edit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/taskEdit.css" type="text/css">
    <script src="../Scripts/updateTask.js" defer></script>
</head>
<body>
<%
Task task = (Task) request.getAttribute("task");
%>
<div class="container">
    <div class="app-title-div">
        <a href="${pageContext.request.contextPath}/" target="_self" class="app-title" id="app-title-id">
            <%@include file="../assets/titleLogo.svg" %>
            <h1>Task Tracker</h1>
        </a>
    </div>
    <div id="editFormDiv">
        <p id="taskId" style="display: none"><%=task.getTaskId()%></p>
        <form id="edit-task-form" method="post">
            <ul>
                <li>
                    <label for="new-task-title">Task Title:</label>
                    <input type="text" name="new-task-title" id="new-task-title" value="<%=task.getTaskTitle()%>" required/>
                </li>
                <li>
                    <label for="new-task-due">Task Due:</label>
                    <input type="datetime-local" name="new-task-due" id="new-task-due" value="<%=task.getDue()%>" required/>
                </li>
                <li>
                    <button type="button" id="task-submit-btn">Submit</button>
                </li>
            </ul>
        </form>
    </div>
</div>
</body>
</html>
