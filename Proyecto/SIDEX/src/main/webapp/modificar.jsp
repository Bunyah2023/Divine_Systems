<%@ page import="mx.edu.utez.sidex.model.User" %><%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 06/08/2024
  Time: 01:36 a. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modificar</title>
    <link rel="stylesheet" href="CSS/registrar.css">
</head>
<body>
<title>SIDEX - Modificacion</title>
<%
    HttpSession sesion = request.getSession();
    User u = (User) sesion.getAttribute("usuario");
    if(u != null) { %>
<div class="register-wrapper">
    <div class="register-container">
        <form method="post" action="modificar">
            <label for="nombres">Nombre(s):</label>
            <input type="text" id="nombres" name="nombres" required value="<%=u.getNombres()%>"><br>

            <label for="apellido">Apellido:</label>
            <input type="text" id="apellido" name="apellido" required value="<%=u.getApellido()%>"><br>

            <label for="apellidoMaterno">Apellido Materno:</label>
            <input type="text" id="apellidoMaterno" name="apellidoMaterno" required value="<%=u.getApellidoMaterno()%>"><br>

            <input type="hidden" id="correo" name="correo" required value="<%=u.getCorreo()%>"><br>

            <label for="contrasena">Crear contraseña:</label>
            <input type="password" id="contrasena" name="contrasena" required value="<%=u.getContra()%>"><br>
            <br>
            <input type="hidden" name="id" value="<%=u.getId()%>" >
            <button type="submit">Registrar</button>
        </form>
    </div>
</div>
<% } %>
<%
    sesion.removeAttribute("usuario");
%>
</body>
</html>