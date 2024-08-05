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
        String nombres = req.getParameter("nombres");
        String apellido = req.getParameter("apellido");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");

        // Determinar el rol basado en el correo electrónico
        int rolId = getRoleIdFromEmail(correo);

        User user = new User(nombres, apellido, apellidoMaterno, correo, contrasena, rolId);

        UserDao userDao = new UserDao();
        boolean userCreated = userDao.create(user);

        HttpSession session = req.getSession();
        if (userCreated) {
            session.setAttribute("registerMessage", "Registro exitoso. Ahora puedes iniciar sesión.");
            session.setAttribute("messageType", "success");
            resp.sendRedirect("login.jsp");
        } else {
            session.setAttribute("registerMessage", "Error al registrar el usuario. Inténtelo de nuevo.");
            session.setAttribute("messageType", "error");
            resp.sendRedirect("registrar.jsp");
        }
    }

    private int getRoleIdFromEmail(String email) {
        if (email.matches("^\\d{5}\\w{2}\\d{3}@utez\\.edu\\.mx$")) {
            return 1; // estudiante
        } else if (email.matches("^\\w+admin\\d{2}@utez\\.edu\\.mx$")) {
            return 3; // admin
        } else if (email.matches("^\\w+@utez\\.edu\\.mx$")) {
            return 2; // docente
        }
        return 1; // Por defecto, estudiante
    }
}
