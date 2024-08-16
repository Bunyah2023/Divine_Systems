<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="mx.edu.utez.sidex.dao.ExamenDao" %>
<%@ page import="mx.edu.utez.sidex.model.Examen" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Almacén de Exámenes - SIDEX</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/almacendeexamenes.css">
</head>
<body>
<div class="container">
    <h1>Almacén de Exámenes</h1>

    <!-- Tabla de exámenes -->
    <div id="tablaExamenes">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>No.</th>
                <th>Título</th>
                <th>Materia</th>
                <th>Fecha de Apertura</th>
                <th>Fecha de Cierre</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <%
                ExamenDao examenDao = new ExamenDao();
                List<Examen> examenes = examenDao.obtenerExamenes(null, null, null, null, null, null);

                if (examenes != null && !examenes.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    for (Examen examen : examenes) {
            %>
            <tr>
                <td><%= examen.getId() %></td>
                <td><%= examen.getTitulo() %></td>
                <td><%= examen.getMateria() %></td>
                <td><%= examen.getFechaHoraApertura() != null ? sdf.format(examen.getFechaHoraApertura()) : "N/A" %></td>
                <td><%= examen.getFechaHoraCierre() != null ? sdf.format(examen.getFechaHoraCierre()) : "N/A" %></td>
                <td><%= examen.getEstado() %></td>
                <td>
                    <a href="editarExamen.jsp?examenId=<%= examen.getId() %>&modo=almacen" class="btn btn-warning">Tomar</a>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="7">No hay exámenes disponibles.</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>

