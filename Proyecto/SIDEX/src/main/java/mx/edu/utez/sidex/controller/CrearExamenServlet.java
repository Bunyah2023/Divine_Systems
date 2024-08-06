package mx.edu.utez.sidex.controller;

import mx.edu.utez.sidex.dao.ExamenDao;
import mx.edu.utez.sidex.dao.PreguntaDao;
import mx.edu.utez.sidex.model.Examen;
import mx.edu.utez.sidex.model.Pregunta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        // Obtener parámetros del formulario
        String titulo = request.getParameter("examTitle");
        String descripcion = request.getParameter("examDescription");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String claseIdStr = request.getParameter("claseId");

        // Imprimir parámetros para depuración
        System.out.println("Título: " + titulo);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Fecha de inicio (String): " + startDateStr);
        System.out.println("Fecha de fin (String): " + endDateStr);
        System.out.println("ID de clase: " + claseIdStr);

        // Parsear fechas
        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = dateFormat.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = dateFormat.parse(endDateStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Imprimir fechas parseadas para depuración
        System.out.println("Fecha de inicio: " + startDate);
        System.out.println("Fecha de fin: " + endDate);

        // Obtener el ID de la clase
        int claseId = claseIdStr != null && !claseIdStr.isEmpty() ? Integer.parseInt(claseIdStr) : 0;

        // Crear el examen y obtener el ID generado
        Examen examen = new Examen(0, titulo != null ? titulo : "Examen sin título", startDate, endDate, claseId, descripcion, "pendiente", 0.0);
        ExamenDao examenDao = new ExamenDao();
        boolean success = examenDao.crearExamen(examen, new ArrayList<>());

        // Recopilar preguntas del formulario
        if (success) {
            int examenId = examenDao.obtenerUltimoExamenId();
            List<Pregunta> preguntas = new ArrayList<>();
            for (int i = 1; i <= 30; i++) {
                String texto = request.getParameter("pregunta" + i);
                String opcion1 = request.getParameter("opcion" + i + "1");
                String opcion2 = request.getParameter("opcion" + i + "2");
                String opcion3 = request.getParameter("opcion" + i + "3");
                String opcion4 = request.getParameter("opcion" + i + "4");
                String respuestaCorrectaStr = request.getParameter("respuestaCorrecta" + i);
                int respuestaCorrecta = respuestaCorrectaStr != null && !respuestaCorrectaStr.isEmpty() ? Integer.parseInt(respuestaCorrectaStr) : 1;

                if (texto != null && !texto.trim().isEmpty()) {
                    preguntas.add(new Pregunta(texto, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta, examenId));
                }
            }

            // Insertar preguntas
            PreguntaDao preguntaDao = new PreguntaDao();
            boolean preguntasInsertadas = preguntaDao.crearPreguntas(preguntas);

            if (preguntasInsertadas) {
                request.getSession().setAttribute("message", "Examen y preguntas creados exitosamente.");
            } else {
                request.getSession().setAttribute("errorMessage", "Examen creado, pero error al guardar las preguntas.");
            }
        } else {
            request.getSession().setAttribute("errorMessage", "Error al crear el examen.");
        }

        // Redirigir de vuelta a la página de detalle de clase
        response.sendRedirect("detalleClase.jsp?claseId=" + claseId);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el ID del examen a eliminar
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

        // Redirigir de vuelta a la página de detalle de clase
        response.sendRedirect("detalleClase.jsp?claseId=" + request.getParameter("claseId"));
    }
}
