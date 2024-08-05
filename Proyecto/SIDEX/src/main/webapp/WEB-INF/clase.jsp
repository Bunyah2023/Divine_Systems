<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mx.edu.utez.sidex.model.Clase" %>
<%@ page import="mx.edu.utez.sidex.dao.ClaseDao" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clase Detalle</title>
    <link rel="stylesheet" href="CSS/bootstrap.min.css">
    <style>
        body { padding: 20px; }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .class-info {
            font-size: 24px;
            color: #333;
        }
        .options {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <span class="class-info">Nombre de la Clase: ${clase.nombre}</span>
        <span class="class-code">Código de la Clase: ${clase.codigo}</span>
    </div>
    <div class="options">
        <button class="btn btn-primary">Agregar Examen</button>
        <button class="btn btn-secondary">Ver Estudiantes</button>
    </div>
    <div>
        <h2>Actividades</h2>
        <!-- Lista de actividades -->
    </div>
    <div>
        <h2>Exámenes</h2>
        <!-- Lista de exámenes -->
    </div>
    <div>
        <h2>Estudiantes Inscritos</h2>
        <!-- Lista de estudiantes -->
    </div>
</div>
</body>
</html>
