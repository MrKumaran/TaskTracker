<%@ page import="in.project.tasktracker.Model.Profile" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Task Tracker</title>
</head>
<body>
<%
  Profile profile = (Profile) request.getAttribute("user");
%>
<h1>UUID: <%= profile.getUserId() %></h1>
<p>Mail: <%= profile.getMailId() %></p>
<p>UserName: <%= profile.getUserName() %></p>
<p>Avatar URL: <%= profile.getAvatarURL() %></p>
<p>Session id: <%= session.getId() %></p>
</body>
</html>