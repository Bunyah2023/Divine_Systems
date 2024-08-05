<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX - Inicia sesión</title>
    <link rel="stylesheet" href="CSS/login.css">
    <style>
        /* Estilo simple para el mensaje de error */
        .error-message {
            color: red;
            font-size: 0.9em;
            margin-bottom: 15px;
        }
    </style>
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
            <form id="login-form" action="login" method="post">
                <label for="email">Correo institucional</label>
                <input type="email" id="email" name="email" placeholder="Ingresa tu correo institucional" required>

                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Ingresa tu contraseña" required>

                <div class="remember-me">
                    <input type="checkbox" id="remember">
                    <label for="remember">Recordarme</label>
                </div>

                <!-- Mensaje de error de inicio de sesión -->
                <%
                    String loginMessage = (String) session.getAttribute("loginMessage");
                    if (loginMessage != null) {
                %>
                <div class="error-message"><%= loginMessage %></div>
                <%
                        session.removeAttribute("loginMessage");
                    }
                %>

                <button type="submit">Iniciar ahora</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
