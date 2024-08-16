package mx.edu.utez.sidex.controller;

import mx.edu.utez.sidex.dao.ExamenDao;
import mx.edu.utez.sidex.model.Examen;
import mx.edu.utez.sidex.model.Pregunta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/crearExamen")
public class CrearExamenServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "tempSave"; // Acción predeterminada si no se especifica
        }

        switch (action) {
            case "tempSave":
                guardarTemporalmente(request, response);
                break;
            case "confirm":
                confirmarExamen(request, response);
                break;
            default:
                response.sendRedirect("error.jsp"); // Página de error si la acción no es reconocida
        }
    }

    private void guardarTemporalmente(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Examen examen;
        try {
            examen = extraerDatosExamen(request);
        } catch (ServletException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect("crearExamen.jsp");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("examenTemporal", examen);
        List<Pregunta> preguntas = recogerPreguntas(request, 0); // Usar ID de examen temporal como 0
        session.setAttribute("preguntasTemporales", preguntas);
        response.sendRedirect("editarExamen.jsp"); // Página para editar el examen temporalmente guardado
    }

    private void confirmarExamen(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Examen examen = (Examen) session.getAttribute("examenTemporal");
        List<Pregunta> preguntas = (List<Pregunta>) session.getAttribute("preguntasTemporales");

        if (examen != null && preguntas != null) {
            ExamenDao examenDao = new ExamenDao();
            boolean creado = examenDao.crearExamen(examen, preguntas);
            if (creado) {
                session.removeAttribute("examenTemporal"); // Limpiar la sesión después de guardar definitivamente
                session.removeAttribute("preguntasTemporales");
                session.setAttribute("message", "Examen creado con éxito.");
                response.sendRedirect("almacenExamenes.jsp");
            } else {
                session.setAttribute("errorMessage", "Error al crear el examen.");
                response.sendRedirect("crearExamen.jsp"); // Volver a la página de creación si hay error
            }
        } else {
            session.setAttribute("errorMessage", "No se encontró el examen temporal en la sesión.");
            response.sendRedirect("crearExamen.jsp"); // Volver a la página de creación si no se encuentra el examen
        }
    }

    private Examen extraerDatosExamen(HttpServletRequest request) throws ServletException {
        try {
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");

            // Asegúrate de que las fechas no sean null antes de intentar parsearlas
            String startDateStr = request.getParameter("fechaApertura");
            String endDateStr = request.getParameter("fechaCierre");
            Date startDate = null;
            Date endDate = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = dateFormat.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = dateFormat.parse(endDateStr);
            }

            String materia = request.getParameter("materia");
            Integer intentos = null;
            String intentosStr = request.getParameter("intentos");
            if (intentosStr != null && !intentosStr.isEmpty()) {
                try {
                    intentos = Integer.parseInt(intentosStr);
                } catch (NumberFormatException e) {
                    throw new ServletException("Número de intentos no válido.", e);
                }
            }

            // Se asume que no se necesita el campo claseId
            return new Examen(0, titulo, startDate, endDate, null, null, 0, descripcion, "pendiente", 0.0, 0.0, materia, intentos, false);
        } catch (ParseException e) {
            throw new ServletException("Error al parsear las fechas del examen.", e);
        }
    }

    private List<Pregunta> recogerPreguntas(HttpServletRequest request, int examenId) {
        List<Pregunta> preguntas = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            String texto = request.getParameter("pregunta" + i);
            if (texto != null && !texto.isEmpty()) {
                String opcion1 = request.getParameter("opcion" + i + "1");
                String opcion2 = request.getParameter("opcion" + i + "2");
                String opcion3 = request.getParameter("opcion" + i + "3");
                String opcion4 = request.getParameter("opcion" + i + "4");
                int respuestaCorrecta;
                try {
                    respuestaCorrecta = Integer.parseInt(request.getParameter("correcta" + i));
                } catch (NumberFormatException e) {
                    respuestaCorrecta = 0; // Valor predeterminado si el número no es válido
                }
                preguntas.add(new Pregunta(texto, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta, examenId));
            }
        }
        return preguntas;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String examenIdStr = request.getParameter("examenId");
        int examenId = examenIdStr != null && !examenIdStr.isEmpty() ? Integer.parseInt(examenIdStr) : 0;

        if (examenId > 0) {
            ExamenDao examenDao = new ExamenDao();
            boolean eliminado = examenDao.eliminarExamen(examenId);

            if (eliminado) {
                request.getSession().setAttribute("message", "Examen eliminado exitosamente.");
            } else {
                request.getSession().setAttribute("errorMessage", "Error al eliminar el examen.");
            }
        } else {
            request.getSession().setAttribute("errorMessage", "ID de examen inválido.");
        }

        response.sendRedirect("almacenExamenes.jsp");
    }
}
