<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>
<%@ page import="mx.edu.utez.sidex.dao.ExamenDao" %>
<%@ page import="mx.edu.utez.sidex.model.Examen" %>
<%@ page import="java.util.List" %>

<%
    // Verificación de acceso para usuarios activos y con rol_id=4
    User usuario = (User) session.getAttribute("user");
    if (usuario == null || !usuario.isEstado() || usuario.getRolId() != 4) { // Verifica si el usuario es nulo, está inactivo o no tiene rol_id=4
        response.sendRedirect("acceso_denegado.jsp");
        return;
    }
%>


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
            <li><a id="nav-home" href="index-coordinador.jsp">Inicio</a></li>
            <li><a id="nav-questions" href="#">Preguntas</a></li>
            <li><a id="nav-classes" href="#">Clases</a></li>
            <li><a id="nav-exams" href="#">Exámenes</a></li>
            <li><a id="nav-settings" href="#">Configuración</a></li>
        </ul>
    </nav>
</header>

<main>
    <h2>Panel de Coordinador</h2>

    <%
        String registerMessage = (String) session.getAttribute("registerMessage");
        String messageType = (String) session.getAttribute("messageType");
        if (registerMessage != null) {
    %>
    <div class="message" style="color: <%= "error".equals(messageType) ? "red" : "green" %>;">
        <%= registerMessage %>
    </div>
    <%
            session.removeAttribute("registerMessage");
            session.removeAttribute("messageType");
        }
    %>
    <br>
    <div>
        <button id="create-exam-btn" class="btn btn-primary">Crear Nuevo Examen</button>
    </div>
    <br>

    <div id="modal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <br>
            <h2>Crear Nuevo Examen</h2>
            <form id="create-exam-form" action="crearExamen" method="post">
                <!-- Formulario de creación de examen -->
                <input type="hidden" name="action" value="confirm">
                <div class="form-group">
                    <label for="exam-title">Título del examen:</label>
                    <input type="text" id="exam-title" name="titulo" placeholder="Título del examen" required>
                </div>
                <div class="form-group">
                    <label for="description">Descripción:</label>
                    <textarea id="description" name="descripcion" placeholder="Descripción del examen" required></textarea>
                </div>
                <div class="form-group">
                    <label for="materia">Materia:</label>
                    <input type="text" id="materia" name="materia" required>
                </div>
                <div class="form-group">
                    <label for="fechaApertura">Fecha de Apertura:</label>
                    <input type="datetime-local" id="fechaApertura" name="fechaApertura" required>
                </div>
                <div class="form-group">
                    <label for="fechaCierre">Fecha de Cierre:</label>
                    <input type="datetime-local" id="fechaCierre" name="fechaCierre" required>
                </div>
                <div class="form-group">
                    <label for="intentos">Número de Intentos:</label>
                    <input type="number" id="intentos" name="intentos" min="1" value="1" required>
                </div>
                <h3>Preguntas</h3>
                <div id="questions-container"></div>
                <button type="button" id="add-question-btn" class="add-question-btn">+</button>
                <button type="submit" class="btn btn-success">Guardar</button>
                <button type="button" id="cancel-btn" class="btn btn-danger">Cancelar</button>
            </form>
        </div>
    </div>

    <!-- Almacén de Exámenes -->
    <section id="almacen-examenes">
        <h2>Almacén de Exámenes</h2>

        <!-- Buscador de exámenes -->
        <div class="buscador">
            <form action="almacenExamenes" method="get">
                <input type="text" name="busqueda" placeholder="Buscar por nombre...">
                <button type="submit" class="btn btn-secondary">Buscar</button>
            </form>
        </div>

        <!-- Ordenar por examen_id -->
        <div class="ordenar">
            <form action="almacenExamenes" method="get">
                <select name="orden" onchange="this.form.submit()">
                    <option value="desc">Más reciente</option>
                    <option value="asc">Más antiguo</option>
                </select>
            </form>
        </div>

        <%
            String busqueda = request.getParameter("busqueda");
            String orden = request.getParameter("orden");

            ExamenDao examenDao = new ExamenDao();
            List<Examen> examenes;

            if (busqueda != null && !busqueda.trim().isEmpty()) {
                examenes = examenDao.buscarExamenesPorNombre(busqueda, orden);
            } else {
                examenes = examenDao.obtenerExamenes(orden);
            }

            if (examenes != null && !examenes.isEmpty()) {
        %>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Materia</th>
                <th>Intentos</th>
            </tr>
            </thead>
            <tbody>
            <%
                for (Examen examen : examenes) {
            %>
            <tr>
                <td><%= examen.getId() %></td>
                <td><%= examen.getTitulo() %></td>
                <td><%= examen.getMateria() %></td>
                <td><%= examen.getIntentos() %></td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <%
        } else {
        %>
        <div class="alert alert-info">
            No se encontraron exámenes.
        </div>
        <%
            }
        %>
    </section>

    <!-- Sección para añadir o registrar docentes y administradores -->
    <section id="registro-usuarios">
        <h2>Registrar Nuevo Usuario</h2>
        <form action="registrarCoordinador" method="post">
            <div class="form-group">
                <label for="nombres">Nombre:</label>
                <input type="text" id="nombres" name="nombres" required>
            </div>
            <div class="form-group">
                <label for="apellido">Apellido:</label>
                <input type="text" id="apellido" name="apellido" required>
            </div>
            <div class="form-group">
                <label for="apellidoMaterno">Apellido Materno:</label>
                <input type="text" id="apellidoMaterno" name="apellidoMaterno" required>
            </div>
            <div class="form-group">
                <label for="correo">Correo Electrónico:</label>
                <input type="email" id="correo" name="correo" required>
            </div>
            <div class="form-group">
                <label for="rol">Rol:</label>
                <select id="rol" name="rol" required>
                    <option value="2">Docente</option>
                    <option value="3">Administrador</option>
                </select>
            </div>
            <div class="form-group">
                <label for="contrasena">Contraseña:</label>
                <input type="password" id="contrasena" name="contrasena" required>
            </div>
            <button type="submit" class="btn btn-success">Registrar</button>
        </form>
    </section>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const modal = document.getElementById('modal');
        const btnOpenModal = document.getElementById('create-exam-btn');
        const btnCloseModal = document.getElementsByClassName('close')[0];
        const btnCancel = document.getElementById('cancel-btn');

        function toggleModal(display) {
            modal.style.display = display;
        }

        btnOpenModal.onclick = function() {
            toggleModal('block');
        };

        btnCloseModal.onclick = function() {
            toggleModal('none');
        };

        btnCancel.onclick = function() {
            toggleModal('none');
        };

        window.onclick = function(event) {
            if (event.target === modal) {
                toggleModal('none');
            }
        };

        const addQuestionBtn = document.getElementById('add-question-btn');
        const questionsContainer = document.getElementById('questions-container');
        let questionCount = 0;
        const maxQuestions = 30; // Ajusta según sea necesario

        addQuestionBtn.addEventListener('click', function() {
            if (questionCount < maxQuestions) {
                questionCount++;
                const newQuestionHTML = `
                    <div class="question">
                        <label for="question${questionCount}">Pregunta ${questionCount}:</label>
                        <textarea id="question${questionCount}" name="pregunta${questionCount}" placeholder="Escribe la pregunta aquí" required></textarea>
                        <label>Opciones:</label>
                        <input type="text" name="opcion${questionCount}1" placeholder="Opción 1" required>
                        <input type="text" name="opcion${questionCount}2" placeholder="Opción 2" required>
                        <input type="text" name="opcion${questionCount}3" placeholder="Opción 3" required>
                        <input type="text" name="opcion${questionCount}4" placeholder="Opción 4" required>
                        <label for="correct${questionCount}">Respuesta Correcta:</label>
                        <select name="correcta${questionCount}" required>
                            <option value="1">Opción 1</option>
                            <option value="2">Opción 2</option>
                            <option value="3">Opción 3</option>
                            <option value="4">Opción 4</option>
                        </select>
                    </div>
                `;
                questionsContainer.insertAdjacentHTML('beforeend', newQuestionHTML);
            }
        });
    });
</script>

</body>
</html>


