<%@ page import="in.project.tasktracker.Model.Profile" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Task Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/profile.css" type="text/css">
    <script type="module" src="../Scripts/profile.js" defer></script>
</head>
<body>
<%
    Profile profile = (Profile) request.getAttribute("user");
%>
<div class="container">
    <div class="app-title">
        <a href="${pageContext.request.contextPath}/" target="_self" class="app-title" id="app-title-id">
            <%@include file="../assets/titleLogo.svg" %>
            <h1>Task Tracker</h1>
        </a>
    </div>
    <div id="sideNotification"></div>
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
                <button id="viewPicBtn">View</button>
                <button id="uploadPicBtn">Upload New</button>
                <% if (!(profile.getAvatarURL() == null) && !profile.getAvatarURL().isEmpty()) { %>
                <button id="removePicBtn">Remove</button>
                <% } %>
                <input type="file" id="uploadInput" style="display:none">
            </div>
        </div>
        <div class="userDetails">
            <p>User Name: </p>
            <p id="userName"><%= profile.getUserName() %></p>
            <p>Mail: </p>
            <p id="userMail"><%= profile.getMailId() %></p>
        </div>
        <div class="redirects">
            <a href="${pageContext.request.contextPath}/" id="backHref"> Back </a>
            <a href="logout"> Logout </a>
        </div>
    </div>
</div>
</body>
</html>