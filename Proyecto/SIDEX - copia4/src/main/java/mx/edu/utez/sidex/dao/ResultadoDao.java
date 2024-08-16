package mx.edu.utez.sidex.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import mx.edu.utez.sidex.model.Resultado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResultadoDao {
    private static final Logger logger = Logger.getLogger(ResultadoDao.class.getName());
    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://sidex.ca9j43mxxj9t.us-east-1.rds.amazonaws.com:3306/SIDEX");
            config.setUsername("admin");
            config.setPassword("#$BybeDeby8484$#");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al inicializar la fuente de datos", e);
            throw new RuntimeException("Error al inicializar la fuente de datos", e);
        }
    }

    // Método para guardar un resultado de examen
    public boolean guardarResultado(Resultado resultado) {
        String sql = "INSERT INTO resultados (estudiante_id, estudiante_nombre, examen_id, calificacion, aciertos, total_preguntas, respuestas_incorrectas, aprobado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, resultado.getEstudianteId());
            ps.setString(2, resultado.getEstudianteNombre());
            ps.setInt(3, resultado.getExamenId());
            ps.setDouble(4, resultado.getCalificacion());
            ps.setInt(5, resultado.getAciertos());
            ps.setInt(6, resultado.getTotalPreguntas());
            ps.setInt(7, resultado.getRespuestasIncorrectas());
            ps.setBoolean(8, resultado.isAprobado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al guardar el resultado", e);
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

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultado = new Resultado(
                            rs.getInt("estudiante_id"),
                            rs.getString("estudiante_nombre"),
                            rs.getDouble("calificacion"),
                            rs.getInt("aciertos"),
                            rs.getInt("total_preguntas"),
                            rs.getInt("respuestas_incorrectas"),
                            rs.getInt("examen_id"),
                            rs.getBoolean("aprobado")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el resultado por examen ID", e);
        }

        if (resultado == null) {
            logger.log(Level.INFO, "No se encontró un resultado para examen ID: " + examenId);
        } else {
            logger.log(Level.INFO, "Resultado encontrado para examen ID: " + examenId);
        }

        return resultado;
    }

    // Método para obtener resultados por examen ID
    public List<Resultado> obtenerResultadosPorExamen(int examenId) {
        List<Resultado> resultados = new ArrayList<>();
        String sql = "SELECT * FROM resultados WHERE examen_id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Resultado resultado = new Resultado(
                            rs.getInt("estudiante_id"),
                            rs.getString("estudiante_nombre"),
                            rs.getDouble("calificacion"),
                            rs.getInt("aciertos"),
                            rs.getInt("total_preguntas"),
                            rs.getInt("respuestas_incorrectas"),
                            rs.getInt("examen_id"),
                            rs.getBoolean("aprobado")
                    );
                    resultados.add(resultado);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener los resultados por examen", e);
        }

        return resultados;
    }

    // Método para obtener un resultado por examen ID y estudiante ID
    public Resultado obtenerResultadoPorExamenYEstudiante(int examenId, int estudianteId) {
        Resultado resultado = null;
        String sql = "SELECT * FROM resultados WHERE examen_id = ? AND estudiante_id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            ps.setInt(2, estudianteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resultado = new Resultado(
                            rs.getInt("estudiante_id"),
                            rs.getString("estudiante_nombre"),
                            rs.getDouble("calificacion"),
                            rs.getInt("aciertos"),
                            rs.getInt("total_preguntas"),
                            rs.getInt("respuestas_incorrectas"),
                            rs.getInt("examen_id"),
                            rs.getBoolean("aprobado")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener el resultado por examen ID y estudiante ID", e);
        }

        if (resultado == null) {
            logger.log(Level.INFO, "No se encontró un resultado para examen ID: " + examenId + " y estudiante ID: " + estudianteId);
        } else {
            logger.log(Level.INFO, "Resultado encontrado para examen ID: " + examenId + " y estudiante ID: " + estudianteId);
        }

        return resultado;
    }
}
