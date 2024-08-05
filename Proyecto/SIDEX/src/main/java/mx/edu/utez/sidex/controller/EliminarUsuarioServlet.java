package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.sidex.dao.UserDao;
import mx.edu.utez.sidex.model.User;

import java.io.IOException;

@WebServlet(name = "EliminarUsuarioServlet", value = "/eliminarUsuario")
public class EliminarUsuarioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        UserDao userDao = new UserDao();
        User user = userDao.getOneById(id);

        if (user != null) {
            boolean backup = userDao.backupUser(user);
            if (backup) {
                boolean deleted = userDao.delete(id);
                if (deleted) {
                    resp.sendRedirect("verregistro.jsp");
                    return;
                }
            }
        }

        req.setAttribute("errorMessage", "Error al eliminar el usuario. Inténtelo de nuevo.");
        req.getRequestDispatcher("verregistro.jsp").forward(req, resp);
    }
}
