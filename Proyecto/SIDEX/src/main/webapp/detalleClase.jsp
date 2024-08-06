<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mx.edu.utez.sidex.model.User, mx.edu.utez.sidex.dao.ClaseDao, mx.edu.utez.sidex.model.Clase, mx.edu.utez.sidex.dao.ExamenDao, mx.edu.utez.sidex.model.Examen, mx.edu.utez.sidex.model.Pregunta, mx.edu.utez.sidex.dao.PreguntaDao, java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalles de la Clase - SIDEX</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/index.css">
    <link rel="stylesheet" href="CSS/detallaClase.css">
    <script src="JS/index.js" defer></script>
</head>
<body class="froid">
<header>
    <div id="hamburger-menu">☰</div>
    <div id="sidebar" class="sidebar">
        <ul>
            <li><a href="index.jsp">Mis cursos</a></li>
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
        <span id="add-class-btn-span" class="icon">+</span>
        <div class="fotoPerfil">
            <% String perfilLink = (session.getAttribute("user") != null) ? "configurar_perfil.jsp" : "login.jsp"; %>
            <a href="<%= perfilLink %>">
                <img src="IMG/PICTURE_PROFILE_DEFAULT.png" alt="Foto de perfil" class="user-info">
            </a>
        </div>
    </div>
</header>

