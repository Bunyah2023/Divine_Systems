<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mx.edu.utez.sidex.dao.PreguntaDao" %>
<%@ page import="mx.edu.utez.sidex.model.Pregunta" %>
<%@ page import="mx.edu.utez.sidex.dao.ExamenDao" %>
<%@ page import="mx.edu.utez.sidex.model.Examen" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>

<%
    // Verificación de acceso para usuarios activos
    User user = (User) session.getAttribute("user");
    if (user == null || !user.isEstado()) { // Verifica si el usuario es nulo o está inactivo
        response.sendRedirect("acceso_denegado.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Examen - SIDEX</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/examen.css">
</head>
<body>
<div class="container">
    <h1>Editar Examen</h1>

    <%
        // Obtener el ID del examen desde la URL y validar
        String examenIdStr = request.getParameter("examenId");
        int examenId = 0;

        try {
            if (examenIdStr != null && !examenIdStr.trim().isEmpty()) {
                examenId = Integer.parseInt(examenIdStr);
            } else {
                throw new NumberFormatException("El ID del examen es nulo o vacío.");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("error.jsp?mensaje=ID de examen no válido.");
            return;
        }

        ExamenDao examenDao = new ExamenDao();
        Examen examen = examenDao.obtenerPorId(examenId);
        PreguntaDao preguntaDao = new PreguntaDao();
        List<Pregunta> preguntas = preguntaDao.obtenerPreguntasPorExamenId(examenId);

        if (examen != null) {
    %>

    <form action="editarExamen" method="post">
        <input type="hidden" name="examenId" value="<%= examenId %>">
        <input type="hidden" name="claseId" value="<%= examen.getClaseId() %>">

        <!-- Mostrar detalles del examen -->
        <div class="form-group">
            <label for="titulo">Título del Examen:</label>
            <input type="text" class="form-control" id="titulo" name="titulo" value="<%= examen.getTitulo() %>" required>
        </div>

        <div class="form-group">
            <label for="descripcion">Descripción:</label>
            <textarea class="form-control" id="descripcion" name="descripcion" required><%= examen.getDescripcion() %></textarea>
        </div>

        <div class="form-group">
            <label for="fechaApertura">Fecha de Apertura:</label>
            <input type="datetime-local" class="form-control" id="fechaApertura" name="fechaApertura"
                   value="<%= examen.getFechaHoraApertura() != null ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(examen.getFechaHoraApertura()) : "" %>" required>
        </div>

        <div class="form-group">
            <label for="fechaCierre">Fecha de Cierre:</label>
            <input type="datetime-local" class="form-control" id="fechaCierre" name="fechaCierre"
                   value="<%= examen.getFechaHoraCierre() != null ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(examen.getFechaHoraCierre()) : "" %>" required>
        </div>

        <h3>Preguntas del Examen</h3>

        <%
            int numeroPregunta = 1;
            for (Pregunta pregunta : preguntas) {
        %>
        <div class="pregunta">
            <h4>Pregunta <%= numeroPregunta++ %></h4>
            <div class="form-group">
                <label>Texto de la Pregunta:</label>
                <input type="text" class="form-control" name="preguntaTexto<%= pregunta.getId() %>" value="<%= pregunta.getTexto() %>" required>
            </div>
            <div class="form-group">
                <label>Opción 1:</label>
                <input type="text" class="form-control" name="opcion<%= pregunta.getId() %>1" value="<%= pregunta.getOpcion1() %>" required>
            </div>
            <div class="form-group">
                <label>Opción 2:</label>
                <input type="text" class="form-control" name="opcion<%= pregunta.getId() %>2" value="<%= pregunta.getOpcion2() %>" required>
            </div>
            <div class="form-group">
                <label>Opción 3:</label>
                <input type="text" class="form-control" name="opcion<%= pregunta.getId() %>3" value="<%= pregunta.getOpcion3() %>" required>
            </div>
            <div class="form-group">
                <label>Opción 4:</label>
                <input type="text" class="form-control" name="opcion<%= pregunta.getId() %>4" value="<%= pregunta.getOpcion4() %>" required>
            </div>
            <div class="form-group">
                <label>Respuesta Correcta:</label>
                <select class="form-control" name="respuestaCorrecta<%= pregunta.getId() %>">
                    <option value="1" <%= (pregunta.getRespuestaCorrecta() == 1 ? "selected" : "") %>>Opción 1</option>
                    <option value="2" <%= (pregunta.getRespuestaCorrecta() == 2 ? "selected" : "") %>>Opción 2</option>
                    <option value="3" <%= (pregunta.getRespuestaCorrecta() == 3 ? "selected" : "") %>>Opción 3</option>
                    <option value="4" <%= (pregunta.getRespuestaCorrecta() == 4 ? "selected" : "") %>>Opción 4</option>
                </select>
            </div>
        </div>
        <% } %>
        <button type="submit" class="btn btn-primary">Guardar Cambios</button>
    </form>

    <%
    } else {  // Mostrar advertencia si no se encuentra el examen
    %>
    <div class="alert alert-warning">
        <strong>Advertencia:</strong> No se encontró el examen con ID <%= examenId %>.
    </div>
    <%
        }
    %>
</div>
</body>
</html>



