<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>500 - %)) error occurred</title>
    <style>
        body {
            width: 100%;
            height: 100%;
        }

        div {
            width: 100%;
            height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background-image: linear-gradient(to right, red, orange, green);
            color: white;
            text-align: center;
        }

        h1 {
            font-size: 2.5em;
        }

        p {
            font-size: 1.2em;
        }
    </style>
</head>
<body>
<div>
    <h1> An error occurred! - my gawwddd... </h1>
    <p>Click anywhere to go home</p>
    <script>
        document.body.onclick = () => window.location.href = "/";
    </script>
</div>
</body>
</html>
