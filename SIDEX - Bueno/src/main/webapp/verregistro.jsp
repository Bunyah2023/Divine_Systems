<%@ page import="mx.edu.utez.sidex.dao.UserDao" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="mx.edu.utez.sidex.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Lista de Usuarios</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/datatables.css">
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
            <th>Correo</th>
            <th>Estado</th>
            <th>Modificar</th>
            <th>Eliminar</th>
        </tr>
        </thead>
        <tbody>
        <%
            UserDao dao = new UserDao();
            ArrayList<User> lista = dao.getAll();
            if (lista != null && !lista.isEmpty()) {
                for(User u : lista) { %>
        <tr>
            <td><%=u.getId()%></td>
            <td><%=u.getNombre()%></td>
            <td><%=u.getApellido() + " " + u.getApellido2()%></td>
            <td><%=u.getCorreo()%></td>
            <td><%=u.isEstado() ? "Activo" : "Inactivo"%></td>
            <td><a href="login?id=<%=u.getId()%>" class="btn btn-warning">Actualizar</a></td>
            <td><a href="delete?id=<%=u.getId()%>" class="btn btn-danger">Eliminar</a></td>
        </tr>
        <% }
        } else { %>
        <tr>
            <td colspan="7" class="text-center">No hay usuarios registrados.</td>
        </tr>
        <% }
        %>
        </tbody>
    </table>
</div>
<script src="${pageContext.request.contextPath}/JS/jquery-3.7.0.js"></script>
<script src="${pageContext.request.contextPath}/JS/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/JS/datatables.js"></script>
<script src="${pageContext.request.contextPath}/JS/dataTables.bootstrap5.js"></script>
<script src="${pageContext.request.contextPath}/JS/es-MX.json"></script>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const table = document.getElementById('example');
        new DataTable(table, {
            language: {
                url: '${pageContext.request.contextPath}/JS/es-MX.json'
            }
        });
    });
</script>
</body>
</html>
