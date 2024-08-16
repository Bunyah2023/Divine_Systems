package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.sidex.dao.ExamenDao;
import mx.edu.utez.sidex.model.User;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/editarExamen")
public class EditarExamenServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Iniciando edición de examen...");

        int examenId = Integer.parseInt(request.getParameter("examenId"));
        int claseId = Integer.parseInt(request.getParameter("claseId"));
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");

        System.out.println("Datos recibidos: Examen ID = " + examenId + ", Clase ID = " + claseId + ", Título = " + titulo + ", Descripción = " + descripcion);

        Timestamp fechaHoraApertura;
        Timestamp fechaHoraCierre;

        try {
            fechaHoraApertura = Timestamp.valueOf(request.getParameter("fecha_hora_apertura").replace("T", " ") + ":00");
            fechaHoraCierre = Timestamp.valueOf(request.getParameter("fecha_hora_cierre").replace("T", " ") + ":00");
        } catch (IllegalArgumentException e) {
            System.out.println("Error en el formato de fecha.");
            response.sendRedirect("error.jsp?mensaje=Formato de fecha no válido.");
            return;
        }

        // Captura de opciones de preguntas
        String opcion1 = request.getParameter("opcion1");
        String opcion2 = request.getParameter("opcion2");
        String opcion3 = request.getParameter("opcion3");
        String opcion4 = request.getParameter("opcion4");

        User usuario = (User) request.getSession().getAttribute("user");
        int creadorId = usuario.getId();

        ExamenDao examenDao = new ExamenDao();

        if (usuario.getRolId() == 2) { // Verificación de rol de docente
            if (examenDao.examenEditadoExiste(examenId, creadorId)) {
                System.out.println("Actualizando examen en ExamenesEditadosPorDocentes...");
                examenDao.actualizarExamenEditado(examenId, titulo, descripcion, fechaHoraApertura, fechaHoraCierre, claseId, creadorId);
            } else {
                System.out.println("Insertando nuevo examen en ExamenesEditadosPorDocentes...");
                examenDao.insertarExamenEditado(examenId, titulo, descripcion, fechaHoraApertura, fechaHoraCierre, claseId, creadorId, opcion1, opcion2, opcion3, opcion4);
            }
        } else { // Actualización para roles distintos de docente
            System.out.println("Actualizando examen en la tabla examenes...");
            examenDao.actualizarExamen(examenId, titulo, descripcion, fechaHoraApertura, fechaHoraCierre, claseId, creadorId);
        }

        System.out.println("Edición de examen completada.");

        response.sendRedirect("detalleClase.jsp?claseId=" + claseId);
    }
}
