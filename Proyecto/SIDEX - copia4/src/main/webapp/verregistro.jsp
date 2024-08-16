<%@ page import="mx.edu.utez.sidex.dao.UserDao" %>
<%@ page import="java.util.List" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Verificación de acceso para usuarios activos y con rol_id=3
    User usuario = (User) session.getAttribute("user");
    if (usuario == null || !usuario.isEstado() || usuario.getRolId() != 3) { // Verifica si el usuario es nulo, está inactivo o no tiene rol_id=3
        response.sendRedirect("acceso_denegado.jsp");
        return;
    }
%>





<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Usuarios</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/modal.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/datatables.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.12.1/css/jquery.dataTables.min.css">
    <style>
        /* Estilo para reducir el tamaño del filtro por rol */
        .form-select-sm {
            width: auto;
            max-width: 200px; /* Ajusta el tamaño máximo del select */
            font-size: 0.9rem; /* Tamaño de fuente más pequeño */
        }

        /* Alinear el filtro por rol al lado derecho */
        .filter-container {
            text-align: right; /* Alinear el contenido al lado derecho */
            margin-bottom: 1rem; /* Espacio inferior para separación */
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.12.1/js/jquery.dataTables.min.js"></script>
</head>
<body>
<div class="container">
    <h1>Lista de Usuarios</h1>

    <div class="d-flex justify-content-end mb-3">
        <div class="me-2">
            <label for="filterRole" class="form-label">Filtrar:</label>
            <select id="filterRole" class="form-select form-select-sm">
                <option value="">Todos</option>
                <option value="Estudiante">Estudiante</option>
                <option value="Docente">Docente</option>
                <option value="Admin">Admin</option>
                <option value="Cordinador">Cordinador</option>
            </select>
        </div>
    </div>

    <table id="example" class="table table-striped table-hover" style="width: 100%">
        <thead>
        <tr>
            <th>Id</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Apellido Materno</th>
            <th>Correo</th>
            <th>Rol</th>
            <th>Estado</th>
            <th>Actualizar</th>
            <th>Actividad</th>
        </tr>
        </thead>
        <tbody>
        <%
            UserDao dao = new UserDao();
            List<User> lista = dao.getAllUsers();
            if (lista != null && !lista.isEmpty()) {
                for(User u : lista) { %>
        <tr>
            <td><%=u.getId()%></td>
            <td><%=u.getNombres()%></td>
            <td><%=u.getApellido()%></td>
            <td><%=u.getApellidoMaterno()%></td>
            <td><%=u.getCorreo()%></td>
            <td>
                <%= u.getRolId() == 1 ? "Estudiante" : (u.getRolId() == 2 ? "Docente" : (u.getRolId() == 3 ? "Admin" : "Cordinador")) %>
            </td>
            <td>
                <%= u.isEstado() ? "Activo" : "Inactivo" %>
            </td>
            <td>
                <a href="verregistro?id=<%=u.getId()%>" class="btn btn-warning edit-btn">Modificar</a>
            </td>
            <td>
                <% if (!u.isEstado()) { %>
                <button class="openModalBtn btn btn-success" data-id="<%=u.getId()%>">Activar</button>
                <% } else { %>
                <button class="openModalBtn btn btn-danger" data-id="<%=u.getId()%>">Desactivar</button>
                <% } %>
            </td>
        </tr>

        <!-- Modal de Confirmación para Eliminar -->
        <div id="modal-<%=u.getId()%>" class="modal">
            <div class="modal-content">
                <span class="close" data-id="<%=u.getId()%>">&times;</span>
                <% if (!u.isEstado()) { %>
                <h2>¿Deseas activar al usuario?</h2>
                <% } else { %>
                <h2>¿Deseas desactivar al usuario?</h2>
                <% } %>
                <div class="modal-buttons">
                    <% if (!u.isEstado()) { %>
                    <button class="btn-accept bt btn-success"><a href="activar?id=<%=u.getId()%>">Aceptar</a></button>
                    <% } else { %>
                    <button class="btn-accept bt btn-success"><a href="delete?id=<%=u.getId()%>">Aceptar</a></button>
                    <% } %>
                    <button class="btn-cancel" data-id="<%=u.getId()%>">Cancelar</button>
                </div>
            </div>
        </div>

        <% }
        } else { %>
        <tr>
            <td colspan="9" class="text-center">No hay usuarios registrados.</td>
        </tr>
        <% }
        %>
        </tbody>
    </table>

    <br>
    <h1>Generar Lista de usuarios</h1>
    <form action="pdf" method="get">
        <button type="submit" name="generate" value="true" class="btn btn-success">Generar PDF</button>
    </form>

    <br>
    <h1>Regresar al inicio</h1>
    <a href="index-admin.jsp" class="btn btn-primary">Regresar a Inicio</a>
    <br>
</div>


<script src="${pageContext.request.contextPath}/JS/modal.js"></script>

<script src="${pageContext.request.contextPath}/JS/jquery-3.7.0.js"></script>
<script src="${pageContext.request.contextPath}/JS/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/JS/datatables.js"></script>
<script src="${pageContext.request.contextPath}/JS/dataTables.bootstrap5.js"></script>
<script src="${pageContext.request.contextPath}/JS/es-MX.json"></script>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        // Configurar DataTable
        var table = $('#example').DataTable({
            "pageLength": 7,
            "ordering": true, // Habilitar ordenamiento
            "lengthChange": false,
            "searching": true,
            "order": [[0, 'desc']], // Ordenar por el primer índice (Id)
            "columnDefs": [
                {
                    "targets": [1,2,3,4,6,7,8], // Aplica la configuración a todas las columnas excepto Rol
                    "orderable": false // Deshabilitar ordenamiento en todas las columnas excepto Id
                },
                {
                    "targets": 0, // Solo habilitar ordenamiento en la primera columna (Id)
                    "orderable": true
                }
            ],
            "columns": [
                { "searchable": false }, // Id no será buscable
                { "searchable": true }, // Nombre será buscable
                { "searchable": true }, // Apellido será buscable
                { "searchable": true }, // Apellido Materno será buscable
                { "searchable": true },  // Correo será buscable
                { "searchable": true }, // Rol será buscable
                { "searchable": false }, // Estado no será buscable
                { "searchable": false }, // Actualizar no será buscable
                { "searchable": false }  // Actividad no será buscable
            ],
            language: {
                url: '${pageContext.request.contextPath}/JS/es-MX.json'
            }
        });

        // Manejar el filtro por rol
        $('#filterRole').on('change', function() {
            var selectedRole = $(this).val();
            table.column(5).search(selectedRole, true, false).draw(); // Filtrar por la columna del Rol
        });

        // Manejar apertura de modal para cada botón
        document.querySelectorAll('.openModalBtn').forEach(button => {
            button.addEventListener('click', function() {
                var userId = this.getAttribute('data-id');
                document.getElementById('modal-' + userId).style.display = "block";
            });
        });

        // Cerrar modal cuando el usuario hace clic en el botón "Cerrar" (x)
        document.querySelectorAll('.close').forEach(button => {
            button.addEventListener('click', function() {
                var userId = this.getAttribute('data-id');
                document.getElementById('modal-' + userId).style.display = "none";
            });
        });

        // Cerrar modal cuando el usuario hace clic en el botón "Cancelar"
        document.querySelectorAll('.btn-cancel').forEach(button => {
            button.addEventListener('click', function() {
                var userId = this.getAttribute('data-id');
                document.getElementById('modal-' + userId).style.display = "none";
            });
        });

        // Cuando el usuario hace clic en cualquier lugar fuera del modal, cerrar el modal
        window.onclick = function(event) {
            if (event.target.classList.contains('modal')) {
                event.target.style.display = "none";
            }
        }
    });
</script>
</body>
</html>