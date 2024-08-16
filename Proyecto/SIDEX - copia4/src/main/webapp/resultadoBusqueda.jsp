<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mx.edu.utez.sidex.model.Examen" %>
<%@ page import="java.util.List" %>

<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Resultados de la Búsqueda - SIDEX</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
</head>
<body>
<div class="container">
    <h1>Resultados de la Búsqueda</h1>
    <ul>
        <%
            List<Examen> examenes = (List<Examen>) request.getAttribute("examenes");
            if (examenes != null && !examenes.isEmpty()) {
                for (Examen examen : examenes) {
        %>
        <li>
            <h3><%= examen.getTitulo() %></h3>
            <p>Materia: <%= examen.getMateria() %></p>
            <p>Fecha de Apertura: <%= examen.getFechaApertura() %></p>
            <p>Fecha de Cierre: <%= examen.getFechaCierre() %></p>
        </li>
        <%
            }
        } else {
        %>
        <p>No se encontraron exámenes que coincidan con los criterios de búsqueda.</p>
        <%
            }
        %>
    </ul>
    <a href="detalleClase.jsp?claseId=<%= request.getParameter("claseId") %>" class="btn btn-primary">Regresar a la Clase</a>
</div>
</body>
</html>

