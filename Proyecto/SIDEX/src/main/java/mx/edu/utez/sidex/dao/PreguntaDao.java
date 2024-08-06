package mx.edu.utez.sidex.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import mx.edu.utez.sidex.model.Pregunta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDao {
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

    // Método para añadir preguntas a un examen
    public boolean crearPreguntas(List<Pregunta> preguntas) {
        String sql = "INSERT INTO preguntas (examen_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (Pregunta pregunta : preguntas) {
                ps.setInt(1, pregunta.getExamenId());
                ps.setString(2, pregunta.getTexto());
                ps.setString(3, pregunta.getOpcion1());
                ps.setString(4, pregunta.getOpcion2());
                ps.setString(5, pregunta.getOpcion3());
                ps.setString(6, pregunta.getOpcion4());
                ps.setInt(7, pregunta.getRespuestaCorrecta());
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            for (int result : results) {
                if (result == 0) {
                    return false; // Si alguna pregunta no se inserta correctamente
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error al crear las preguntas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para actualizar preguntas de un examen
    public boolean actualizarPreguntas(int examenId, List<Pregunta> preguntas) {
        String deleteSql = "DELETE FROM preguntas WHERE examen_id = ?";
        String insertSql = "INSERT INTO preguntas (examen_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement deletePs = con.prepareStatement(deleteSql)) {
                deletePs.setInt(1, examenId);
                deletePs.executeUpdate();
            }
            try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                for (Pregunta pregunta : preguntas) {
                    insertPs.setInt(1, pregunta.getExamenId());
                    insertPs.setString(2, pregunta.getTexto());
                    insertPs.setString(3, pregunta.getOpcion1());
                    insertPs.setString(4, pregunta.getOpcion2());
                    insertPs.setString(5, pregunta.getOpcion3());
                    insertPs.setString(6, pregunta.getOpcion4());
                    insertPs.setInt(7, pregunta.getRespuestaCorrecta());
                    insertPs.addBatch();
                }
                insertPs.executeBatch();
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar las preguntas: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener preguntas por examen ID
    public List<Pregunta> obtenerPreguntasPorExamenId(int examenId) {
        List<Pregunta> preguntas = new ArrayList<>();
        String sql = "SELECT * FROM preguntas WHERE examen_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pregunta pregunta = new Pregunta(
                            rs.getString("texto"),
                            rs.getString("opcion1"),
                            rs.getString("opcion2"),
                            rs.getString("opcion3"),
                            rs.getString("opcion4"),
                            rs.getInt("respuesta_correcta"),
                            rs.getInt("examen_id")
                    );
                    pregunta.setId(rs.getInt("id")); // Establecer el ID si es necesario
                    preguntas.add(pregunta);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener preguntas por examen ID: " + e.getMessage());
            e.printStackTrace();
        }
        return preguntas;
    }

    // Método para obtener todas las preguntas
    public List<Pregunta> obtenerTodasPreguntas() {
        List<Pregunta> preguntas = new ArrayList<>();
        String sql = "SELECT * FROM preguntas";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pregunta pregunta = new Pregunta(
                        rs.getString("texto"),
                        rs.getString("opcion1"),
                        rs.getString("opcion2"),
                        rs.getString("opcion3"),
                        rs.getString("opcion4"),
                        rs.getInt("respuesta_correcta"),
                        rs.getInt("examen_id")
                );
                pregunta.setId(rs.getInt("id"));
                preguntas.add(pregunta);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las preguntas: " + e.getMessage());
            e.printStackTrace();
        }
        return preguntas;
    }
}
