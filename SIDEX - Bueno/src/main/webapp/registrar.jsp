<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 31/07/2024
  Time: 04:58 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/registrar.css">
</head>
<body>
<div class="titulo">
    <h1>Registrarse</h1>
</div>
<form id="registroForm" action="registrar" method="post">
    <label for="nombres">Nombre(s):</label>
    <input type="text" id="nombres" name="nombres" required><br>

    <label for="apellido">Apellido:</label>
    <input type="text" id="apellido" name="apellido" required><br>

    <label for="apellidoMaterno">Apellido Materno:</label>
    <input type="text" id="apellidoMaterno" name="apellidoMaterno" required><br>

    <label for="correo">Correo institucional:</label>
    <input type="email" id="correo" name="correo" required><br>

    <label for="contrasena">Crear contraseña:</label>
    <input type="password" id="contrasena" name="contrasena" required><br>

    <label for="repetirContrasena">Repetir contraseña:</label>
    <input type="password" id="repetirContrasena" name="repetirContrasena" required><br>

    <button type="submit">Registrar</button>
</form>
</body>
</html>