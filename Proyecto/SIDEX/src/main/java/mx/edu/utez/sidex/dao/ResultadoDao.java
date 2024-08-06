package mx.edu.utez.sidex.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import mx.edu.utez.sidex.model.Resultado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultadoDao {
    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/activos");
            config.setUsername("root");
            config.setPassword("12345678");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al inicializar la fuente de datos", e);
        }
    }

    // Método para guardar un resultado de examen
    public boolean guardarResultado(Resultado resultado) {
        String sql = "INSERT INTO resultados (estudiante_id, estudiante_nombre, examen_id, calificacion, aciertos, respuestas_incorrectas, aprobado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, resultado.getEstudianteId());
            ps.setString(2, resultado.getEstudianteNombre());
            ps.setInt(3, resultado.getExamenId());
            ps.setDouble(4, resultado.getCalificacion());
            ps.setInt(5, resultado.getAciertos());
            ps.setInt(6, resultado.getRespuestasIncorrectas());
            ps.setBoolean(7, resultado.isAprobado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar el resultado: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener un resultado por examen ID
    public Resultado obtenerResultadoPorExamenId(int examenId) {
        Resultado resultado = null;
        String sql = "SELECT * FROM resultados WHERE examen_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = new Resultado(
                        rs.getInt("estudiante_id"),
                        rs.getString("estudiante_nombre"),
                        rs.getDouble("calificacion"),
                        rs.getInt("aciertos"),
                        rs.getInt("total_preguntas"), // Asegúrate de que esta columna existe en la tabla
                        rs.getInt("respuestas_incorrectas"),
                        examenId,
                        rs.getBoolean("aprobado")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el resultado por examen ID: " + e.getMessage());
            e.printStackTrace();
        }
        return resultado;
    }
}
