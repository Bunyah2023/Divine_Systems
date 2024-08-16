package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.sidex.dao.ResultadoDao;
import mx.edu.utez.sidex.model.Resultado;

import java.io.IOException;

@WebServlet("/verResultadoExamen")
public class VerResultadoExamenServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String examenIdStr = request.getParameter("examenId");
        int examenId = examenIdStr != null ? Integer.parseInt(examenIdStr) : 0;

        ResultadoDao resultadoDao = new ResultadoDao();
        Resultado resultado = resultadoDao.obtenerResultadoPorExamenId(examenId);

        if (resultado != null) {
            request.setAttribute("resultado", resultado);
            request.getRequestDispatcher("/verResultado.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "No se encontró un resultado para el examen proporcionado.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
