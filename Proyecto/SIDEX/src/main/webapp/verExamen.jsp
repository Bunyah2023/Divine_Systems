<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.sidex.dao.ExamenDao, mx.edu.utez.sidex.dao.ResultadoDao, mx.edu.utez.sidex.model.Examen, mx.edu.utez.sidex.model.Resultado" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalles del Examen - SIDEX</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/examen.css">
</head>
<body>
<div class="main-container">
    <div class="container">
        <%
            String examenIdStr = request.getParameter("examenId");
            int examenId = examenIdStr != null ? Integer.parseInt(examenIdStr) : 0;
            ExamenDao examenDao = new ExamenDao();
            ResultadoDao resultadoDao = new ResultadoDao();
            Resultado resultado = resultadoDao.obtenerResultadoPorExamenId(examenId);

            // Determinar si el examen fue aprobado o reprobado
            boolean aprobado = resultado.getCalificacion() >= 60; // Suponiendo que el 60 es la nota de aprobación
            String resultadoClase = aprobado ? "pass" : "fail";
        %>

        <div class="exam-results <%= resultadoClase %>">
            <h2>Resultado del Examen</h2>
            <p>Calificación: <%= resultado.getCalificacion() %>%</p>
            <p>Aciertos: <%= resultado.getAciertos() %></p>
            <p>Respuestas Incorrectas: <%= resultado.getRespuestasIncorrectas() %></p>
        </div>
    </div>
</div>
</body>
</html>
