<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mx.edu.utez.sidex.model.User, mx.edu.utez.sidex.dao.ClaseDao, mx.edu.utez.sidex.model.Clase, mx.edu.utez.sidex.dao.ExamenDao, mx.edu.utez.sidex.dao.ResultadoDao, mx.edu.utez.sidex.model.Examen, mx.edu.utez.sidex.model.Resultado, java.util.List, java.util.Date" %>

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
    <style>
        /* Estilos personalizados */
        .modal-exito {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: #f8f9fa;
            border: 1px solid #ddd;
            padding: 20px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            display: none;
            z-index: 1050;
            text-align: center;
            border-radius: 8px;
        }

        .historial-examenes {
            margin-top: 20px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
        }
        .historial-examenes h3 {
            margin-bottom: 15px;
        }
        .examen-historial {
            margin-bottom: 10px;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #fff;
            cursor: pointer;
        }
        .examen-historial:hover {
            background-color: #f1f1f1;
        }
        .examen-caducado {
            background-color: #ffdddd;
            cursor: not-allowed;
        }
        .resultados-examen p {
            margin: 5px 0;
        }
        .mensaje-no-examenes {
            text-align: center;
            font-weight: bold;
            color: #28a745;
            margin-top: 20px;
        }
    </style>
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
        <h3>Mis clases</h3>
        <ul>
            <%
                User usuario = (User) session.getAttribute("user");
                ClaseDao claseDao = new ClaseDao();
                List<Clase> clases = null;
                if (usuario.getRolId() == 1) { // Estudiante
                    clases = claseDao.obtenerClasesPorEstudiante(usuario.getId());
                } else if (usuario.getRolId() == 2) { // Docente
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
                ResultadoDao resultadoDao = new ResultadoDao();
                Date now = new Date();

                // Obtener exámenes pendientes (para estudiantes)
                List<Examen> examenesPendientes = examenDao.obtenerExamenesPorClaseYEstado(Integer.parseInt(claseId), false);

                // Obtener exámenes completados (para estudiantes)
                List<Examen> examenesCompletados = examenDao.obtenerExamenesPorClaseYEstado(Integer.parseInt(claseId), true);

                // Obtener historial de exámenes (para docentes)
                List<Examen> examenesHistorial = examenDao.obtenerExamenesCerradosPorClase(Integer.parseInt(claseId));

                // Obtener último examen editado por el docente
                Examen ultimoExamenEditado = examenDao.obtenerUltimoExamenEditadoPorDocente(usuario.getId());

                // Obtener exámenes en curso (para docentes)
                List<Examen> examenesEnCurso = examenDao.obtenerExamenesEnCursoPorDocente(usuario.getId());
            %>
            <div class="class-details">
                <h1><%= clase.getNombre() %></h1>
                <p><%= clase.getDescripcion() %></p>
                <div class="class-info">
                    <p>Fecha de inicio: <%= clase.getFechaInicio() != null ? clase.getFechaInicio().toString() : "Fecha no disponible" %></p>
                    <p>Fecha de fin: <%= clase.getFechaFin() != null ? clase.getFechaFin().toString() : "Fecha no disponible" %></p>
                    <p>Código: <%= clase.getCodigo() %></p>
                </div>
            </div>

            <% if (usuario.getRolId() == 1) { %> <!-- Estudiante -->
            <div class="exam-section">
                <h2>Exámenes pendientes</h2>
                <div class="exam-list">
                    <% if (examenesPendientes.isEmpty()) { %>
                    <p class="mensaje-no-examenes">Yujuuu, no tienes exámenes pendientes</p>
                    <% } else { %>
                    <% for (Examen examen : examenesPendientes) {
                        if (examen.getFechaHoraApertura() != null && examen.getFechaHoraCierre() != null &&
                                now.after(examen.getFechaHoraApertura()) && now.before(examen.getFechaHoraCierre())) {
                    %>
                    <div class="exam">
                        <p><%= examen.getTitulo() %> - <%= examen.getFechaHoraApertura() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(examen.getFechaHoraApertura()) : "Fecha no disponible" %></p>
                        <button class="btn btn-primary" onclick="window.location.href='examen.jsp?examenId=<%= examen.getId() %>'">Tomar examen</button>
                    </div>
                    <% } else if (examen.getFechaHoraCierre() != null && now.after(examen.getFechaHoraCierre())) {
                        examenDao.moverExamenAHistorial(examen.getId(), usuario.getId());
                    %>
                    <div class="exam examen-caducado tooltip">
                        <p><%= examen.getTitulo() %> - Examen Caducado</p>
                        <span class="tooltiptext">Examen ya caducado</span>
                    </div>
                    <% } %>
                    <% } %>
                    <% } %>
                </div>
                <h2>Exámenes completados</h2>
                <div class="exam-results">
                    <% for (Examen examen : examenesCompletados) { %>
                    <div class="exam">
                        <p><%= examen.getTitulo() %> - <%= examen.getFechaCierre() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(examen.getFechaCierre()) : "Fecha no disponible" %></p>
                        <%
                            Resultado resultado = resultadoDao.obtenerResultadoPorExamenYEstudiante(examen.getId(), usuario.getId());
                        %>
                        <div class="resultados-examen">
                            <p>Calificación: <%= resultado.getCalificacion() %></p>
                            <p>Aciertos: <%= resultado.getAciertos() %> / <%= resultado.getTotalPreguntas() %></p>
                            <p>Respuestas Incorrectas: <%= resultado.getRespuestasIncorrectas() %></p>
                            <p><%= resultado.getAprobado() ? "Aprobado" : "Reprobado" %></p>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>
            <% } else if (usuario.getRolId() == 2) { %> <!-- Docente -->

            <div class="almacen-examenes">
                <h2>Almacén de Exámenes</h2>
                <p>Dentro del almacén puedes seleccionar un examen y aplicarlo a tus alumnos. Aquí podrás encontrar exámenes creados previamente por el coordinador.</p>
                <button class="btn btn-primary" onclick="window.location.href='almacendeexamenes.jsp'">Almacén de Exámenes</button>
            </div>

            <div class="exam-section">
                <h2>Exámenes en curso</h2>
                <div class="exam-list">
                    <% if (examenesEnCurso.isEmpty()) { %>
                    <p>No hay exámenes en curso en este momento.</p>
                    <% } else { %>
                    <% for (Examen examen : examenesEnCurso) { %>
                    <div class="exam">
                        <p><%= examen.getTitulo() %> - <%= examen.getFechaHoraApertura() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(examen.getFechaHoraApertura()) : "Fecha no disponible" %></p>
                        <button class="btn btn-primary" onclick="window.location.href='editarExamen.jsp?examenId=<%= examen.getId() %>'">Editar Examen</button>
                    </div>
                    <% } %>
                    <% } %>
                </div>

                <h2>Historial de exámenes</h2>
                <div class="historial-examenes">
                    <% for (Examen examen : examenesHistorial) {
                        boolean caducado = examen.getFechaHoraCierre() != null && now.after(examen.getFechaHoraCierre());
                    %>
                    <div class="examen-historial <%= caducado ? "examen-caducado" : "" %>" data-examen-id="<%= examen.getId() %>">
                        <p><%= examen.getTitulo() %> - <%= examen.getFechaHoraCierre() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(examen.getFechaHoraCierre()) : "Fecha no disponible" %></p>
                        <button class="btn btn-primary" onclick="window.location.href='editarExamen.jsp?examenId=<%= examen.getId() %>'">Editar Examen</button>
                    </div>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>
    </main>
</div>

<!-- Modal de éxito -->
<div id="modal-exito" class="modal-exito">
    <p>¡Examen enviado con éxito!</p>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        <% if ("exito".equals(request.getAttribute("messageType"))) { %>
        var modal = document.getElementById('modal-exito');
        modal.style.display = 'block';
        setTimeout(function() {
            modal.style.display = 'none';
        }, 3000);
        <% } %>

        document.getElementById('hamburger-menu').addEventListener('click', function() {
            var sidebar = document.getElementById('sidebar');
            sidebar.style.display = sidebar.style.display === 'block' ? 'none' : 'block';
        });

        var examenesHistorial = document.querySelectorAll('.examen-historial');
        examenesHistorial.forEach(function(examen) {
            examen.addEventListener('click', function() {
                var examenId = examen.getAttribute('data-examen-id');
            });
        });
    });
</script>

</body>
</html>
