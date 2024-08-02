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

@WebServlet(name = "LoginServlet", value = "/login5")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        UserDao userDao = new UserDao();
        User user = userDao.getOne(email, password);

        if (user != null && user.getCorreo() != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect("index.jsp");
        } else {
            HttpSession session = req.getSession();
            session.setAttribute("loginMessage", "Correo o contraseña incorrectos.");
            resp.sendRedirect("login.jsp");
        }
    }
}
