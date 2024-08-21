package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.sidex.dao.ExamenDao;
import mx.edu.utez.sidex.dao.PreguntaDao;
import mx.edu.utez.sidex.model.Examen;
import mx.edu.utez.sidex.model.Pregunta;
import mx.edu.utez.sidex.model.User;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "GuardarExamenServlet", urlPatterns = {"/guardarExamen"})
public class GuardarExamenServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener parámetros del formulario
            String examenIdStr = request.getParameter("examenId");
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String fechaAperturaStr = request.getParameter("fechaApertura");
            String fechaCierreStr = request.getParameter("fechaCierre");

            if (examenIdStr == null || titulo == null || descripcion == null || fechaAperturaStr == null || fechaCierreStr == null) {
                response.getWriter().write("Faltan parámetros requeridos.");
                return;
            }

            int examenId = Integer.parseInt(examenIdStr);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date fechaApertura = dateFormat.parse(fechaAperturaStr);
            Date fechaCierre = dateFormat.parse(fechaCierreStr);

            // Obtener usuario de la sesión
            User usuario = (User) request.getSession().getAttribute("user");
            if (usuario == null) {
                response.getWriter().write("Usuario no autenticado.");
                return;
            }

            int creadorId = usuario.getId();
            int rolId = usuario.getRolId();

            ExamenDao examenDao = new ExamenDao();
            PreguntaDao preguntaDao = new PreguntaDao();
            Examen examen = examenDao.obtenerPorId(examenId);
            List<Pregunta> preguntas = preguntaDao.obtenerPreguntasPorExamenId(examenId);

            if (examen == null) {
                response.getWriter().write("Examen no encontrado.");
                return;
            }

            if (rolId == 2) { // Verificar si el rol del usuario es 2 (docente)
                // Convertir java.util.Date a java.sql.Date para guardar en la base de datos
                java.sql.Date sqlFechaApertura = new java.sql.Date(fechaApertura.getTime());
                java.sql.Date sqlFechaCierre = new java.sql.Date(fechaCierre.getTime());

                // Guardar el examen editado por el docente en la tabla ExamenesEditadosPorDocentes
                boolean examenGuardado = examenDao.guardarExamenEditadoPorDocente(examenId, titulo, descripcion, sqlFechaApertura, sqlFechaCierre, creadorId);

                if (!examenGuardado) {
                    response.getWriter().write("Error al guardar el examen editado.");
                    return;
                }

                // Guardar las preguntas editadas en la tabla ExamenesEditadosPorDocentes
                for (Pregunta pregunta : preguntas) {
                    String texto = request.getParameter("preguntaTexto" + pregunta.getId());
                    String opcion1 = request.getParameter("opcion" + pregunta.getId() + "1");
                    String opcion2 = request.getParameter("opcion" + pregunta.getId() + "2");
                    String opcion3 = request.getParameter("opcion" + pregunta.getId() + "3");
                    String opcion4 = request.getParameter("opcion" + pregunta.getId() + "4");
                    int respuestaCorrecta = Integer.parseInt(request.getParameter("respuestaCorrecta" + pregunta.getId()));

                    boolean preguntaGuardada = examenDao.guardarPreguntaEditadaPorDocente(examenId, pregunta.getId(), texto, opcion1, opcion2, opcion3, opcion4, respuestaCorrecta, usuario.getRolId());

                    if (!preguntaGuardada) {
                        response.getWriter().write("Error al guardar la pregunta editada.");
                        return;
                    }
                }

                response.sendRedirect("detalleClase.jsp?claseId=" + examen.getClaseId() + "&mensaje=exito");

            } else {
                // Aquí debería ir la lógica para otros roles
                response.getWriter().write("Este usuario no tiene permisos para editar exámenes.");
                return;
            }
        } catch (ParseException e) {
            response.getWriter().write("Error en el formato de fecha: " + e.getMessage());
        } catch (Exception e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}



