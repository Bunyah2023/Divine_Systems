<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="mx.edu.utez.sidex.dao.PreguntaDao" %>
<%@ page import="mx.edu.utez.sidex.model.Pregunta" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Realizar Examen - SIDEX</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/examen.css">
</head>
<body>
<div class="container">
    <h1>Realizar Examen</h1>

    <%
        // Obtener el ID del examen desde la URL
        String examenIdStr = request.getParameter("examenId");
        int examenId = (examenIdStr != null) ? Integer.parseInt(examenIdStr) : 0;

        if (examenId > 0) {
            PreguntaDao preguntaDao = new PreguntaDao();
            List<Pregunta> preguntas = preguntaDao.obtenerPreguntasPorExamenId(examenId); // Obtener preguntas del examen

            if (!preguntas.isEmpty()) {
                Collections.shuffle(preguntas); // Mezclar las preguntas para aleatorizarlas

                // Limitar el número de preguntas a 15
                preguntas = preguntas.subList(0, Math.min(15, preguntas.size()));
    %>

    <form action="guardarRespuestas" method="post">
        <input type="hidden" name="examenId" value="<%= examenId %>">
        <%
            int numeroPregunta = 1;
            for (Pregunta pregunta : preguntas) {
                List<String> opciones = new ArrayList<>();
                opciones.add(pregunta.getOpcion1());
                opciones.add(pregunta.getOpcion2());
                opciones.add(pregunta.getOpcion3());
                opciones.add(pregunta.getOpcion4());
                Collections.shuffle(opciones); // Mezclar opciones

        %>
        <div class="pregunta">
            <h4>Pregunta <%= numeroPregunta++ %>: <%= pregunta.getTexto() %></h4>
            <%
                for (int i = 0; i < opciones.size(); i++) {
                    String opcion = opciones.get(i);
            %>
            <div class="form-check">
                <input class="form-check-input" type="radio" name="respuesta<%= pregunta.getId() %>" value="<%= i + 1 %>" required>
                <label class="form-check-label">
                    <%= opcion %>
                </label>
            </div>
            <%
                }
            %>
        </div>
        <%
            }
        %>
        <button type="submit" class="btn btn-primary">Enviar Respuestas</button>
    </form>

    <%
    } else {
    %>
    <div class="alert alert-warning">
        <strong>Advertencia:</strong> No se encontraron preguntas para este examen.
    </div>
    <%
        }
    } else {
    %>
    <div class="alert alert-danger">
        <strong>Error:</strong> El ID del examen no es válido.
    </div>
    <%
        }
    %>
</div>
</body>
</html>
