<%@ page import="in.project.tasktracker.Model.Profile.ProfileDto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update password - Task Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/updatePassword.css" type="text/css">
    <script src="../Scripts/errorIndication.js" defer></script>
</head>
<body>
<%
    ProfileDto profile = (ProfileDto) request.getAttribute("profile");
%>
<div class="container">
    <div class="app-title">
        <a href="${pageContext.request.contextPath}/" target="_self" class="app-title" id="app-title-id">
            <%@include file="../assets/titleLogo.svg" %>
            <h1>Task Tracker</h1>
        </a>
    </div>
    <div class="body">
        <div class="userAvatarDiv">
            <div id="profilePicDiv">
                <% if (profile.getAvatarUrl() == null || profile.getAvatarUrl().isEmpty()) { %>
                <%@include file="../assets/userAvatar.svg" %>
                <% } else { %>
                <img src="<%=profile.getAvatarUrl()%>" alt="" id="profilePic">
                <% } %>
            </div>
            <div id="user-name">
                <h2>
                    <%=profile.getUserName()%>
                </h2>
            </div>
        </div>
        <form id="updatePassword" action="${pageContext.request.contextPath}/update/password" method="post">
            <ul>
                <li>
                    <label for="current-password">Current Password<sup style="color: red">*</sup>:</label>
                    <input type="password" id="current-password" placeholder="******" name="current-password" required/>
                </li>
                <li>
                    <label for="password">New Password:</label>
                    <input type="password" id="password" placeholder="******" name="password" required/>
                </li>
                <li>
                    <label for="confirm-password">Confirm Password:</label>
                    <input type="password" id="confirm-password" placeholder="******" name="confirm-password" required/>
                </li>
                <li>
                    <div id="button-div">
                        <button value="submit">Update password</button>
                        <a href="${pageContext.request.contextPath}/profile" class="blueBorder"> Back </a>
                    </div>
                </li>
            </ul>
        </form>
    </div>
    <div id="sideNotification"></div>
</div>
</body>
</html>
