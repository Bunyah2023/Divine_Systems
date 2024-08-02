package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        User user = new User(nombres, apellido, apellidoMaterno, correo, contrasena);

        UserDao userDao = new UserDao();
        boolean userCreated = userDao.create(user);

        if (userCreated) {
            resp.sendRedirect("login.jsp");
        } else {
            req.setAttribute("errorMessage", "Error al registrar el usuario. Inténtelo de nuevo.");
            req.getRequestDispatcher("RegistrarUsuario.jsp").forward(req, resp);
        }
    }
}