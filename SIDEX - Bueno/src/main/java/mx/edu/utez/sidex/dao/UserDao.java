package mx.edu.utez.sidex.dao;

import mx.edu.utez.sidex.utils.DatabaseConnectionManager;
import mx.edu.utez.sidex.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    public ArrayList<User> getAll() {
        ArrayList<User> lista = new ArrayList<>();
        String query = "SELECT * FROM users";
        try {
            Connection con = DatabaseConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombres"));  // Asegúrate de que el nombre de columna sea "nombres"
                u.setCorreo(rs.getString("correo"));
                u.setContra(rs.getString("contra"));
                u.setCodigo(rs.getString("codigo"));
                u.setEstado(rs.getBoolean("estado"));
                u.setApellido(rs.getString("apellido"));
                u.setApellido2(rs.getString("apellidoMaterno"));  // Asegúrate de que coincide con el nombre de la columna
                lista.add(u);
            }
            rs.close();
            ps.close();
            con.close();
            System.out.println("Total de usuarios recuperados: " + lista.size());  // Agrega esto para depuración
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean update(User u) {
        boolean flag = false;
        String query = "UPDATE users SET nombre = ?, correo = ?, contra = sha2(?, 256), codigo = ?, estado = ? WHERE id = ?";
        try {
            Connection con = DatabaseConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getContra());
            ps.setString(4, u.getCodigo());
            ps.setBoolean(5, u.isEstado());
            ps.setInt(6, u.getId());

            if (ps.executeUpdate() > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public User getOne(int id) {
        User u = new User();
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            Connection con = DatabaseConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setContra(rs.getString("contra"));
                u.setCodigo(rs.getString("codigo"));
                u.setEstado(rs.getBoolean("estado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }
    public User getOne(String correo, String contrasena) {
        User user = null;
        String sql = "SELECT * FROM users WHERE correo = ? AND contra = ?";

        // Conexión a la base de datos y ejecución de la consulta
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, correo);
            preparedStatement.setString(2, contrasena);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Si se encuentra un usuario, se crea una instancia de User y se llenan sus campos
                if (resultSet.next()) {
                    user = new User(
                            resultSet.getString("nombres"),
                            resultSet.getString("apellido"),
                            resultSet.getString("apellido2"),
                            resultSet.getString("correo"),
                            resultSet.getString("contra")
                    );
                    user.setId(resultSet.getInt("id"));
                    user.setCodigo(resultSet.getString("codigo"));
                    user.setEstado(resultSet.getBoolean("estado"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Método para crear un nuevo usuario
    public boolean create(User user) {
        String sql = "INSERT INTO users (nombres, apellido, apellidoMaterno, correo, contra, codigo, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getNombre());
            preparedStatement.setString(2, user.getApellido());
            preparedStatement.setString(3, user.getApellido2());
            preparedStatement.setString(4, user.getCorreo());
            preparedStatement.setString(5, user.getContra());
            preparedStatement.setString(6, user.getCodigo());
            preparedStatement.setBoolean(7, true);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
