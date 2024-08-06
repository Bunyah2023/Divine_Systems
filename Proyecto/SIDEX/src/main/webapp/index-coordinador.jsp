<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>
<%@ page import="mx.edu.utez.sidex.dao.PreguntaDao" %>
<%@ page import="mx.edu.utez.sidex.model.Pregunta" %>
<%@ page import="mx.edu.utez.sidex.dao.ClaseDao" %>
<%@ page import="mx.edu.utez.sidex.model.Clase" %>
<%@ page import="mx.edu.utez.sidex.dao.ExamenDao" %>
<%@ page import="mx.edu.utez.sidex.model.Examen" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SIDEX - Panel Coordinador</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/index.css">
    <link rel="stylesheet" href="CSS/index-coordinador.css">
    <script src="JS/index.js" defer></script>
</head>
<body>
<header>
    <div class="logo">
        <img id="logoDIVINESYSTEMS" src="IMG/LOGO SIDEX by DIVINE SYSTEMS.png" alt="DIVINE SYSTEMS" onclick="location.href='index-coordinador.jsp'">
    </div>
    <nav>
        <ul>
            <li><a href="index-coordinador.jsp">Inicio</a></li>
            <li><a href="#">Preguntas</a></li>
            <li><a href="#">Clases</a></li>
            <li><a href="#">Exámenes</a></li>
            <li><a href="#">Configuración</a></li>
        </ul>
    </nav>
</header>

<main>
    <h1>Gestión de Exámenes</h1>

    <%
        User user = (User) session.getAttribute("user");
        if (user != null) {
            out.print("Bienvenido, " + user.getNombres() + " " + user.getApellido() + " (" + user.getCorreo() + ")");
        } else {
            response.sendRedirect("login.jsp");
            return; // Asegurar que el código se detenga aquí si redirige
        }
    %>

    <!-- Formulario para crear un nuevo examen -->
    <section id="crear-examen">
        <h2>Crear Nuevo Examen</h2>
        <form action="crearExamen" method="post">
            <div class="form-group">
                <label for="nombreExamen">Nombre del Examen:</label>
                <input type="text" id="nombreExamen" name="nombre" required>
            </div>
            <div class="form-group">
                <label for="clasesExamen">Selecciona las Clases:</label>
                <select id="clasesExamen" name="clases[]" multiple required>
                    <%
                        ClaseDao claseDao = new ClaseDao();
                        List<Clase> clases = claseDao.obtenerTodasClases();
                        for (Clase clase : clases) {
                    %>
                    <option value="<%= clase.getId() %>"><%= clase.getNombre() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Crear Examen</button>
        </form>
    </section>

    <!-- Listado de exámenes por clase -->
    <section id="examenes-por-clase">
        <h2>Exámenes por Clase</h2>
        <%
            ExamenDao examenDao = new ExamenDao();
            // Se reutiliza el objeto claseDao creado previamente
            for (Clase clase : clases) {
                List<Examen> examenes = examenDao.obtenerExamenesPorClase(clase.getId());
        %>
        <div class="clase">
            <h3><%= clase.getNombre() %></h3>
            <ul>
                <%
                    for (Examen examen : examenes) {
                %>
                <li>
                    <%= examen.getTitulo() %>
                    <form action="agregarPreguntaAExamen" method="post">
                        <input type="hidden" name="examenId" value="<%= examen.getId() %>">
                        <select name="preguntaId" required>
                            <%
                                PreguntaDao preguntaDao = new PreguntaDao();
                                List<Pregunta> preguntas = preguntaDao.obtenerTodasPreguntas();
                                for (Pregunta pregunta : preguntas) {
                            %>
                            <option value="<%= pregunta.getId() %>"><%= pregunta.getTexto() %></option>
                            <%
                                }
                            %>
                        </select>
                        <button type="submit" class="btn btn-success">Añadir Pregunta</button>
                    </form>
                </li>
                <%
                    }
                %>
            </ul>
        </div>
        <%
            }
        %>
    </section>
</main>
</body>
</html>
