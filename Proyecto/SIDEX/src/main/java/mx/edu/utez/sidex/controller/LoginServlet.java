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

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();

        // Validar el dominio del correo
        if (!email.endsWith("@utez.edu.mx")) {
            session.setAttribute("loginMessage", "El correo debe tener el dominio @utez.edu.mx.");
            System.out.println("Error de dominio: " + email);
            resp.sendRedirect("login.jsp");
            return;
        }

        UserDao userDao = new UserDao();
        User user = userDao.getOne(email, password);

        if (user != null && user.getCorreo() != null) {
            session.setAttribute("user", user);
            System.out.println("Login exitoso para: " + email + ", rol: " + user.getRolId());

            switch (user.getRolId()) {
                case 4: // coordinador
                    System.out.println("Redirigiendo a index-coordinador.jsp");
                    resp.sendRedirect("index-coordinador.jsp");
                    break;
                case 3: // admin
                    System.out.println("Redirigiendo a index-admin.jsp");
                    resp.sendRedirect("index-admin.jsp");
                    break;
                case 2: // docente
                    System.out.println("Redirigiendo a index-docente.jsp");
                    resp.sendRedirect("index-docente.jsp");
                    break;
                case 1: // estudiante
                default:
                    System.out.println("Redirigiendo a index.jsp");
                    resp.sendRedirect("index.jsp");
                    break;
            }
        } else {
            session.setAttribute("loginMessage", "Correo o contraseña incorrectos.");
            System.out.println("Login fallido para: " + email);
            resp.sendRedirect("login.jsp");
        }
    }
}
