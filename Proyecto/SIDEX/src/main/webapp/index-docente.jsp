<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>
<%@ page import="mx.edu.utez.sidex.dao.ClaseDao" %>
<%@ page import="mx.edu.utez.sidex.model.Clase" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX - Docente</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/index.css">
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
        <img id="logoDIVINESYSTEMS" src="IMG/LOGO SIDEX by DIVINE SYSTEMS.png" alt="DIVINE SYSTEMS" onclick="location.href='index-docente.jsp'">
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
        <div class="alert" id="netacad-maintenance">
            <%
                // Obtener el usuario de la sesión
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    response.sendRedirect("login.jsp");
                    return;
                }

                String nombre = user.getNombres();
                String primerNombre = nombre.contains(" ") ? nombre.substring(0, nombre.indexOf(" ")) : nombre;
                out.print("Bienvenido a SIDEX, " + primerNombre);
            %>
        </div>
        <div class="alert" id="skillsforall-maintenance">Crea y gestiona tus clases y exámenes aquí.</div>
        <div class="alert" id="current-date"></div>
    </div>
    <section class="teaching">
        <h1>Mis clases...</h1>
        <div class="clases">
            <%
                ClaseDao claseDao = new ClaseDao();
                List<Clase> clases = claseDao.obtenerClasesPorCreador(user.getCorreo());

                if (clases.isEmpty()) {
            %>
            <p>Actualmente, no hay clases disponibles. Por favor, crea una nueva clase.</p>
            <%
            } else {
                for (Clase clase : clases) {
            %>
            <div class="classroom-block" onclick="location.href='detalleClase.jsp?claseId=<%= clase.getId() %>&role=docente'">
                <h3><%= clase.getNombre() %></h3>
                <p><%= clase.getDescripcion() %></p>
                <p>Código: <%= clase.getCodigo() %></p>
            </div>
            <%
                    }
                }
            %>
        </div>
        <button id="create-class-btn">Crear Clase</button>
    </section>
</main>

<!-- Ventana Modal para Crear Clase -->
<div id="modal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>CONFIGURA TU CLASE</h2>
        <form id="create-class-form" action="crearClase" method="post">
            <label for="class-name">Nombre de la clase:</label>
            <input type="text" id="class-name" name="className" placeholder="Nombre de la clase" required>

            <label for="class-description">Descripción de clase:</label>
            <textarea id="class-description" name="description" placeholder="Descripción de la clase" required></textarea>

            <label>Período de expiración de clase:</label>
            <input type="date" id="start-date" name="startDate" required>
            <input type="date" id="end-date" name="endDate" required>

            <label>Calificaciones:</label>
            <div class="grades">
                <div class="grade">
                    <span>AU (Excelente)</span>
                    <input type="number" name="minAU" placeholder="Min" step="0.1" min="0" max="10" required>
                    <input type="number" name="maxAU" placeholder="Max" step="0.1" min="0" max="10" required>
                </div>
                <div class="grade">
                    <span>DE (Bueno)</span>
                    <input type="number" name="minDE" placeholder="Min" step="0.1" min="0" max="10" required>
                    <input type="number" name="maxDE" placeholder="Max" step="0.1" min="0" max="10" required>
                </div>
                <div class="grade">
                    <span>SA (Satisfactorio)</span>
                    <input type="number" name="minSA" placeholder="Min" step="0.1" min="0" max="10" required>
                    <input type="number" name="maxSA" placeholder="Max" step="0.1" min="0" max="10" required>
                </div>
                <div class="grade">
                    <span>NA (Insuficiente)</span>
                    <input type="number" name="minNA" placeholder="Min" step="0.1" min="0" max="10" required>
                    <input type="number" name="maxNA" placeholder="Max" step="0.1" min="0" max="10" required>
                </div>
            </div>

            <button type="submit" class="btn btn-success">Crear ahora</button>
            <button type="button" class="btn btn-danger" id="cancel-btn">Cancelar</button>
        </form>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Abrir y cerrar el modal
        const modal = document.getElementById('modal');
        const btn = document.getElementById('create-class-btn');
        const span = document.getElementsByClassName('close')[0];

        btn.onclick = function () {
            modal.style.display = 'block';
        }

        span.onclick = function () {
            modal.style.display = 'none';
        }

        window.onclick = function (event) {
            if (event.target == modal) {
                modal.style.display = 'none';
            }
        }
    });
</script>

</body>
</html>
