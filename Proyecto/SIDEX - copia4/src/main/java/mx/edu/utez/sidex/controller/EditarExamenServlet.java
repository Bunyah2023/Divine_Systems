package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.sidex.dao.ExamenDao;
import mx.edu.utez.sidex.model.Examen;
import mx.edu.utez.sidex.model.Pregunta;
import mx.edu.utez.sidex.model.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/editarExamen")
public class EditarExamenServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Iniciando edición de examen...");

        try {
            // Obtener y validar parámetros de la solicitud
            int examenId = Integer.parseInt(request.getParameter("examenId"));
            int claseId = Integer.parseInt(request.getParameter("claseId"));
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            int rolId = Integer.parseInt(request.getParameter("rolId"));

            System.out.println("Datos recibidos: Examen ID = " + examenId + ", Clase ID = " + claseId + ", Título = " + titulo + ", Descripción = " + descripcion);

            Timestamp fechaHoraApertura = null;
            Timestamp fechaHoraCierre = null;

            String fechaAperturaStr = request.getParameter("fechaApertura");
            String fechaCierreStr = request.getParameter("fechaCierre");

            if (fechaAperturaStr != null && !fechaAperturaStr.isEmpty()) {
                fechaHoraApertura = Timestamp.valueOf(fechaAperturaStr.replace("T", " ") + ":00");
            }
            if (fechaCierreStr != null && !fechaCierreStr.isEmpty()) {
                fechaHoraCierre = Timestamp.valueOf(fechaCierreStr.replace("T", " ") + ":00");
            }

            // Captura de opciones de preguntas
            List<Pregunta> preguntas = new ArrayList<>();
            for (int i = 1; i <= 30; i++) {
                String texto = request.getParameter("preguntaTexto" + i);
                if (texto != null && !texto.isEmpty()) {
                    String opcion1 = request.getParameter("opcion" + i + "1");
                    String opcion2 = request.getParameter("opcion" + i + "2");
                    String opcion3 = request.getParameter("opcion" + i + "3");
                    String opcion4 = request.getParameter("opcion" + i + "4");
                    int respuestaCorrecta = Integer.parseInt(request.getParameter("respuestaCorrecta" + i));
                    preguntas.add(new Pregunta(texto, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta, examenId));
                }
            }

            User usuario = (User) request.getSession().getAttribute("user");
            int creadorId = usuario.getId();

            ExamenDao examenDao = new ExamenDao();

            if (rolId == 4) { // Coordinador
                System.out.println("Actualizando examen en la tabla examenes...");
                examenDao.actualizarExamen(examenId, titulo, descripcion, fechaHoraApertura, fechaHoraCierre, claseId, creadorId);
            } else if (rolId == 2) { // Docente
                if (examenDao.examenEditadoExiste(examenId, creadorId)) {
                    System.out.println("Actualizando examen en ExamenesEditadosPorDocentes...");
                    examenDao.actualizarExamenEditado(examenId, titulo, descripcion, fechaHoraApertura, fechaHoraCierre, claseId, creadorId);
                } else {
                    System.out.println("Insertando nuevo examen en ExamenesEditadosPorDocentes...");
                    Examen examenEditado = new Examen(examenId, titulo, null, null, fechaHoraApertura, fechaHoraCierre, claseId, descripcion, "pendiente", 0.0, 0.0, "", null, false, creadorId);
                    examenDao.crearExamenEditadoPorDocente(examenEditado, preguntas, creadorId);
                }
            }

            System.out.println("Edición de examen completada.");

            response.sendRedirect("detalleClase.jsp?claseId=" + claseId);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?mensaje=Ha ocurrido un error al editar el examen.");
        }
    }
}