<div class="main-container">
    <aside class="class-list">
        <!-- Lista de clases generada dinámicamente -->
        <ul>
            <%
                User usuario = (User) session.getAttribute("user");
                ClaseDao claseDao = new ClaseDao();
                List<Clase> clases = null;
                if (usuario.getRolId() == 1) { // Suponiendo que rolId 1 es estudiante
                    clases = claseDao.obtenerClasesPorEstudiante(usuario.getId());
                } else if (usuario.getRolId() == 2) { // Suponiendo que rolId 2 es docente
                    clases = claseDao.obtenerClasesPorCreador(usuario.getCorreo());
                }
                for (Clase claseItem : clases) {
                    out.println("<li><a href='detalleClase.jsp?claseId=" + claseItem.getId() + "'>" + claseItem.getNombre() + "</a></li>");
                }
            %>
        </ul>
    </aside>
    <main>
        <div class="container">
            <%
                String claseId = request.getParameter("claseId");
                Clase clase = claseDao.obtenerClasePorId(Integer.parseInt(claseId));
                ExamenDao examenDao = new ExamenDao();
                List<Examen> examenesPendientes = examenDao.obtenerExamenesPorClaseYEstado(Integer.parseInt(claseId), false); // false para pendientes
                List<Examen> examenesCompletados = examenDao.obtenerExamenesPorClaseYEstado(Integer.parseInt(claseId), true); // true para completados
            %>
            <div class="class-details">
                <h1><%= clase.getNombre() %></h1>
                <p><%= clase.getDescripcion() %></p>
                <div class="class-info">
                    <p>Fecha de inicio: <%= clase.getFechaInicio().toString() %></p>
                    <p>Fecha de fin: <%= clase.getFechaFin().toString() %></p>
                    <p>Código: <%= clase.getCodigo() %></p>
                </div>
            </div>

            <% if (usuario.getRolId() == 1) { %> <!-- Estudiante -->
            <div class="exam-section">
                <h2>Exámenes pendientes</h2>
                <div class="exam-list">
                    <% for (Examen examen : examenesPendientes) { %>
                    <div class="exam">
                        <p><%= examen.getTitulo() %> - <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(examen.getFechaApertura()) %></p>
                        <button class="btn btn-primary" onclick="window.location.href='examen.jsp?examenId=<%= examen.getId() %>'">Tomar examen</button>
                    </div>
                    <% } %>
                </div>
                <h2>Exámenes completados</h2>
                <div class="exam-results">
                    <% for (Examen examen : examenesCompletados) { %>
                    <div class="exam">
                        <p><%= examen.getTitulo() %> - <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(examen.getFechaCierre()) %></p>
                        <span class="exam-score">Calificación: <%= examen.getCalificacion() %></span>
                    </div>
                    <% } %>
                </div>
            </div>
            <% } else if (usuario.getRolId() == 2) { %> <!-- Docente -->
            <div class="exam-section">
                <h2>Gestionar Exámenes</h2>
                <button id="create-exam-btn" class="btn btn-success">Crear Examen</button>
                <h3>Exámenes por activar</h3>
                <div class="exam-list">
                    <% for (Examen examen : examenesPendientes) { %>
                    <div class="exam">
                        <p><%= examen.getTitulo() %> - <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(examen.getFechaApertura()) %></p>
                        <!-- Cambiar para redirigir al JSP examen.jsp -->
                        <button class="btn btn-secondary" onclick="window.location.href='examen.jsp?examenId=<%= examen.getId() %>'">Editar</button>
                        <button class="btn btn-danger">Eliminar</button>
                    </div>
                    <% } %>
                </div>
                <h3>Exámenes activados</h3>
                <div class="exam-results">
                    <% for (Examen examen : examenesCompletados) { %>
                    <div class="exam">
                        <p><%= examen.getTitulo() %> - <%= new java.text.SimpleDateFormat("dd/MM/yyyy").format(examen.getFechaCierre()) %></p>
                        <button class="btn btn-warning">Cerrar</button>
                    </div>
                    <% } %>
                </div>
            </div>

            <!-- Ventana Modal para Crear/Editar Examen -->
            <div id="modal-exam" class="modal">
                <div class="modal-content">
                    <span class="close">&times;</span>
                    <h2 id="modal-title">Crear Nuevo Examen</h2>
                    <form id="create-edit-exam-form" action="crearExamen" method="post">
                        <input type="hidden" name="claseId" value="<%= claseId %>">
                        <input type="hidden" id="examen-id" name="examenId">
                        <label for="exam-title">Título del examen:</label>
                        <input type="text" id="exam-title" name="examTitle">

                        <label for="exam-description">Descripción:</label>
                        <textarea id="exam-description" name="examDescription"></textarea>

                        <label for="start-date">Fecha de apertura:</label>
                        <input type="date" id="start-date" name="startDate">

                        <label for="end-date">Fecha de cierre:</label>
                        <input type="date" id="end-date" name="endDate">

                        <h3>Preguntas</h3>
                        <div class="questions" id="questions-container">
                            <!-- Las preguntas se agregarán aquí dinámicamente -->
                        </div>

                        <div class="add-question-circle" id="add-question-btn">+</div>

                        <button type="submit" class="btn btn-success">Guardar Examen</button>
                        <button type="button" class="btn btn-danger" id="cancel-exam-btn">Cancelar</button>
                    </form>
                </div>
            </div>

            <script>
                document.addEventListener('DOMContentLoaded', function () {
                    const modalExam = document.getElementById('modal-exam');
                    const createExamBtn = document.getElementById('create-exam-btn');
                    const closeExamModal = document.getElementsByClassName('close')[0];
                    const addQuestionBtn = document.getElementById('add-question-btn');
                    const questionsContainer = document.getElementById('questions-container');
                    let questionCount = 0;

                    createExamBtn.onclick = function () {
                        document.getElementById('modal-title').textContent = "Crear Nuevo Examen";
                        document.getElementById('create-edit-exam-form').reset();
                        questionsContainer.innerHTML = ''; // Resetear preguntas
                        questionCount = 0;
                        modalExam.style.display = 'block';
                    }

                    closeExamModal.onclick = function () {
                        modalExam.style.display = 'none';
                    }

                    window.onclick = function (event) {
                        if (event.target == modalExam) {
                            modalExam.style.display = 'none';
                        }
                    }

                    addQuestionBtn.onclick = function () {
                        if (questionCount < 30) {
                            questionCount++;
                            const questionDiv = document.createElement('div');
                            questionDiv.classList.add('question');
                            questionDiv.innerHTML = `
                                <label>Pregunta ${questionCount}:</label>
                                <input type="text" name="pregunta${questionCount}">
                                <label>Opciones:</label>
                                <input type="text" name="opcion${questionCount}1" placeholder="Opción 1">
                                <input type="text" name="opcion${questionCount}2" placeholder="Opción 2">
                                <input type="text" name="opcion${questionCount}3" placeholder="Opción 3">
                                <input type="text" name="opcion${questionCount}4" placeholder="Opción 4">
                                <label>Respuesta correcta:</label>
                                <select name="respuestaCorrecta${questionCount}">
                                    <option value="1">Opción 1</option>
                                    <option value="2">Opción 2</option>
                                    <option value="3">Opción 3</option>
                                    <option value="4">Opción 4</option>
                                </select>
                            `;
                            questionsContainer.appendChild(questionDiv);
                        } else {
                            alert("Se ha alcanzado el límite máximo de 30 preguntas.");
                        }
                    }
                });

                function editarExamen(examenId) {
                    window.location.href = 'examen.jsp?examenId=' + examenId;
                }
            </script>
            <% } %>
        </div>
    </main>
</div>

</body>
</html>
