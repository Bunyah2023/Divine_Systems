<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 31/07/2024
  Time: 05:04 p. m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ver Registro</title>
    <link rel="stylesheet" href="CSS/bootstrap.css">
    <link rel="stylesheet" href="CSS/verregistros.css">
</head>
<body>
<div class="titulo">
    <h1>Registro de usuarios</h1>
</div>

<div>
    <table id="registroTabla">
        <thead>
        <tr>
            <th>Nombre(s)</th>
            <th>Apellido</th>
            <th>Apellido Materno</th>
            <th>Correo</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <!-- Los datos se llenarán con JavaScript -->
        </tbody>
    </table>
</div>

<a href="index.jsp">Volver al inicio</a>
<script src="JS/llenar_registro.js"></script>
</body>
</html>

