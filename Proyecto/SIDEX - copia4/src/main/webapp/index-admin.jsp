<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX - Administrador</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/index.css">
</head>
<body class="froid">
<header>
    <div id="hamburger-menu">☰</div>
    <div id="sidebar" class="sidebar">
        <ul>
            <li><a href="verregistro.jsp">Gestionar Usuarios</a></li>
            <li><a href="#">Pendientes</a></li>
            <li><a href="#">Exámenes</a></li>
            <li><a href="#">Más...</a></li>
        </ul>
    </div>

    <div class="logo">
        <img id="logoDIVINESYSTEMS" src="IMG/LOGO SIDEX by DIVINE SYSTEMS.png" alt="DIVINE SYSTEMS">
    </div>
    <nav>
        <ul>
            <li><a href="verregistro.jsp">Gestionar Usuarios</a></li>
            <li><a href="#">Tareas</a></li>
            <li><a href="#">Cursos</a></li>
            <li><a href="#">Carreras</a></li>
            <li><a href="#">Más</a></li>
        </ul>
    </nav>
    <div class="right-section">
        <span class="icon">🔍</span>
        <span class="icon">❓</span>
        <span class="icon">🔔</span>
        <div class="fotoPerfil">
            <a href="configurar_perfil.jsp">
                <img src="IMG/PICTURE_PROFILE_DEFAULT.png" alt="Foto de perfil" class="user-info">
            </a>
        </div>
    </div>
</header>

<main>
    <div class="notifications">
        <div class="alert" id="netacad-maintenance">
            <%
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    String nombre = user.getNombres();
                    String primerNombre = nombre.contains(" ") ? nombre.substring(0, nombre.indexOf(" ")) : nombre;
                    out.print("Bienvenido a SIDEX, " + primerNombre);
                } else {
                    out.print("Bienvenido a SIDEX");
                }
            %>
        </div>
        <div class="alert" id="skillsforall-maintenance">Gestiona usuarios y sus roles aquí.</div>
        <div class="alert" id="current-date"></div>
    </div>
    <section class="management">
        <h1>Gestión de Usuarios...</h1>
        <p>Aquí puedes gestionar los usuarios del sistema.</p>
        <a href="verregistro.jsp" class="btn btn-primary">Ir a Gestión de Usuarios</a>
    </section>
</main>
<script src="JS/index.js"></script>
</body>
</html>
