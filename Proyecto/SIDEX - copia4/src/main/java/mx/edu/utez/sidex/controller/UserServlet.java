package mx.edu.utez.sidex.controller;

import mx.edu.utez.sidex.dao.UserDao;
import mx.edu.utez.sidex.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/verregistro")
public class UserServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Porque estamos mandando un "?" es una request GET
        int id = Integer.parseInt(req.getParameter("id"));
        UserDao dao = new UserDao();
        User usuario = dao.getOne1(id);
        HttpSession sesion = req.getSession();
        sesion.setAttribute("usuario", usuario);
        resp.sendRedirect("modificar.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "update":
                    updateUser(request, response);
                    break;
                case "create":
                    createUser(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error al procesar la solicitud: " + e.getMessage(), e);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> listUser = userDao.getAllUsers();
        request.setAttribute("listUser", listUser);
        request.getRequestDispatcher("verregistro.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User existingUser = userDao.getOneById(id);
        HttpSession session = request.getSession();
        session.setAttribute("usuario", existingUser);
        response.sendRedirect("modificar.jsp");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombres = request.getParameter("nombres");
        String apellido = request.getParameter("apellido");
        String apellidoMaterno = request.getParameter("apellidoMaterno");
        String correo = request.getParameter("correo");
        String contra = request.getParameter("contra");
        boolean estado = Boolean.parseBoolean(request.getParameter("estado"));
        int rolId = Integer.parseInt(request.getParameter("rolId"));

        User user = new User(nombres, apellido, apellidoMaterno, correo, contra, rolId);
        user.setId(id);
        user.setEstado(estado);

        if (userDao.update(user)) {
            response.sendRedirect("verregistro");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar el usuario");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        if (userDao.delete(id)) {
            response.sendRedirect("verregistro");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar el usuario");
        }
    }

    private void createUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombres = request.getParameter("nombres");
        String apellido = request.getParameter("apellido");
        String apellidoMaterno = request.getParameter("apellidoMaterno");
        String correo = request.getParameter("correo");
        String contra = request.getParameter("contra");
        boolean estado = Boolean.parseBoolean(request.getParameter("estado"));
        int rolId = Integer.parseInt(request.getParameter("rolId"));

        User newUser = new User(nombres, apellido, apellidoMaterno, correo, contra, rolId);
        newUser.setEstado(estado);

        if (userDao.create(newUser)) {
            response.sendRedirect("verregistro");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al crear el usuario");
        }
    }
}
