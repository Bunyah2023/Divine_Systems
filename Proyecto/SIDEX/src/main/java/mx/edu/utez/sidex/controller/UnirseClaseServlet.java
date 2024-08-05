package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.sidex.dao.ClaseDao;
import mx.edu.utez.sidex.model.User;

import java.io.IOException;

@WebServlet("/unirseClase")
public class UnirseClaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el código de clase del formulario
        String classCodeInput = request.getParameter("classCode");
        // Formatear el código de clase para que coincida con el formato almacenado
        String classCodeFormatted = classCodeInput.replaceAll("(.{3})(?=.)", "$1-").toUpperCase();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Verificar si el usuario está autenticado
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        ClaseDao claseDao = new ClaseDao();

        // Intentar unirse a la clase con el código proporcionado
        boolean joined = claseDao.unirseClase(user.getId(), classCodeFormatted);

        if (joined) {
            request.setAttribute("message", "Te has unido a la clase con éxito.");
        } else {
            request.setAttribute("errorMessage", "Error al unirse a la clase. Código incorrecto o ya estás inscrito.");
        }

        // Redirigir a la página de inicio del estudiante
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}