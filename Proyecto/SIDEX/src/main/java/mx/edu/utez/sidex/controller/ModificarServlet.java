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
@WebServlet(name = "ModificarServlet", value = "/modificar")
public class ModificarServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nombres = req.getParameter("nombres");
        String apellido = req.getParameter("apellido");
        String apellidoMaterno = req.getParameter("apellidoMaterno");
        String correo = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");
        String id = req.getParameter("id");
        //Hacer un usuario
        User u = new User();
        u.setId(Integer.parseInt(id));
        u.setNombres(nombres);
        u.setApellido(apellido);
        u.setApellidoMaterno(apellidoMaterno);
        u.setCorreo(correo);
        u.setContra(contrasena);
        //Actualizar al usuario
        UserDao dao = new UserDao();
        boolean actualizacion = dao.update(u);
        //Mandar una respuesta
        if (actualizacion) {
            //Mandar al usuario al inicio de sesión
            resp.sendRedirect("verregistro.jsp");
        } else {
            //Mandar un mensaje de errror y regesar al formulario de registro
            HttpSession sesion = req.getSession();
            sesion.setAttribute("mensaje", "No se pudo actualizar el usuario en la BD");
            resp.sendRedirect("index.jsp");
        }
    }
}