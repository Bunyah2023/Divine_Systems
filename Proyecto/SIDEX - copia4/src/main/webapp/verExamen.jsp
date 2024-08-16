<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Random, java.text.DecimalFormat, mx.edu.utez.sidex.dao.ExamenDao, mx.edu.utez.sidex.dao.ResultadoDao, mx.edu.utez.sidex.model.Examen, mx.edu.utez.sidex.model.Resultado, mx.edu.utez.sidex.dao.ClaseDao, mx.edu.utez.sidex.model.Clase" %>
<%
    // Establece las cabeceras HTTP para prevenir el almacenamiento en caché
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setDateHeader("Expires", 0); // Proxies.
%>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalles del Examen - SIDEX</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/resultados.css">
</head>
<body>
<div class="main-container">
    <div class="container">
        <%
            String examenIdStr = request.getParameter("examenId");
            int examenId = examenIdStr != null ? Integer.parseInt(examenIdStr) : 0;
            ResultadoDao resultadoDao = new ResultadoDao();
            ExamenDao examenDao = new ExamenDao();
            ClaseDao claseDao = new ClaseDao();

            Resultado resultado = null;
            Random rand = new Random();
            String mensajeMotivacional = "";

            String[] mensajesAltos = {
                    "¡Excelente trabajo!",
                    "¡Sobresaliente esfuerzo!",
                    "¡Mantén el gran trabajo!",
                    "¡Eres un modelo a seguir!",
                    "¡Increíble dedicación!"
            };

            String[] mensajesMedios = {
                    "¡Buen trabajo, sigue así!",
                    "Estás progresando muy bien, ¡continúa!",
                    "¡Sigue esforzándote y lo lograrás!",
                    "Estás en el camino correcto.",
                    "¡Buen esfuerzo! Intenta revisar algunos puntos."
            };

            String[] mensajesBajos = {
                    "No te desanimes, ¡aprende de tus errores!",
                    "La práctica hace al maestro, ¡sigue intentándolo!",
                    "Revisa tus errores, puedes hacerlo mejor.",
                    "El fracaso es solo el primer paso hacia el éxito.",
                    "No es fracaso, es una lección aprendida."
            };

            if (examenId > 0) {
                // Obtener el resultado del examen
                resultado = resultadoDao.obtenerResultadoPorExamenId(examenId);
                Examen examen = examenDao.obtenerExamenPorId(examenId);

                if (resultado != null && examen != null) {
                    Clase clase = claseDao.obtenerClasePorId(examen.getClaseId());

                    if (clase != null) {
                        // Calcular la calificación basada en los aciertos
                        int aciertos = resultado.getAciertos();
                        int totalPreguntas = resultado.getTotalPreguntas();
                        double porcentajeAciertos = (double) aciertos / totalPreguntas * 100;

                        // Determinar la calificación en función del porcentaje de aciertos
                        double calificacion = 0.0;
                        if (porcentajeAciertos >= clase.getMinAU()) {
                            calificacion = clase.getMaxAU();
                        } else if (porcentajeAciertos >= clase.getMinDE()) {
                            calificacion = clase.getMaxDE();
                        } else if (porcentajeAciertos >= clase.getMinSA()) {
                            calificacion = clase.getMaxSA();
                        } else {
                            calificacion = clase.getMaxNA();
                        }

                        resultado.setCalificacion(calificacion); // Establecer la calificación calculada

                        // Guardar la calificación en la base de datos
                        resultadoDao.actualizarCalificacion(resultado);

                        // Obtener el mensaje motivacional
                        if (calificacion >= 90) {
                            mensajeMotivacional = mensajesAltos[rand.nextInt(mensajesAltos.length)];
                        } else if (calificacion >= 60) {
                            mensajeMotivacional = mensajesMedios[rand.nextInt(mensajesMedios.length)];
                        } else {
                            mensajeMotivacional = mensajesBajos[rand.nextInt(mensajesBajos.length)];
                        }

                        // Determinar si el examen fue aprobado o reprobado
                        boolean aprobado = calificacion >= 60; // Suponiendo que el 60 es la nota de aprobación
                        String resultadoClase = aprobado ? "pass" : "fail";
        %>

        <div class="exam-results <%= resultadoClase %>">
            <h2>Resultado del Examen</h2>
            <p>Calificación: <%= new DecimalFormat("#.0").format(calificacion) %></p>
            <p>Aciertos: <%= resultado.getAciertos() %></p>
            <p>Respuestas Incorrectas: <%= resultado.getRespuestasIncorrectas() %></p>
            <p><strong>Mensaje Motivacional:</strong> <%= mensajeMotivacional %></p>
        </div>
        <a href="detalleClase.jsp?claseId=<%= resultado.getExamenId() %>" class="btn-regresar">Regresar a la Clase</a>

        <%
        } else { // Manejar el caso donde la clase no se encontró
        %>
        <div class="alert alert-danger">
            <strong>Error:</strong> No se encontró la clase asociada al examen.
        </div>
        <%
            }
        } else { // Manejar el caso donde no se encontró el resultado o el examen
        %>
        <div class="alert alert-warning">
            <strong>Información:</strong> Ya has entregado el examen. No puedes regresar para modificar tus respuestas.
        </div>
        <%
            }
        } else { // Manejar el caso donde el ID del examen no es válido
        %>
        <div class="alert alert-danger">
            <strong>Error:</strong> El ID del examen no es válido.
        </div>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
