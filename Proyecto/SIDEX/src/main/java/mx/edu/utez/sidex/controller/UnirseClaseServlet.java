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

        // Verificar si el código de clase no es nulo o vacío
        if (classCodeInput == null || classCodeInput.trim().isEmpty()) {
            request.setAttribute("errorMessage", "El código de clase no puede estar vacío.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        // Formatear el código de clase para que coincida con el formato almacenado
        String classCodeFormatted = classCodeInput.trim().toUpperCase(); // No formatear aquí, solo normalizar el código

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Verificar si el usuario está autenticado
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        ClaseDao claseDao = null;
        try {
            claseDao = new ClaseDao();
        } catch (Exception e) {
            e.printStackTrace(); // Puedes usar un logger para registrar errores
            request.setAttribute("errorMessage", "Error al acceder a la base de datos. Intenta de nuevo más tarde.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

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
