<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 31/07/2024
  Time: 04:53 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX - Inicia sesión</title>
    <link rel="stylesheet" href="CSS/login.css">
</head>
<body>
<div class="login-wrapper">
    <div class="login-container">
        <div class="login-left">
            <img src="IMG/LOGO DIVINE SYSTEMS_BLACK.png" alt="Logo de la institución" class="logo">
            <h1>SIDEX</h1>
            <h2>Inicia sesión</h2>
        </div>
        <div class="login-right">
            <form id="login-form">
                <label for="email">Correo institucional</label>
                <input type="email" id="email" placeholder="Ingresa tu correo institucional" required>

                <label for="password">Contraseña</label>
                <input type="password" id="password" placeholder="Ingresa tu contraseña" required>

                <div class="remember-me">
                    <input type="checkbox" id="remember">
                    <label for="remember">Recordarme</label>
                </div>

                <button type="submit" disabled>Iniciar ahora</button>
            </form>
        </div>
    </div>
</div>
<script src="JS/login.js"></script>
</body>
</html>
