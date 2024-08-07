<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 31/07/2024
  Time: 05:09 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX - Configuración de Perfil</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/configurar_perfil.css">
</head>
<body class="froid">
<header>
    <div id="hamburger-menu" class="hamburger-menu">
        <div class="bar"></div>
        <div class="bar"></div>
        <div class="bar"></div>
    </div>
    <div class="logo">
        <img src="IMG/DIVINE-SYSTEMS-LOGO.png" alt="DIVINE SYSTEMS">
        <span> SIDEX </span>
    </div>
    <nav>
        <ul>
            <li><a href="index.jsp">Mis Cursos</a></li>
            <li><a href="index.jsp">Tareas</a></li>
            <li><a href="index.jsp">Cursos</a></li>
            <li><a href="index.jsp">Carreras</a></li>
            <li><a href="index.jsp">Más</a></li>
        </ul>
    </nav>
    <div class="right-section">
        <span class="icon">🔍</span>
        <span class="icon">❓</span>
        <span class="icon">🔔</span>
        <span id="add-class-btn-span" class="icon">+</span>
        <div class="fotoPerfil">
            <a href="configurar_perfil.jsp">
                <img src="IMG/PICTURE_PROFILE_DEFAULT.png" alt="Foto de perfil" class="user-info">
            </a>
        </div>
    </div>
</header>
<main>
    <div class="notifications">
        <div class="alert" id="netacad-maintenance">Bienvenido a SIDEX</div>
        <div class="alert" id="skillsforall-maintenance">¡Un buen estudiante no descuida sus deberes!</div>
        <div class="alert" id="current-date"></div>
    </div>
    <h1>Configuración de Perfil</h1>
    <div class="profile-settings">
        <div class="profile-photo">
            <h2>Foto de Perfil</h2>
            <img src="IMG/PICTURE_PROFILE_DEFAULT.png" alt="Foto de perfil" id="profile-pic">
            <input type="file" id="upload-photo" accept="image/*">
        </div>
        <div class="notifications-settings">
            <h2>Notificaciones</h2>
            <label>
                <input type="checkbox" id="email-notifications"> Notificaciones por correo
            </label>
        </div>
        <div class="password-settings">
            <h2>Cambiar Contraseña</h2>
            <input type="password" id="current-password" placeholder="Contraseña Actual">
            <input type="password" id="new-password" placeholder="Nueva Contraseña">
            <input type="password" id="confirm-password" placeholder="Confirmar Nueva Contraseña">
            <button id="change-password-btn">Cambiar Contraseña</button>
        </div>
        <div class="language-settings">
            <h2>Preferencias de Idioma</h2>
            <select id="language-select">
                <option value="es">Español</option>
                <option value="en">Inglés</option>
                <option value="fr">Francés</option>
                <option value="de">Alemán</option>
            </select>
        </div>
        <div class="visibility-settings">
            <h2>Visibilidad del Perfil</h2>
            <label>
                <input type="radio" name="visibility" value="public"> Público
            </label>
            <label>
                <input type="radio" name="visibility" value="private"> Privado
            </label>
        </div>
        <button id="save-changes-btn">Guardar Cambios</button>
    </div>
</main>
<div id="modal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>Agregar Clase</h2>
        <input type="text" id="class-code" placeholder="Ingrese el código de la clase">
        <button id="accept-btn">Aceptar</button>
        <button id="cancel-btn">Cancelar</button>
    </div>
</div>
<script src="JS/configurar_perfil.js"></script>
</body>
</html>
