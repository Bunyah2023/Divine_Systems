package mx.edu.utez.sidex.dao;

import mx.edu.utez.sidex.model.User;
import mx.edu.utez.sidex.utils.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Método para obtener todos los usuarios
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nombres"),
                        resultSet.getString("apellido"),
                        resultSet.getString("apellidoMaterno"),
                        resultSet.getString("correo"),
                        resultSet.getString("contra"), // Asumiendo que quieres obtener la contraseña, aunque no es recomendado
                        resultSet.getInt("rol_id"),
                        resultSet.getBoolean("estado")
                );
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Considera utilizar un logger para los errores
        }

        return users;
    }

    // Método para obtener un usuario por correo y contraseña
    public User getOne(String correo, String contra) {
        String sql = "SELECT * FROM users WHERE correo = ? AND contra = ?";
        User user = null;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, correo);
            statement.setString(2, contra); // Considera usar hashing y salting para la contraseña
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nombres"),
                        resultSet.getString("apellido"),
                        resultSet.getString("apellidoMaterno"),
                        resultSet.getString("correo"),
                        resultSet.getString("contra"),
                        resultSet.getInt("rol_id"),
                        resultSet.getBoolean("estado")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Considera utilizar un logger
        }

        return user;
    }

    // Método para crear un nuevo usuario
    public boolean create(User user) {
        String sql = "INSERT INTO users (nombres, apellido, apellidoMaterno, correo, contra, rol_id, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getNombres());
            statement.setString(2, user.getApellido());
            statement.setString(3, user.getApellidoMaterno());
            statement.setString(4, user.getCorreo());
            statement.setString(5, user.getContra()); // Asegúrate de encriptar la contraseña
            statement.setInt(6, user.getRolId());
            statement.setBoolean(7, user.isEstado());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException ex) {
            ex.printStackTrace(); // Considera utilizar un logger
            return false;
        }
    }

    // Método para actualizar un usuario
    public User getOne1(int id) {
        User u = new User();
        String query = "select * from users where id = ?";
        try {
            Connection con = DatabaseConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u.setNombres(rs.getString("nombres"));
                u.setApellido(rs.getString("apellido"));
                u.setApellidoMaterno(rs.getString("apellidoMaterno"));
                u.setContra(rs.getString("contra"));
                u.setCorreo(rs.getString("correo"));
                u.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    // Método para actualizar un usuario
    public boolean update(User user) {
        String sql = "UPDATE users SET nombres = ?, apellido = ?, apellidoMaterno = ?, correo = ?, contra = ? WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getNombres());
            statement.setString(2, user.getApellido());
            statement.setString(3, user.getApellidoMaterno());
            statement.setString(4, user.getCorreo());
            statement.setString(5, user.getContra()); // Asegúrate de encriptar la contraseña si fue actualizada
            statement.setInt(6, user.getId());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException ex) {
            ex.printStackTrace(); // Considera utilizar un logger
            return false;
        }
    }
    // Método para eliminar un usuario
    public boolean delete(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException ex) {
            ex.printStackTrace(); // Considera utilizar un logger
            return false;
        }
    }

    // Método para respaldar un usuario eliminado
    public boolean backupUser(User user) {
        String sql = "INSERT INTO usuarios_respaldo (id, nombres, apellido, apellidoMaterno, correo, rol_id, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, user.getId());
            statement.setString(2, user.getNombres());
            statement.setString(3, user.getApellido());
            statement.setString(4, user.getApellidoMaterno());
            statement.setString(5, user.getCorreo());
            statement.setInt(6, user.getRolId());
            statement.setBoolean(7, user.isEstado());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException ex) {
            ex.printStackTrace(); // Considera utilizar un logger
            return false;
        }
    }

    // Método para obtener un usuario por ID
    public User getOneById(int id) {
        String sql = "SELECT id, nombres, apellido, apellidoMaterno, correo, rol_id, estado FROM users WHERE id = ?";
        User user = null;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nombres"),
                        resultSet.getString("apellido"),
                        resultSet.getString("apellidoMaterno"),
                        resultSet.getString("correo"),
                        null, // La contraseña no se recupera por razones de seguridad
                        resultSet.getInt("rol_id"),
                        resultSet.getBoolean("estado")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Considera utilizar un logger para manejar los errores de SQL
        }

        return user;
    }
}
