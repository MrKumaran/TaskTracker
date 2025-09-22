<%@ page import="in.project.tasktracker.Model.Profile" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit profile - Task Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/profileEdit.css" type="text/css">
    <script src="../Scripts/errorIndication.js" defer></script>
</head>
<body>
<% Profile profile = (Profile) request.getAttribute("user");
String error = (String) session.getAttribute("error");
if(error != null && !error.isEmpty()) session.removeAttribute("error");
%>
<div class="container">
        <form id="authForm" action="updateProfile" method="post">
            <ul>
                <li>
                    <label for="userName">User Name:</label>
                    <input type="text" id="userName" placeholder="Enter user name" name="userName" value="<%= profile.getUserName() %>" required/>
                </li>
                <li>
                    <label for="mail">Email:</label>
                    <input type="email" id="mail" placeholder="Enter mail address" name="mail" value="<%= profile.getMailId() %>" required/>
                </li>
                <li>
                    <label for="password">Password:</label>
                    <input type="password" id="password" placeholder="******" name="password"/>
                </li>
                <li>
                    <label for="confirm-password">Confirm Password:</label>
                    <input type="password" id="confirm-password" placeholder="******" name="confirm-password"/>
                </li>
                <li>
                    <button value="submit">Update profile</button>
                </li>
            </ul>
    </form>
    <div id="errorDiv" style="display: none; color: red"><%=error%></div>
</div>
</body>
</html>
