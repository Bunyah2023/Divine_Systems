package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.sidex.dao.UserDao;
import mx.edu.utez.sidex.model.User;

import java.io.IOException;

@WebServlet(name = "RegistrarUsuarioServlet", value = "/registrar")
public class RegistrarUsuarioServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String nombres = req.getParameter("nombres");
        String apellido = req.getParameter("apellido");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        // Determinar el rol basado en el dominio del correo
        int rolId = determineRoleByEmail(correo);

        // Solo permitir el registro de estudiantes (3)
        if (rolId != 1) { // 1: Estudiante
            session.setAttribute("registerMessage", "Solo se permite el registro de estudiantes.");
            session.setAttribute("messageType", "error");
            resp.sendRedirect("registrar.jsp");
            return;
        }

        User user = new User(nombres, apellido, apellidoMaterno, correo, contrasena, rolId);

        UserDao userDao = new UserDao();
        boolean userCreated = userDao.create(user);

        if (userCreated) {
            // Iniciar sesión para el nuevo usuario
            session.setAttribute("user", user);
            session.setAttribute("registerMessage", "Registro exitoso. El usuario ha sido añadido.");
            session.setAttribute("messageType", "success");
            resp.sendRedirect("index.jsp");
        } else {
            session.setAttribute("registerMessage", "Error al registrar el usuario. Inténtelo de nuevo.");
            session.setAttribute("messageType", "error");
            resp.sendRedirect("registrar.jsp");
        }
    }

    // Método para determinar el rol basado en el correo
    private int determineRoleByEmail(String correo) {
        if (correo.endsWith("@utez.edu.mx")) {
            return 1; // Estudiante
        } else {
            return 0; // Rol no permitido
        }
    }
}
