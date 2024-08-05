<%@ page import="mx.edu.utez.sidex.dao.UserDao" %>
<%@ page import="java.util.List" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Usuarios</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/datatables.css">
</head>
<body>
<div class="container">
    <h1>Lista de Usuarios</h1>
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
            <th>Modificar</th>
            <th>Eliminar</th>
        </tr>
        </thead>
        <tbody>
        <%
            UserDao dao = new UserDao();
            List<User> lista = dao.getAllUsers();
            if (lista != null && !lista.isEmpty()) {
                for(User u : lista) { %>
        <tr>
            <form method="post" action="modificarUsuario">
                <td><input type="text" name="id" value="<%=u.getId()%>" readonly class="form-control"></td>
                <td><input type="text" name="nombres" value="<%=u.getNombres()%>" class="form-control" disabled></td>
                <td><input type="text" name="apellido" value="<%=u.getApellido()%>" class="form-control" disabled></td>
                <td><input type="text" name="apellidoMaterno" value="<%=u.getApellidoMaterno()%>" class="form-control" disabled></td>
                <td><input type="email" name="correo" value="<%=u.getCorreo()%>" class="form-control" disabled></td>
                <td>
                    <select name="rolId" class="form-control" disabled>
                        <option value="1" <%=u.getRolId() == 1 ? "selected" : ""%>>Estudiante</option>
                        <option value="2" <%=u.getRolId() == 2 ? "selected" : ""%>>Docente</option>
                        <option value="3" <%=u.getRolId() == 3 ? "selected" : ""%>>Admin</option>
                    </select>
                </td>
                <td>
                    <select name="estado" class="form-control" disabled>
                        <option value="true" <%=u.isEstado() ? "selected" : ""%>>Activo</option>
                        <option value="false" <%=!u.isEstado() ? "selected" : ""%>>Inactivo</option>
                    </select>
                </td>
                <td><button type="button" class="btn btn-warning edit-btn">Modificar</button></td>
            </form>
            <td><button type="button" class="btn btn-danger delete-btn" data-id="<%=u.getId()%>">Eliminar</button></td>
        </tr>
        <% }
        } else { %>
        <tr>
            <td colspan="9" class="text-center">No hay usuarios registrados.</td>
        </tr>
        <% }
        %>
        </tbody>
    </table>
</div>

<!-- Modal de Confirmación para Eliminar -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteModalLabel">Confirmar Eliminación</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                ¿Estás seguro de que deseas eliminar este usuario?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-danger" id="confirmDelete">Eliminar</button>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/JS/jquery-3.7.0.js"></script>
<script src="${pageContext.request.contextPath}/JS/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/JS/datatables.js"></script>
<script src="${pageContext.request.contextPath}/JS/dataTables.bootstrap5.js"></script>
<script src="${pageContext.request.contextPath}/JS/es-MX.json"></script>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        $('#example').DataTable({
            language: {
                url: '${pageContext.request.contextPath}/JS/es-MX.json'
            }
        });

        // Habilitar edición al hacer clic en el botón "Modificar"
        document.querySelectorAll('.edit-btn').forEach(button => {
            button.addEventListener('click', function() {
                const form = this.closest('form');
                const inputs = form.querySelectorAll('input, select');

                inputs.forEach(input => {
                    if (input.name !== 'id') { // No habilitar el campo de ID
                        input.disabled = false;
                    }
                });

                // Cambiar el botón para que permita guardar
                this.textContent = 'Guardar';
                this.type = 'submit';
                this.classList.remove('btn-warning');
                this.classList.add('btn-success');
            });
        });

        // Manejar eliminación de usuario con el modal
        let userIdToDelete = null;
        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', function() {
                userIdToDelete = this.getAttribute('data-id');
                $('#deleteModal').modal('show');
            });
        });

        document.getElementById('confirmDelete').addEventListener('click', function() {
            if (userIdToDelete !== null) {
                window.location.href = `eliminarUsuario?id=${userIdToDelete}`;
            }
        });
    });
</script>
</body>
</html>
