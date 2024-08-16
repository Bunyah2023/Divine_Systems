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

@WebServlet(name = "RegistrarUsuarioCoordinadorServlet", value = "/registrarCoordinador")
public class RegistrarUsuarioCoordinadorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("user");

        // Verificar si el usuario actual es un coordinador
        if (currentUser == null || currentUser.getRolId() != 4) {
            session.setAttribute("registerMessage", "Acceso denegado. Solo los coordinadores pueden registrar nuevos usuarios.");
            session.setAttribute("messageType", "error");
            resp.sendRedirect("index-coordinador.jsp");
            return;
        }

        // Recuperar parámetros del formulario
        String nombres = req.getParameter("nombres");
        String apellido = req.getParameter("apellido");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");
        int rolId = Integer.parseInt(req.getParameter("rol"));

        // Validar que los campos no sean nulos o vacíos
        if (nombres == null || nombres.isEmpty() ||
                apellido == null || apellido.isEmpty() ||
                correo == null || correo.isEmpty() ||
                contrasena == null || contrasena.isEmpty()) {
            session.setAttribute("registerMessage", "Todos los campos son obligatorios.");
            session.setAttribute("messageType", "error");
            resp.sendRedirect("index-coordinador.jsp");
            return;
        }

        // Validar roles permitidos (2: Docente, 3: Administrador)
        if (rolId != 2 && rolId != 3) {
            session.setAttribute("registerMessage", "Rol no permitido. Solo se pueden registrar docentes y administradores.");
            session.setAttribute("messageType", "error");
            resp.sendRedirect("index-coordinador.jsp");
            return;
        }

        // Crear el objeto User con los datos del formulario
        User user = new User(nombres, apellido, apellidoMaterno, correo, contrasena, rolId);

        // Intentar registrar el usuario en la base de datos
        UserDao userDao = new UserDao();
        boolean userCreated = userDao.create(user);

        if (userCreated) {
            session.setAttribute("registerMessage", "Registro exitoso. El usuario ha sido añadido.");
            session.setAttribute("messageType", "success");
        } else {
            log("Error al registrar el usuario con correo: " + correo);
            session.setAttribute("registerMessage", "Error al registrar el usuario. Inténtelo de nuevo.");
            session.setAttribute("messageType", "error");
        }

        // Redirigir de nuevo al panel del coordinador
        resp.sendRedirect("index-coordinador.jsp");
    }
}
