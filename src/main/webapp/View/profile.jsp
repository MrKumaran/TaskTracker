<%@ page import="in.project.tasktracker.Model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Task Tracker</title>
</head>
<body>
<%
  User user = (User) request.getAttribute("user");
%>
<h1>UUID: <%= user.getUserId() %></h1>
<p>Mail: <%= user.getMail() %></p>
<p>Password: <%= user.getPassword() %></p>
<p>salt: <%= user.getSalt() %></p>
<p>UserName: <%= user.getUserName() %></p>
<p>Avatar URL: <%= user.getAvatarURL() %></p>
<p>Session id: <%= session.getId() %></p>
</body>
</html>