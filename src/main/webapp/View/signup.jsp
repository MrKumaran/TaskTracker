<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Task Tracker boarding</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/authPages.css" type="text/css">
</head>
<body>
<div class="container">
    <div class="column c1">
            <span>
                <%@include file="../assets/Titlelogo.svg" %>
                <h1>Task Tracker</h1>
            </span>
    </div>
    <div class="column c2">
        <h2>Create Account</h2>
        <form id="authForm" action="signup" method="post">
            <ul>
                <li>
                    <label for="userName">User Name:</label>
                    <input type="text" id="userName" placeholder="Enter user name" name="userName" required/>
                </li>
                <li>
                    <label for="mail">Email:</label>
                    <input type="email" id="mail" placeholder="Enter mail address" name="mail" required/>
                </li>
                <li>
                    <label for="password">Password:</label>
                    <input type="password" id="password" placeholder="******" name="password" required/>
                </li>
                <li>
                    <label for="confirm-password">Confirm Password:</label>
                    <input type="password" id="confirm-password" placeholder="******" name="confirm-password" required/>
                </li>
                <li>
                    <button value="submit">Create Account</button>
                </li>
            </ul>
        </form>
        <a href="login"> Have an account? </a>
    </div>
</div>
</body>
</html>
