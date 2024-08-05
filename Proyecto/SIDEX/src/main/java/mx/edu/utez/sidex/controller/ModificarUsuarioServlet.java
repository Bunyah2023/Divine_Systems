package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.sidex.dao.UserDao;
import mx.edu.utez.sidex.model.User;

import java.io.IOException;

@WebServlet(name = "ModificarUsuarioServlet", value = "/modificarUsuario")
public class ModificarUsuarioServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String nombres = req.getParameter("nombres");
            String apellido = req.getParameter("apellido");
            String apellidoMaterno = req.getParameter("apellidoMaterno");
            String correo = req.getParameter("correo");
            int rolId = Integer.parseInt(req.getParameter("rolId"));
            boolean estado = Boolean.parseBoolean(req.getParameter("estado"));

            User user = new User(id, nombres, apellido, apellidoMaterno, correo, null, rolId, estado);

            UserDao userDao = new UserDao();
            boolean updated = userDao.update(user);

            if (updated) {
                resp.sendRedirect("verregistro.jsp?success=true");
            } else {
                req.setAttribute("errorMessage", "Error al actualizar el usuario. Inténtelo de nuevo.");
                req.getRequestDispatcher("verregistro.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Datos inválidos proporcionados.");
            req.getRequestDispatcher("verregistro.jsp").forward(req, resp);
        }
    }
}
