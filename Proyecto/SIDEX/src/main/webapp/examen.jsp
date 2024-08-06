<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.sidex.model.User, mx.edu.utez.sidex.dao.ExamenDao, mx.edu.utez.sidex.model.Examen, mx.edu.utez.sidex.model.Pregunta, mx.edu.utez.sidex.model.Resultado, mx.edu.utez.sidex.dao.PreguntaDao, java.util.List" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Examen - SIDEX</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/examen.css">
    <script src="JS/examen.js" defer></script>
    <style>
        /* Estilos para la ventana modal */
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0, 0, 0, 0.4);
            justify-content: center;
            align-items: center;
        }

        .modal-content {
            background-color: #fefefe;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            max-width: 400px;
            margin: auto;
            text-align: center;
            border-radius: 8px;
        }

        .modal-content p {
            font-size: 18px;
        }

        .question.correct {
            background-color: #c8e6c9; /* Verde pastel para correcto */
        }

        .question.incorrect {
            background-color: #ffcdd2; /* Rojo pastel para incorrecto */
        }
    </style>
</head>
<body>
<header>
    <div id="hamburger-menu">☰</div>
    <div class="logo">
        <img id="logoDIVINESYSTEMS" src="IMG/LOGO SIDEX by DIVINE SYSTEMS.png" alt="DIVINE SYSTEMS" onclick="location.href='index.jsp'">
    </div>

    <div class="right-section">
        <span class="icon">🔍</span>
        <span class="icon">❓</span>
        <span class="icon">🔔</span>
        <div class="fotoPerfil">
            <% String perfilLink = (session.getAttribute("user") != null) ? "configurar_perfil.jsp" : "login.jsp"; %>
            <a href="<%= perfilLink %>">
                <img src="IMG/PICTURE_PROFILE_DEFAULT.png" alt="Foto de perfil" class="user-info">
            </a>
        </div>
    </div>
</header>

<div class="main-container">
    <%
        String examenIdStr = request.getParameter("examenId");
        int examenId = examenIdStr != null ? Integer.parseInt(examenIdStr) : 0;
        ExamenDao examenDao = new ExamenDao();
        PreguntaDao preguntaDao = new PreguntaDao();
        Examen examen = examenDao.obtenerPorId(examenId);
        List<Pregunta> preguntas = preguntaDao.obtenerPreguntasPorExamenId(examenId);

        User usuario = (User) session.getAttribute("user");
    %>

    <div class="container">
        <% if (usuario.getRolId() == 1) { %> <!-- Estudiante -->
        <div class="exam-student">
            <h2><%= examen.getTitulo() %></h2>
            <p><%= examen.getDescripcion() %></p>
            <form id="exam-form" action="entregarExamen" method="post" onsubmit="return handleSubmit()">
                <input type="hidden" name="examenId" value="<%= examenId %>">
                <div class="questions">
                    <% for (Pregunta pregunta : preguntas) { %>
                    <div class="question" id="pregunta<%= pregunta.getId() %>">
                        <p><%= pregunta.getTexto() %></p>
                        <% for (int i = 1; i <= 4; i++) { %>
                        <div>
                            <input type="radio" name="respuesta<%= pregunta.getId() %>" value="<%= i %>" id="respuesta<%= pregunta.getId() %>_<%= i %>">
                            <label for="respuesta<%= pregunta.getId() %>_<%= i %>">
                                <%
                                    String opcion = (String) Pregunta.class.getMethod("getOpcion" + i).invoke(pregunta);
                                    out.print(opcion);
                                %>
                            </label>
                        </div>
                        <% } %>
                    </div>
                    <% } %>
                </div>
                <button type="submit" class="btn btn-primary">Enviar</button>
            </form>
        </div>
        <% } else if (usuario.getRolId() == 2) { %> <!-- Docente -->
        <div class="exam-teacher">
            <h2>Editar Examen</h2>
            <form id="edit-exam-form" action="crearExamen" method="post">
                <input type="hidden" name="examenId" value="<%= examenId %>">
                <label for="exam-title">Título del examen:</label>
                <input type="text" id="exam-title" name="examTitle" value="<%= examen.getTitulo() %>">

                <label for="exam-description">Descripción:</label>
                <textarea id="exam-description" name="examDescription"><%= examen.getDescripcion() %></textarea>

                <label for="start-date">Fecha de apertura:</label>
                <input type="date" id="start-date" name="startDate" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(examen.getFechaApertura()) %>">

                <label for="end-date">Fecha de cierre:</label>
                <input type="date" id="end-date" name="endDate" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(examen.getFechaCierre()) %>">

                <h3>Preguntas</h3>
                <div class="questions" id="questions-container">
                    <% for (Pregunta pregunta : preguntas) { %>
                    <div class="question">
                        <label>Pregunta:</label>
                        <input type="text" name="pregunta<%= pregunta.getId() %>" value="<%= pregunta.getTexto() %>">
                        <label>Opciones:</label>
                        <% for (int i = 1; i <= 4; i++) { %>
                        <input type="text" name="opcion<%= pregunta.getId() %><%= i %>" value="<%= pregunta.getClass().getMethod("getOpcion" + i).invoke(pregunta) %>" placeholder="Opción <%= i %>">
                        <% } %>
                        <label>Respuesta correcta:</label>
                        <select name="respuestaCorrecta<%= pregunta.getId() %>">
                            <% for (int i = 1; i <= 4; i++) { %>
                            <option value="<%= i %>" <%= pregunta.getRespuestaCorrecta() == i ? "selected" : "" %>>Opción <%= i %></option>
                            <% } %>
                        </select>
                    </div>
                    <% } %>
                </div>
                <button type="submit" class="btn btn-success">Guardar Cambios</button>
            </form>

            <!-- Resultados del examen -->
            <div class="exam-results">
                <h3>Resultados del Examen</h3>
                <%
                    List<Resultado> resultados = examenDao.obtenerResultados(examenId);
                %>
                <table class="table">
                    <thead>
                    <tr>
                        <th>Estudiante</th>
                        <th>Calificación</th>
                        <th>Aciertos</th>
                        <th>Total Preguntas</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Resultado resultado : resultados) { %>
                    <tr>
                        <td><%= resultado.getEstudianteNombre() %></td>
                        <td><%= resultado.getCalificacion() %>%</td>
                        <td><%= resultado.getAciertos() %>/<%= resultado.getTotalPreguntas() %></td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        <% } %>
    </div>
</div
