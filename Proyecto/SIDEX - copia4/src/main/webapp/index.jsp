<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mx.edu.utez.sidex.model.User, mx.edu.utez.sidex.dao.ClaseDao, mx.edu.utez.sidex.model.Clase" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX - Panel Principal</title>

    <link rel="stylesheet" href="CSS/index.css">
    <link rel="stylesheet" href="CSS/clases.css">
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <script src="JS/index.js" defer></script>
</head>
<body class="froid">
<header>
    <div id="hamburger-menu">☰</div>
    <div id="sidebar" class="sidebar">
        <ul>
            <li><a href="#">Mis cursos</a></li>
            <li><a href="#">Pendientes</a></li>
            <li><a href="#">Exámenes</a></li>
            <li><a href="#">Más...</a></li>
        </ul>
    </div>
    <div class="logo">
        <img id="logoDIVINESYSTEMS" src="IMG/LOGO SIDEX by DIVINE SYSTEMS.png" alt="DIVINE SYSTEMS" onclick="location.href='index.jsp'">
    </div>
    <nav>
        <ul>
            <li><a href="#">Mis Cursos</a></li>
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
        <%
            User user = (User) session.getAttribute("user");
            if (user != null && user.isEstado() && user.getRolId() == 1) { // Verifica si el usuario es un estudiante activo
        %>
        <button id="add-class-btn-span" class="icon">+</button>
        <% } %>
        <div class="fotoPerfil">
            <%
                String perfilLink = (user != null) ? "configurar_perfil.jsp" : "login.jsp";
            %>
            <a href="<%= perfilLink %>">
                <img src="IMG/PICTURE_PROFILE_DEFAULT.png" alt="Foto de perfil" class="user-info">
            </a>
        </div>
        <% if (user != null) { %>
        <!-- Botón de cerrar sesión -->
        <a href="login.jsp" class="btn btn-primary" style="display:inline;">Cerrar sesión</a>
        <% } %>
    </div>
</header>

<main>
    <div class="notifications">
        <%
            if (user != null && user.isEstado() && user.getRolId() == 1) { // Verifica si el usuario es activo y es estudiante
                String nombre = user.getNombres();
                String primerNombre = nombre.contains(" ") ? nombre.substring(0, nombre.indexOf(" ")) : nombre;
        %>
        <div class="alert" id="netacad-maintenance">Bienvenido a SIDEX, <%= primerNombre %></div>
        <div class="alert" id="skillsforall-maintenance">¡Un buen estudiante no descuida sus deberes!</div>
        <div class="alert" id="current-date"></div>

        <%
            String message = (String) session.getAttribute("message");
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (message != null) {
        %>
        <div class="alert alert-success"><%= message %></div>
        <%
                session.removeAttribute("message");
            }
            if (errorMessage != null) {
        %>
        <div class="alert alert-danger"><%= errorMessage %></div>
        <%
                session.removeAttribute("errorMessage");
            }
        %>
    </div>
    <section class="clases-estudiante">
        <h2 id="tus-clases">Tus Clases</h2>
        <div class="cursos">
            <%
                ClaseDao claseDao = new ClaseDao();
                List<Clase> clases = claseDao.obtenerClasesPorEstudiante(user.getId());

                for (Clase clase : clases) {
            %>
            <div class="curso" onclick="location.href='detalleClase.jsp?claseId=<%= clase.getId() %>'">
                <div class="curso-header" style="background-color: #3498db;">
                    <h3><%= clase.getNombre() %></h3>
                </div>
                <div class="curso-details">
                    <p>Descripción: <%= clase.getDescripcion() %></p>
                    <p>Código: <%= clase.getCodigo() %></p>
                    <p>Fecha de inicio: <%= clase.getFechaInicio() %></p>
                    <p>Fecha de fin: <%= clase.getFechaFin() %></p>
                </div>
            </div>
            <%
                }
            %>
        </div>
    </section>
    <%
    } else {
    %>
    <div class="alert alert-warning">Por favor, inicia sesión para ver tus clases o verifica tu estado como usuario activo.</div>
    <%
        }
    %>
    </div>
</main>

<!-- Ventana Modal para Unirse a Clase -->
<div id="modal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>Agregar Clase</h2>
        <form id="join-class-form" method="post" action="unirseClase">
            <input type="text" name="classCode" id="class-code" placeholder="Ingrese el código de la clase" required>
            <button type="submit" class="btn btn-success">Aceptar</button>
            <button type="button" class="btn btn-danger" id="cancel-btn">Cancelar</button>
        </form>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const modal = document.getElementById('modal');
        const btn = document.getElementById('add-class-btn-span');
        const span = document.getElementsByClassName('close')[0];

        btn.onclick = function () {
            modal.style.display = 'block';
        };
        span.onclick = function () {
            modal.style.display = 'none';
        };
        window.onclick = function (event) {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        };
    });
</script>
</body>
</html>

