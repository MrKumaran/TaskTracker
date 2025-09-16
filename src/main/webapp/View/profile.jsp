<%@ page import="in.project.tasktracker.Model.Profile" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Task Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/profile.css" type="text/css">
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
    <div class="body">
        <div class="userAvatarDiv">
            <% if (profile.getAvatarURL() == null || profile.getAvatarURL().isEmpty()) { %>
            <%@include file="../assets/userAvatar.svg" %>
            <% } else { %>
            <img src="<%=profile.getAvatarURL()%>" alt="">
            <% } %>
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