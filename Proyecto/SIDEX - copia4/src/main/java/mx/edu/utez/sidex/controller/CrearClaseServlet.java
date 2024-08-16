package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.sidex.dao.ClaseDao;
import mx.edu.utez.sidex.model.Clase;
import mx.edu.utez.sidex.model.User;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/crearClase")
public class CrearClaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los parámetros del formulario
        String className = request.getParameter("className");
        String description = request.getParameter("description");
        LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
        LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));

        // Obtener calificaciones de los formularios
        double minAU = Double.parseDouble(request.getParameter("minAU"));
        double maxAU = Double.parseDouble(request.getParameter("maxAU"));
        double minDE = Double.parseDouble(request.getParameter("minDE"));
        double maxDE = Double.parseDouble(request.getParameter("maxDE"));
        double minSA = Double.parseDouble(request.getParameter("minSA"));
        double maxSA = Double.parseDouble(request.getParameter("maxSA"));
        double minNA = Double.parseDouble(request.getParameter("minNA"));
        double maxNA = Double.parseDouble(request.getParameter("maxNA"));

        // Generar un código de clase único
        String classCode = generarCodigoClase();

        // Obtener la sesión del usuario y el usuario mismo
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Crear una nueva instancia de Clase
        Clase nuevaClase = new Clase(
                className,
                description,
                startDate,
                endDate,
                minAU,
                maxAU,
                minDE,
                maxDE,
                minSA,
                maxSA,
                minNA,
                maxNA,
                classCode,
                user.getId()
        );

        // Intentar guardar la clase en la base de datos
        ClaseDao claseDao = new ClaseDao();
        boolean claseCreada = claseDao.create(nuevaClase);

        if (claseCreada) {
            // Redirigir al índice del docente con mensaje de éxito
            request.setAttribute("message", "Has creado tu clase correctamente. Código de la clase: " + classCode);
            request.getRequestDispatcher("index-docente.jsp").forward(request, response);
        } else {
            // Mostrar error y permitir reintentar
            request.setAttribute("errorMessage", "Error al crear la clase. Inténtelo de nuevo.");
            request.getRequestDispatcher("index-docente.jsp").forward(request, response);
        }
    }

    private String generarCodigoClase() {
        StringBuilder codigo = new StringBuilder();
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 9; i++) {
            if (i > 0 && i % 3 == 0) {
                codigo.append('-');
            }
            int index = (int) (Math.random() * caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        return codigo.toString();
    }
}
