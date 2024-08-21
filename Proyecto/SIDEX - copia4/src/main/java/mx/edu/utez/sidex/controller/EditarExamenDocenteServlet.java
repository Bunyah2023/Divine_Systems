package mx.edu.utez.sidex.controller;

import mx.edu.utez.sidex.dao.ExamenDao;
import mx.edu.utez.sidex.model.Examen;
import mx.edu.utez.sidex.model.Pregunta;
import mx.edu.utez.sidex.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/editarExamenDocente")
public class EditarExamenDocenteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Iniciando proceso de edición de examen...");

        HttpSession session = request.getSession();
        User usuario = (User) session.getAttribute("user");

        if (usuario != null && usuario.getRolId() == 2) { // Verifica que el usuario es docente
            System.out.println("Usuario autenticado como docente: " + usuario.getNombres());

            Examen examen;
            try {
                examen = extraerDatosExamen(request);  // Extrae los datos del examen
            } catch (ServletException e) {
                session.setAttribute("errorMessage", e.getMessage());
                response.sendRedirect("editarExamen.jsp");
                return;
            }

            List<Pregunta> preguntas = recogerPreguntas(request);  // Recoge las preguntas del formulario

            int claseId = examen.getClaseId(); // Obtén el clase_id desde el examen
            int creadorId = usuario.getId(); // Obtén el creador_id desde la sesión del usuario

            ExamenDao examenDao = new ExamenDao();
            boolean actualizado = examenDao.actualizarExamenDocente(examen, preguntas, creadorId, claseId);  // Actualiza el examen y sus preguntas, pasando el creador_id y clase_id

            if (actualizado) {
                System.out.println("Examen editado exitosamente: " + examen.getTitulo());
                session.setAttribute("registerMessage", "Examen editado con éxito.");
                session.setAttribute("messageType", "success");
            } else {
                System.out.println("Error al editar el examen: " + examen.getTitulo());
                session.setAttribute("registerMessage", "Error al editar el examen.");
                session.setAttribute("messageType", "error");
            }

            response.sendRedirect("index-docente.jsp");

        } else {
            System.out.println("Acceso denegado: El usuario no es docente o no está autenticado.");
            session.setAttribute("errorMessage", "No tienes permiso para editar exámenes.");
            response.sendRedirect("acceso_denegado.jsp");
        }
    }

    private Examen extraerDatosExamen(HttpServletRequest request) throws ServletException {
        try {
            int examenId = Integer.parseInt(request.getParameter("examen_id"));
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String materia = request.getParameter("materia");

            if (materia == null || materia.trim().isEmpty()) {
                throw new ServletException("El campo materia no puede estar vacío.");
            }

            String startDateStr = request.getParameter("fechaApertura");
            String endDateStr = request.getParameter("fechaCierre");
            Timestamp startDate = null;
            Timestamp endDate = null;

            if (startDateStr != null && !startDateStr.isEmpty()) {
                try {
                    startDate = Timestamp.valueOf(startDateStr.replace("T", " ") + ":00");
                    System.out.println("Fecha y hora de apertura procesada: " + startDate);
                } catch (IllegalArgumentException e) {
                    throw new ServletException("Error en el formato de la fecha y hora de apertura.", e);
                }
            } else {
                throw new ServletException("La fecha y hora de apertura no puede estar vacía.");
            }

            if (endDateStr != null && !endDateStr.isEmpty()) {
                try {
                    endDate = Timestamp.valueOf(endDateStr.replace("T", " ") + ":00");
                    System.out.println("Fecha y hora de cierre procesada: " + endDate);
                } catch (IllegalArgumentException e) {
                    throw new ServletException("Error en el formato de la fecha y hora de cierre.", e);
                }
            } else {
                throw new ServletException("La fecha y hora de cierre no puede estar vacía.");
            }

            Integer intentos = null;
            String intentosStr = request.getParameter("intentos");
            if (intentosStr != null && !intentosStr.isEmpty()) {
                try {
                    intentos = Integer.parseInt(intentosStr);
                } catch (NumberFormatException e) {
                    throw new ServletException("Número de intentos no válido.", e);
                }
            }

            // Obtener claseId desde la sesión
            HttpSession session = request.getSession();
            Object claseIdObj = session.getAttribute("claseId");
            if (claseIdObj == null) {
                throw new ServletException("No se encontró el Clase ID en la sesión.");
            }
            int claseId = Integer.parseInt(claseIdObj.toString());

            System.out.println("Datos extraídos del examen: " + titulo + ", Fecha y Hora Apertura: " + startDate + ", Fecha y Hora Cierre: " + endDate + ", Materia: " + materia + ", Clase ID: " + claseId);

            return new Examen(examenId, titulo, null, null, startDate, endDate, claseId, descripcion, "pendiente", 0.0, 0.0, materia, intentos, false);
        } catch (IllegalArgumentException e) {
            throw new ServletException("Error al parsear las fechas del examen o el ID del examen.", e);
        }
    }


    private List<Pregunta> recogerPreguntas(HttpServletRequest request) {
        List<Pregunta> preguntas = new ArrayList<>();

        String[] preguntaIds = request.getParameterValues("preguntaId[]");
        String[] textos = request.getParameterValues("preguntaTexto[]");
        String[] opcion1s = request.getParameterValues("opcion1[]");
        String[] opcion2s = request.getParameterValues("opcion2[]");
        String[] opcion3s = request.getParameterValues("opcion3[]");
        String[] opcion4s = request.getParameterValues("opcion4[]");
        String[] correctas = request.getParameterValues("respuestaCorrecta[]");

        if (textos != null && opcion1s != null && opcion2s != null && opcion3s != null && opcion4s != null && correctas != null) {
            for (int i = 0; i < textos.length; i++) {
                String texto = textos[i];
                String opcion1 = opcion1s[i];
                String opcion2 = opcion2s[i];
                String opcion3 = opcion3s[i];
                String opcion4 = opcion4s[i];
                String correctaStr = correctas[i];

                System.out.println("Recogiendo pregunta " + (i + 1) + ":");
                System.out.println("Texto: " + texto);
                System.out.println("Opción 1: " + opcion1);
                System.out.println("Opción 2: " + opcion2);
                System.out.println("Opción 3: " + opcion3);
                System.out.println("Opción 4: " + opcion4);
                System.out.println("Respuesta correcta: " + correctaStr);

                if (texto != null && !texto.trim().isEmpty() &&
                        opcion1 != null && !opcion1.trim().isEmpty() &&
                        opcion2 != null && !opcion2.trim().isEmpty() &&
                        opcion3 != null && !opcion3.trim().isEmpty() &&
                        opcion4 != null && !opcion4.trim().isEmpty() &&
                        correctaStr != null && !correctaStr.trim().isEmpty()) {

                    try {
                        int respuestaCorrecta = Integer.parseInt(correctaStr);
                        Pregunta pregunta = new Pregunta(Integer.parseInt(preguntaIds[i]), texto, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta, 0);
                        preguntas.add(pregunta);
                        System.out.println("Pregunta " + (i + 1) + " añadida exitosamente.");
                    } catch (NumberFormatException e) {
                        System.out.println("Error al convertir la respuesta correcta a un número: " + correctaStr);
                    }
                } else {
                    System.out.println("Pregunta " + (i + 1) + " no añadida. Falta información o hay campos vacíos.");
                }
            }
        } else {
            System.out.println("No se recibieron parámetros de preguntas o están incompletos.");
        }

        System.out.println("Total de preguntas recogidas: " + preguntas.size());
        return preguntas;
    }
}

