<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>404 - $)$ Not found</title>
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
    <h1> Whatever you're looking for is not found! </h1>
    <p>Click anywhere to go back</p>
    <script>
        document.body.onclick = () => history.back();
    </script>
</div>
</body>
</html>
