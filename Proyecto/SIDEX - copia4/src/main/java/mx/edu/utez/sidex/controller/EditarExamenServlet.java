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

    private static final int MAX_PREGUNTAS = 30;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Iniciando edición de examen...");

        try {
            // Obtener y validar parámetros de la solicitud
            int examenId = Integer.parseInt(request.getParameter("examenId"));
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            int rolId = Integer.parseInt(request.getParameter("rolId"));
            int intentos = Integer.parseInt(request.getParameter("intentos"));

            System.out.println("Datos recibidos: Examen ID = " + examenId + ", Título = " + titulo + ", Descripción = " + descripcion + ", Intentos = " + intentos);

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

            // Verificación de valores nulos o vacíos para usar los valores actuales si no son modificados
            ExamenDao examenDao = new ExamenDao();
            Examen examenActual = examenDao.obtenerPorId(examenId);

            if (titulo == null || titulo.trim().isEmpty()) {
                titulo = examenActual.getTitulo();
            }
            if (descripcion == null || descripcion.trim().isEmpty()) {
                descripcion = examenActual.getDescripcion();
            }
            if (fechaHoraApertura == null) {
                fechaHoraApertura = examenActual.getFechaHoraApertura();
            }
            if (fechaHoraCierre == null) {
                fechaHoraCierre = examenActual.getFechaHoraCierre();
            }
            if (intentos == 0) {
                intentos = examenActual.getIntentos() != null ? examenActual.getIntentos() : 0;
            }

            // Captura de opciones de preguntas
            List<Pregunta> preguntas = new ArrayList<>();
            for (int i = 1; i <= MAX_PREGUNTAS; i++) {
                String texto = request.getParameter("preguntaTexto" + i);
                if (texto != null && !texto.isEmpty()) {
                    String opcion1 = request.getParameter("opcion" + i + "1");
                    String opcion2 = request.getParameter("opcion" + i + "2");
                    String opcion3 = request.getParameter("opcion" + i + "3");
                    String opcion4 = request.getParameter("opcion" + i + "4");

                    // Validar que ninguna opción sea nula o vacía antes de agregar la pregunta
                    if (opcion1 != null && !opcion1.isEmpty() &&
                            opcion2 != null && !opcion2.isEmpty() &&
                            opcion3 != null && !opcion3.isEmpty() &&
                            opcion4 != null && !opcion4.isEmpty()) {

                        int respuestaCorrecta = Integer.parseInt(request.getParameter("respuestaCorrecta" + i));
                        preguntas.add(new Pregunta(texto, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta, examenId));
                    } else {
                        System.out.println("Error: Una o más opciones de la pregunta " + i + " son nulas o vacías.");
                    }
                }
            }

            User usuario = (User) request.getSession().getAttribute("user");
            int creadorId = usuario.getId();
            int claseId = examenActual.getClaseId();

            if (rolId == 4) { // Coordinador
                System.out.println("Actualizando examen en la tabla examenes...");
                examenDao.actualizarExamen(examenId, titulo, descripcion, fechaHoraApertura, fechaHoraCierre, claseId, creadorId);
            } else if (rolId == 2) { // Docente
                if (examenDao.examenEditadoExiste(examenId, creadorId)) {
                    System.out.println("Actualizando examen en ExamenesEditadosPorDocentes...");
                    examenDao.actualizarExamenEditado(examenId, titulo, descripcion, fechaHoraApertura, fechaHoraCierre, intentos, claseId, creadorId, preguntas);
                } else {
                    System.out.println("Insertando nuevo examen en ExamenesEditadosPorDocentes...");
                    Examen examenEditado = new Examen(examenId, titulo, null, null, fechaHoraApertura, fechaHoraCierre, claseId, descripcion, "pendiente", 0.0, 0.0, examenActual.getMateria(), intentos, false, creadorId);
                    examenDao.crearExamenEditadoPorDocente(examenEditado, preguntas, creadorId);
                }

                // Activar el examen si el docente lo ha editado
                examenDao.activarExamenParaDocente(examenId, creadorId);
            }

            System.out.println("Edición de examen completada.");

            // Redirigir al docente a la página de la clase donde aparece el examen en "exámenes en curso"
            response.sendRedirect("detalleClase.jsp?claseId=" + claseId + "&examenEnCursoId=" + examenId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?mensaje=Formato de número inválido.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?mensaje=Ha ocurrido un error al editar el examen.");
        }
    }
}






