package mx.edu.utez.sidex.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.sidex.dao.ExamenDao;
import mx.edu.utez.sidex.model.Examen;

import java.io.IOException;
import java.util.List;

@WebServlet("/buscarExamen")
public class BuscarExamenServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {
        String busqueda = request.getParameter("busqueda");
        String materia = request.getParameter("materia");
        String orden = request.getParameter("orden");

        ExamenDao examenDao = new ExamenDao();
        List<Examen> examenes = examenDao.buscarExamenes(busqueda, materia, orden);

        request.setAttribute("examenes", examenes);
        RequestDispatcher dispatcher = request.getRequestDispatcher("resultadoBusqueda.jsp");
        dispatcher.forward(request, response);
    }
}
