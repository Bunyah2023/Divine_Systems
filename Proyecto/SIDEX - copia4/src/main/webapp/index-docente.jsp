<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>
<%@ page import="mx.edu.utez.sidex.dao.ClaseDao" %>
<%@ page import="mx.edu.utez.sidex.model.Clase" %>
<%@ page import="java.util.List" %>

<%
    // Verificación de acceso para usuarios con rol de docente y que estén activos
    User user = (User) session.getAttribute("user");
    if (user == null || user.getRolId() != 2 || !user.isEstado()) { // Suponiendo que rolId 2 es para docentes
        response.sendRedirect("acceso_denegado.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX - Docente</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/index.css">
    <style>
        /* Estilo para el botón de Crear Clase */
        #create-class-btn {
            background-color: #4CAF50; /* Verde */
            border: none;
            color: white;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 10px 0;
            cursor: pointer;
            border-radius: 4px;
            transition: background-color 0.3s ease;
        }

        #create-class-btn:hover {
            background-color: #45a049; /* Verde oscuro al pasar el cursor */
        }

        /* Estilo para el modal */
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.5); /* Fondo negro con opacidad */
        }

        .modal-content {
            background-color: #fefefe;
            margin: 10% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 50%;
            border-radius: 8px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
            cursor: pointer;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }

        /* Estilo para los bloques de clases */
        .classroom-block {
            background-color: #ffffff;
            border: 1px solid #ddd;
            padding: 15px;
            margin: 10px 0;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            cursor: pointer;
        }

        .classroom-block:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        }

        .classroom-block h3 {
            margin-top: 0;
            color: #333;
        }

        .classroom-block p {
            color: #777;
        }

        .grades input[type="number"] {
            width: 48%;
            padding: 5px;
            margin: 5px 1%;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
    </style>
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
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        }
    });
</script>

</body>
</html>
