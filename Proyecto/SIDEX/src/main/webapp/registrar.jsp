<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX - Registro</title>
    <link rel="stylesheet" href="CSS/registrar.css">
    <script>
        // Validación de contraseñas en el lado del cliente
        function validarContrasenas() {
            const contrasena = document.getElementById('contrasena').value;
            const repetirContrasena = document.getElementById('repetirContrasena').value;
            if (contrasena !== repetirContrasena) {
                alert("Las contraseñas no coinciden. Por favor, verifica e inténtalo de nuevo.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<div class="register-wrapper">
    <div class="register-container">
        <h1>SIDEX - Registro</h1>
        <form id="registroForm" action="registrar" method="post" onsubmit="return validarContrasenas()">
            <div class="form-group">
                <label for="nombres">Nombre(s):</label>
                <input type="text" id="nombres" name="nombres" required>
            </div>

            <div class="form-group">
                <label for="apellido">Apellido:</label>
                <input type="text" id="apellido" name="apellido" required>
            </div>

            <div class="form-group">
                <label for="apellidoMaterno">Apellido Materno:</label>
                <input type="text" id="apellidoMaterno" name="apellidoMaterno" required>
            </div>

            <div class="form-group">
                <label for="correo">Correo institucional:</label>
                <input type="email" id="correo" name="correo" required>
            </div>

            <div class="form-group">
                <label for="contrasena">Crear contraseña:</label>
                <input type="password" id="contrasena" name="contrasena" required>
            </div>

            <div class="form-group">
                <label for="repetirContrasena">Repetir contraseña:</label>
                <input type="password" id="repetirContrasena" name="repetirContrasena" required>
            </div>

            <!-- Mensajes de error y éxito -->
            <%
                String registerMessage = (String) session.getAttribute("registerMessage");
                if (registerMessage != null) {
                    String messageType = (String) session.getAttribute("messageType");
            %>
            <div class="message">
                    <span style="color: <%= "error".equals(messageType) ? "red" : "green" %>;">
                        <%= registerMessage %>
                    </span>
            </div>
            <%
                    session.removeAttribute("registerMessage");
                    session.removeAttribute("messageType");
                }
            %>

            <button type="submit">Registrar</button>
        </form>
    </div>
</div>
</body>
</html>
