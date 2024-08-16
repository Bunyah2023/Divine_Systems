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

@WebServlet("/crearExamen")
public class CrearExamenServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Iniciando proceso de creación de examen...");

        HttpSession session = request.getSession();
        User usuario = (User) session.getAttribute("user");

        if (usuario != null && usuario.getRolId() == 4) { // Verifica que el usuario es coordinador
            System.out.println("Usuario autenticado como coordinador: " + usuario.getNombres());

            Examen examen;
            try {
                examen = extraerDatosExamen(request);
            } catch (ServletException e) {
                session.setAttribute("errorMessage", e.getMessage());
                response.sendRedirect("crearExamen.jsp");
                return;
            }

            List<Pregunta> preguntas = recogerPreguntas(request);

            ExamenDao examenDao = new ExamenDao();
            boolean creado = examenDao.crearExamen(examen, preguntas);

            if (creado) {
                System.out.println("Examen creado exitosamente: " + examen.getTitulo());
                session.setAttribute("registerMessage", "Examen creado con éxito.");
                session.setAttribute("messageType", "success");
            } else {
                System.out.println("Error al crear el examen: " + examen.getTitulo());
                session.setAttribute("registerMessage", "Error al crear el examen.");
                session.setAttribute("messageType", "error");
            }

            response.sendRedirect("index-coordinador.jsp");

        } else {
            System.out.println("Acceso denegado: El usuario no es coordinador o no está autenticado.");
            session.setAttribute("errorMessage", "No tienes permiso para crear exámenes.");
            response.sendRedirect("acceso_denegado.jsp");
        }
    }

    private Examen extraerDatosExamen(HttpServletRequest request) throws ServletException {
        try {
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String materia = request.getParameter("materia"); // Asegúrate de que este valor no sea nulo o vacío

            // Verifica que materia no sea nulo ni vacío
            if (materia == null || materia.trim().isEmpty()) {
                throw new ServletException("El campo materia no puede estar vacío.");
            }

            // Otros parámetros y procesamiento...
            String startDateStr = request.getParameter("fechaHoraApertura");
            String endDateStr = request.getParameter("fechaHoraCierre");
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

            System.out.println("Datos extraídos del examen: " + titulo + ", Fecha y Hora Apertura: " + startDate + ", Fecha y Hora Cierre: " + endDate + ", Materia: " + materia);

            return new Examen(0, titulo, null, null, startDate, endDate, 0, descripcion, "pendiente", 0.0, 0.0, materia, intentos, false);
        } catch (IllegalArgumentException e) {
            throw new ServletException("Error al parsear las fechas del examen.", e);
        }
    }


    private List<Pregunta> recogerPreguntas(HttpServletRequest request) {
        List<Pregunta> preguntas = new ArrayList<>();
        String[] textosPreguntas = request.getParameterValues("pregunta");
        int cantidadPreguntas = textosPreguntas != null ? textosPreguntas.length : 0;

        for (int i = 0; i < cantidadPreguntas; i++) {
            String opcion1 = request.getParameter("opcion" + (i + 1) + "_1");
            String opcion2 = request.getParameter("opcion" + (i + 1) + "_2");
            String opcion3 = request.getParameter("opcion" + (i + 1) + "_3");
            String opcion4 = request.getParameter("opcion" + (i + 1) + "_4");
            String correctaStr = request.getParameter("correcta" + (i + 1));

            int respuestaCorrecta = 1; // valor predeterminado en caso de que no se reciba correctaStr
            if (correctaStr != null) {
                try {
                    respuestaCorrecta = Integer.parseInt(correctaStr);
                } catch (NumberFormatException e) {
                    // En caso de que no se pueda convertir a número, usar el valor por defecto
                }
            }

            // Verificar que las opciones no sean nulas o vacías
            if (opcion1 == null || opcion1.trim().isEmpty()) {
                opcion1 = "Opción 1 no especificada";
            }
            if (opcion2 == null || opcion2.trim().isEmpty()) {
                opcion2 = "Opción 2 no especificada";
            }
            if (opcion3 == null || opcion3.trim().isEmpty()) {
                opcion3 = "Opción 3 no especificada";
            }
            if (opcion4 == null || opcion4.trim().isEmpty()) {
                opcion4 = "Opción 4 no especificada";
            }

            // Ahora creamos la pregunta con las opciones recogidas
            preguntas.add(new Pregunta(textosPreguntas[i], opcion1, opcion2, opcion3, opcion4, respuestaCorrecta, 0));

            // Imprimir para depurar
            System.out.println("Pregunta recogida: " + textosPreguntas[i] +
                    " | Opciones: [" + opcion1 + ", " + opcion2 + ", " + opcion3 + ", " + opcion4 + "] | Respuesta Correcta: " + respuestaCorrecta);
        }

        System.out.println("Total de preguntas recogidas: " + preguntas.size());
        return preguntas;
    }
}











