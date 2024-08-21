package mx.edu.utez.sidex.controller;

import mx.edu.utez.sidex.dao.ExamenDao;
import mx.edu.utez.sidex.dao.ResultadoDao;
import mx.edu.utez.sidex.model.Examen;
import mx.edu.utez.sidex.model.Resultado;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/guardarRespuestas")
public class GuardarRespuestasServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Iniciando procesamiento de respuestas...");

        String examenIdStr = request.getParameter("examenId");
        String usuarioIdStr = request.getParameter("usuarioId");

        System.out.println("examenIdStr: " + examenIdStr);
        System.out.println("usuarioIdStr: " + usuarioIdStr);

        if (examenIdStr == null || usuarioIdStr == null || examenIdStr.isEmpty() || usuarioIdStr.isEmpty()) {
            System.out.println("Datos de examen o usuario inválidos: examenIdStr=" + examenIdStr + ", usuarioIdStr=" + usuarioIdStr);
            response.sendRedirect("error.jsp?mensaje=Datos%20de%20examen%20o%20usuario%20inv%E1lidos.");
            return;
        }

        int examenId;
        int usuarioId;

        try {
            examenId = Integer.parseInt(examenIdStr);
            usuarioId = Integer.parseInt(usuarioIdStr);
        } catch (NumberFormatException e) {
            System.out.println("Error al convertir examenId o usuarioId a entero: " + e.getMessage());
            response.sendRedirect("error.jsp?mensaje=Error%20de%20formato%20en%20los%20datos%20de%20examen%20o%20usuario.");
            return;
        }

        // Verificar que el examen existe en ExamenesEditadosPorDocente
        ExamenDao examenDao = new ExamenDao();
        Examen examen = examenDao.obtenerExamenEditadoPorId(examenId);

        if (examen == null) {
            System.out.println("Examen no encontrado para examenId: " + examenId);
            response.sendRedirect("error.jsp?mensaje=Examen%20no%20encontrado.");
            return;
        }

        // Verificar los intentos permitidos y actuales
        ResultadoDao resultadoDao = new ResultadoDao();
        int intentosPermitidos = resultadoDao.obtenerIntentosPermitidos(examenId);
        int intentosActuales = resultadoDao.contarIntentos(usuarioId, examenId);

        if (intentosActuales >= intentosPermitidos) {
            System.out.println("Intentos máximos alcanzados: " + intentosActuales + " de " + intentosPermitidos);
            response.sendRedirect("error.jsp?mensaje=Has%20alcanzado%20el%20número%20máximo%20de%20intentos%20permitidos.");
            return;
        }

        // Obtener las respuestas enviadas por el usuario
        Map<Integer, Integer> respuestas = new HashMap<>();
        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
            String paramName = e.nextElement();
            if (paramName.startsWith("respuesta")) {
                try {
                    int preguntaId = Integer.parseInt(paramName.substring(9)); // "respuesta" + preguntaId
                    int respuesta = Integer.parseInt(request.getParameter(paramName));
                    respuestas.put(preguntaId, respuesta);
                } catch (NumberFormatException nfe) {
                    System.out.println("Error al procesar respuestas: " + nfe.getMessage());
                    response.sendRedirect("error.jsp?mensaje=Error%20en%20los%20datos%20de%20las%20respuestas.");
                    return;
                }
            }
        }

        // Procesar las respuestas y generar el resultado
        Resultado resultado = resultadoDao.procesarRespuestas(usuarioId, examenId, respuestas);

        // Redirigir al usuario con el resultado
        if (resultado != null) {
            System.out.println("Respuestas procesadas correctamente. Redirigiendo al resultado...");
            response.sendRedirect("resultadoExamen.jsp?resultadoId=" + resultado.getId());
        } else {
            System.out.println("Error al procesar las respuestas. Redirigiendo a la página de error.");
            response.sendRedirect("error.jsp?mensaje=Error%20al%20procesar%20las%20respuestas.");
        }
    }
}


