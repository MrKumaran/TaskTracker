<%@ page import="in.project.tasktracker.Model.Profile" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit profile - Task Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/profileEdit.css" type="text/css">
    <script src="../Scripts/deleteAccountMethod.js" defer></script>
    <script src="../Scripts/errorIndication.js" defer></script>
    <script src="../Scripts/profilePicUpdater.js"  defer type="module"></script>
</head>
<body>
<% Profile profile = (Profile) request.getAttribute("user");
    String error = (String) session.getAttribute("error");
    if (error != null && !error.isEmpty()) session.removeAttribute("error");
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
                <% if (profile.getAvatarURL() == null || profile.getAvatarURL().isEmpty()) { %>
                <%@include file="../assets/userAvatar.svg" %>
                <% } else { %>
                <img src="<%=profile.getAvatarURL()%>" alt="" id="profilePic">
                <% } %>
            </div>
            <div class="pic-options" id="picOptions">
                <% if (!(profile.getAvatarURL() == null) && !profile.getAvatarURL().isEmpty()) { %>
                <button id="viewPicBtn">View</button>
                <% } %>
                <button id="uploadPicBtn">Upload New</button>
                <% if (!(profile.getAvatarURL() == null) && !profile.getAvatarURL().isEmpty()) { %>
                <button id="removePicBtn">Remove</button>
                <% } %>
                <input type="file" id="uploadInput" style="display:none">
            </div>
        </div>
        <form id="authForm" action="updateProfile" method="post">
            <ul>
                <li>
                    <label for="userName">User Name:</label>
                    <input type="text" id="userName" placeholder="Enter user name" name="userName"
                           value="<%= profile.getUserName() %>" required/>
                </li>
                <%--                <li>--%>
                <%--                    <label for="mail">Email:</label>--%>
                <%--                    <input type="email" id="mail" placeholder="Enter mail address" name="mail" value="<%= profile.getMailId() %>" required/>--%>
                <%--                </li>--%>
                <li>
                    <label for="current-password">Current Password<sup style="color: red">*</sup>:</label>
                    <input type="password" id="current-password" placeholder="******" name="current-password" required/>
                </li>
                <li>
                    <label for="password">New Password:</label>
                    <input type="password" id="password" placeholder="******" name="password"/>
                </li>
                <li>
                    <label for="confirm-password">Confirm Password:</label>
                    <input type="password" id="confirm-password" placeholder="******" name="confirm-password"/>
                </li>
                <li>
                    <div id="errorDiv" style="display: none; color: red"><%=error%>
                    </div>
                    <button value="submit">Update profile</button>
                </li>
            </ul>
        </form>
        <div class="redirects">
            <a href="${pageContext.request.contextPath}/profile" class="blueBorder"> Back </a>
            <button id="accountDeleteBTN">Delete Account</button>
        </div>
    </div>
    <div id="sideNotification"></div>
</div>
</body>
</html>
