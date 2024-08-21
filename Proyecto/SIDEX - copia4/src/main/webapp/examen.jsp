<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.sidex.dao.ResultadoDao" %>
<%@ page import="mx.edu.utez.sidex.model.Resultado" %>
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
    <style>
        /* Estilos para el modal */
        .modal-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 1000;
            justify-content: center;
            align-items: center;
        }

        .modal-content {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            text-align: center;
            max-width: 400px;
        }

        .modal-content button {
            margin: 10px;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .btn-danger {
            background-color: #dc3545;
            color: white;
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Realizar Examen</h1>

    <%
        // Obtener el ID del examen desde la URL
        String examenIdStr = request.getParameter("examenId");
        int examenId = (examenIdStr != null) ? Integer.parseInt(examenIdStr) : 0;
        int usuarioId = (int) session.getAttribute("usuarioId");

        ResultadoDao resultadoDao = new ResultadoDao();
        int intentosActuales = resultadoDao.contarIntentos(usuarioId, examenId);
        int intentosPermitidos = resultadoDao.obtenerIntentosPermitidos(examenId);

        if (intentosActuales >= intentosPermitidos) {
            // Obtener la calificación del último intento
            Resultado ultimoResultado = resultadoDao.obtenerUltimoResultado(usuarioId, examenId);
    %>
    <div class="alert alert-info">
        <strong>Ya has respondido este examen. Tu calificación es: <%= ultimoResultado.getCalificacion() %></strong>
    </div>
    <%
    } else {
        // Continuar con la lógica para mostrar las preguntas del examen
        if (examenId > 0) {
            PreguntaDao preguntaDao = new PreguntaDao();
            List<Pregunta> preguntas = preguntaDao.obtenerPreguntasPorExamenEditadoId(examenId); // Obtener preguntas del examen desde ExamenesEditadosPorDocentes

            if (!preguntas.isEmpty()) {
                Collections.shuffle(preguntas); // Mezclar las preguntas para aleatorizarlas

                // Limitar el número de preguntas a 15
                preguntas = preguntas.subList(0, Math.min(15, preguntas.size()));
    %>

    <form action="guardarRespuestas" method="post" onsubmit="window.onbeforeunload = null;">
        <input type="hidden" name="examenId" value="<%= examenId %>">
        <input type="hidden" name="usuarioId" value="<%= session.getAttribute("usuarioId") %>">
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
        }
    %>
</div>

<!-- Modal -->
<div class="modal-overlay" id="modalOverlay">
    <div class="modal-content">
        <p>Si recargas la página, perderás todas tus respuestas. ¿Estás seguro de que quieres recargar?</p>
        <button id="confirmReload" class="btn-danger">Sí, recargar</button>
        <button id="cancelReload" class="btn-secondary">No, continuar</button>
    </div>
</div>

<script>
    let preventUnload = true;

    // Mostrar el modal si se intenta recargar la página
    window.onbeforeunload = function (event) {
        if (preventUnload) {
            document.getElementById('modalOverlay').style.display = 'flex';
            event.preventDefault();
            event.returnValue = ''; // Requerido para algunos navegadores
            return ''; // Prevenir recarga
        }
    };

    // Confirmar recarga
    document.getElementById('confirmReload').addEventListener('click', function() {
        preventUnload = false; // Permitir recarga
        window.location.reload(); // Recargar página
    });

    // Cancelar recarga
    document.getElementById('cancelReload').addEventListener('click', function() {
        document.getElementById('modalOverlay').style.display = 'none'; // Cerrar el modal
    });

    // Desaparecer el modal automáticamente después de 6 segundos si no se toma ninguna acción
    setTimeout(function() {
        document.getElementById('modalOverlay').style.display = 'none';
    }, 6000);
</script>

</body>
</html>









