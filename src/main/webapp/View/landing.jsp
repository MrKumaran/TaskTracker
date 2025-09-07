<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>Task Tracker</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/landing.css" type="text/css">
  </head>
  <body>
    <div class="container">
        <div id="logoDiv">
          <%@include file="../assets/titleLogo.svg"%>
          <h1>Task Tracker</h1>
        </div>
      <div class="content">
        <h2>Track your tasks, not the tracker </h2>
        <nav>
          <a href="signup" id="signup">Sign up</a>
          <a href="login" id="login">Login</a>
        </nav>
      </div>
    </div>
  </body>
</html>
