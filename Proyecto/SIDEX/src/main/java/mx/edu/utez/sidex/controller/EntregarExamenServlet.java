package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.sidex.dao.ExamenDao;
import mx.edu.utez.sidex.dao.ResultadoDao;
import mx.edu.utez.sidex.model.Resultado;
import mx.edu.utez.sidex.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/entregarExamen")
public class EntregarExamenServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int examenId = Integer.parseInt(request.getParameter("examenId"));
        HttpSession session = request.getSession();

        // Obtener el objeto User de la sesión
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Redirigir a una página de error o mostrar un mensaje adecuado
            response.sendRedirect("error.jsp?mensaje=No%20se%20encontr%F3%20el%20usuario%20en%20la%20sesi%F3n.");
            return;
        }

        int estudianteId = user.getId(); // Obtener el ID del estudiante desde el objeto User

        // Obtener respuestas del formulario
        Map<Integer, Integer> respuestas = obtenerRespuestasDelFormulario(request);

        ExamenDao examenDao = new ExamenDao();
        ResultadoDao resultadoDao = new ResultadoDao();

        // Obtener ID de clase del examen
        int claseId = examenDao.obtenerClaseIdPorExamen(examenId);

        // Calcular resultados
        Resultado resultado = calcularResultado(examenId, respuestas, estudianteId, claseId, examenDao, user);

        // Guardar resultado
        resultadoDao.guardarResultado(resultado);

        // Redirigir al usuario a la página de resultados
        response.sendRedirect("verExamen.jsp?examenId=" + examenId);
    }

    private Map<Integer, Integer> obtenerRespuestasDelFormulario(HttpServletRequest request) {
        Map<Integer, Integer> respuestas = new HashMap<>();
        // Extraer respuestas del formulario (asumiendo parámetros como "respuesta[1]", "respuesta[2]", etc.)
        request.getParameterMap().forEach((key, value) -> {
            if (key.startsWith("respuesta")) {
                int preguntaId = Integer.parseInt(key.replace("respuesta", ""));
                int respuestaSeleccionada = Integer.parseInt(value[0]);
                respuestas.put(preguntaId, respuestaSeleccionada);
            }
        });
        return respuestas;
    }

    private Resultado calcularResultado(int examenId, Map<Integer, Integer> respuestas, int estudianteId, int claseId, ExamenDao examenDao, User user) {
        int totalPreguntas = examenDao.obtenerTotalPreguntasPorExamen(examenId);

        int aciertos = 0;
        int respuestasIncorrectas = 0;

        // Obtener respuestas correctas de la base de datos
        Map<Integer, Integer> respuestasCorrectas = examenDao.obtenerRespuestasCorrectasPorExamen(examenId);

        for (Map.Entry<Integer, Integer> entry : respuestas.entrySet()) {
            int preguntaId = entry.getKey();
            int respuestaSeleccionada = entry.getValue();
            if (respuestasCorrectas.get(preguntaId) == respuestaSeleccionada) {
                aciertos++;
            } else {
                respuestasIncorrectas++;
            }
        }

        double calificacion = (aciertos / (double) totalPreguntas) * 100;

        // Obtener los rangos de calificación desde la tabla de clases
        Map<String, Double> rangosCalificacion = examenDao.obtenerRangosCalificacionPorClase(claseId);

        // Obtener la categoría de calificación
        String categoria = obtenerCalificacionCategoria(calificacion, rangosCalificacion);

        // Crear el objeto Resultado
        boolean aprobado = categoria.equals("AU") || categoria.equals("DE");
        return new Resultado(estudianteId, user.getNombres(), calificacion, aciertos, totalPreguntas, respuestasIncorrectas, examenId, aprobado);
    }

    private String obtenerCalificacionCategoria(double calificacion, Map<String, Double> rangosCalificacion) {
        if (calificacion >= rangosCalificacion.get("minAU") && calificacion <= rangosCalificacion.get("maxAU")) {
            return "AU";
        } else if (calificacion >= rangosCalificacion.get("minDE") && calificacion <= rangosCalificacion.get("maxDE")) {
            return "DE";
        } else if (calificacion >= rangosCalificacion.get("minSA") && calificacion <= rangosCalificacion.get("maxSA")) {
            return "SA";
        } else {
            return "NA";
        }
    }
}
