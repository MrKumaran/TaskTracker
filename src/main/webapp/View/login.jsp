<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Task Tracker Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/authPages.css" type="text/css">
</head>
<body>
<div class="container">
    <div class="column c1">
                <span>
                    <%@include file="../assets/titleLogo.svg" %>
                    <h1>Task Tracker</h1>
                </span>
    </div>
    <div class="column c2">
        <h2>Login</h2>
        <form id="authForm" action="login" method="post">
            <ul>
                <li>
                    <label for="mail">Email:</label>
                    <input type="email" placeholder="Enter mail address" id="mail" name="mail" required/>
                </li>
                <li>
                    <label for="password">Password:</label>
                    <input type="password" placeholder="******" id="password" name="password" required/>
                </li>
                <li>
                    <button value="submit">login up</button>
                </li>
            </ul>
        </form>
        <a href="signup"> Don't have an account? </a>
    </div>
</div>
</body>
</html>
