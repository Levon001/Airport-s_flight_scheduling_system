<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title> Sign Up </title>
    <link rel="stylesheet" href="style/registerPage.css">
</head>
<body>

<%
    String msg = (String) request.getAttribute("msg");
%>

<form action="/register" method="post" id="register-form">
    <h1>Register</h1>

    <label for="username">Username</label>
    <input type="text" id="username" name="name" placeholder="Username" required>

    <label for="surname">Surname</label>
    <input type="text" id="surname" name="surname" placeholder="Surname" required>

    <label for="email">Email</label>
    <input type="email" id="email" name="email" placeholder="E-mail" required>

    <label for="password">Password</label>
    <input type="password" id="password" name="password" placeholder="Password" required>
    <br>
    <% if (msg != null) {%>
    <span style="color: red"><%=msg%> </span>
    <%}%>
    <input type="submit" value="Register" id="sub">
    <p id="p"><a href="/" class="register-link">&laquo; Previous</a></p>
</form>

<br/>
</body>
</html>
