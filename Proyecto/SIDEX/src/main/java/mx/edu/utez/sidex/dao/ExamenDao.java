package mx.edu.utez.sidex.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import mx.edu.utez.sidex.model.Examen;
import mx.edu.utez.sidex.model.Pregunta;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamenDao {
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

    // Método para obtener todos los exámenes
    public List<Examen> obtenerTodos() {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM examenes";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                examenes.add(new Examen(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getDate("fecha_apertura"),
                        rs.getDate("fecha_cierre"),
                        rs.getInt("clase_id"),
                        rs.getString("descripcion"),
                        rs.getString("estado"),
                        rs.getDouble("calificacion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los exámenes: " + e.getMessage());
            e.printStackTrace();
        }
        return examenes;
    }

    // Método para obtener un examen por ID
    public Examen obtenerPorId(int id) {
        String sql = "SELECT * FROM examenes WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Examen(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getDate("fecha_apertura"),
                            rs.getDate("fecha_cierre"),
                            rs.getInt("clase_id"),
                            rs.getString("descripcion"),
                            rs.getString("estado"),
                            rs.getDouble("calificacion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el examen por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Método para crear un nuevo examen
    public boolean crearExamen(Examen examen, List<Pregunta> preguntas) {
        String examenQuery = "INSERT INTO examenes (titulo, fecha_apertura, fecha_cierre, clase_id, descripcion, estado, calificacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String preguntaQuery = "INSERT INTO preguntas (examen_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            // Insertar el examen
            try (PreparedStatement ps = con.prepareStatement(examenQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, examen.getTitulo() != null ? examen.getTitulo() : "Examen sin título");
                ps.setDate(2, examen.getFechaApertura() != null ? new Date(examen.getFechaApertura().getTime()) : null);
                ps.setDate(3, examen.getFechaCierre() != null ? new Date(examen.getFechaCierre().getTime()) : null);
                ps.setInt(4, examen.getClaseId());
                ps.setString(5, examen.getDescripcion());
                ps.setString(6, examen.getEstado());
                ps.setDouble(7, examen.getCalificacion());
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                int examenId = 0;
                if (rs.next()) {
                    examenId = rs.getInt(1);
                }

                // Insertar las preguntas asociadas
                try (PreparedStatement psPregunta = con.prepareStatement(preguntaQuery)) {
                    for (Pregunta pregunta : preguntas) {
                        psPregunta.setInt(1, examenId);
                        psPregunta.setString(2, pregunta.getTexto());
                        psPregunta.setString(3, pregunta.getOpcion1());
                        psPregunta.setString(4, pregunta.getOpcion2());
                        psPregunta.setString(5, pregunta.getOpcion3());
                        psPregunta.setString(6, pregunta.getOpcion4());
                        psPregunta.setInt(7, pregunta.getRespuestaCorrecta());
                        psPregunta.addBatch();
                    }
                    psPregunta.executeBatch();
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al crear el examen y las preguntas: " + e.getMessage());
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Método para obtener exámenes por clase y estado (pendiente o completado)
    public List<Examen> obtenerExamenesPorClaseYEstado(int claseId, boolean completado) {
        List<Examen> examenes = new ArrayList<>();
        String estado = completado ? "completado" : "pendiente";
        String sql = "SELECT * FROM examenes WHERE clase_id = ? AND estado = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, claseId);
            ps.setString(2, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(new Examen(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getDate("fecha_apertura"),
                            rs.getDate("fecha_cierre"),
                            rs.getInt("clase_id"),
                            rs.getString("descripcion"),
                            rs.getString("estado"),
                            rs.getDouble("calificacion")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener exámenes por clase y estado: " + e.getMessage());
            e.printStackTrace();
        }
        return examenes;
    }

    // Método para obtener el último ID de examen
    public int obtenerUltimoExamenId() {
        String sql = "SELECT MAX(id) AS id FROM examenes";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el último ID de examen: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Método para actualizar un examen existente
    public boolean actualizarExamen(Examen examen) {
        String sql = "UPDATE examenes SET titulo = ?, fecha_apertura = ?, fecha_cierre = ?, clase_id = ?, descripcion = ?, estado = ?, calificacion = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, examen.getTitulo() != null ? examen.getTitulo() : "Examen sin título");
            ps.setDate(2, examen.getFechaApertura() != null ? new Date(examen.getFechaApertura().getTime()) : null);
            ps.setDate(3, examen.getFechaCierre() != null ? new Date(examen.getFechaCierre().getTime()) : null);
            ps.setInt(4, examen.getClaseId());
            ps.setString(5, examen.getDescripcion());
            ps.setString(6, examen.getEstado());
            ps.setDouble(7, examen.getCalificacion());
            ps.setInt(8, examen.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar el examen: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un examen
    public boolean eliminarExamen(int id) {
        String sql = "DELETE FROM examenes WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el examen: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener el total de preguntas por examen
    public int obtenerTotalPreguntasPorExamen(int examenId) {
        String sql = "SELECT COUNT(*) AS total FROM preguntas WHERE examen_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el total de preguntas por examen: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Método para obtener las respuestas correctas por examen
    public Map<Integer, Integer> obtenerRespuestasCorrectasPorExamen(int examenId) {
        Map<Integer, Integer> respuestasCorrectas = new HashMap<>();
        String sql = "SELECT id, respuesta_correcta FROM preguntas WHERE examen_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    respuestasCorrectas.put(rs.getInt("id"), rs.getInt("respuesta_correcta"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las respuestas correctas por examen: " + e.getMessage());
            e.printStackTrace();
        }
        return respuestasCorrectas;
    }

    // Método para obtener los rangos de calificación por clase
    public Map<String, Double> obtenerRangosCalificacionPorClase(int claseId) {
        Map<String, Double> rangos = new HashMap<>();
        String sql = "SELECT minAU, maxAU, minDE, maxDE, minSA, maxSA, minNA, maxNA FROM clases WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, claseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rangos.put("minAU", rs.getDouble("minAU"));
                    rangos.put("maxAU", rs.getDouble("maxAU"));
                    rangos.put("minDE", rs.getDouble("minDE"));
                    rangos.put("maxDE", rs.getDouble("maxDE"));
                    rangos.put("minSA", rs.getDouble("minSA"));
                    rangos.put("maxSA", rs.getDouble("maxSA"));
                    rangos.put("minNA", rs.getDouble("minNA"));
                    rangos.put("maxNA", rs.getDouble("maxNA"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los rangos de calificación: " + e.getMessage());
            e.printStackTrace();
        }
        return rangos;
    }

    // Método para obtener el ID de clase a partir del ID del examen
    public int obtenerClaseIdPorExamen(int examenId) {
        String sql = "SELECT clase_id FROM examenes WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("clase_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el ID de clase por examen: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Método para obtener exámenes por clase
    public List<Examen> obtenerExamenesPorClase(int claseId) {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM examenes WHERE clase_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, claseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(new Examen(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getDate("fecha_apertura"),
                            rs.getDate("fecha_cierre"),
                            rs.getInt("clase_id"),
                            rs.getString("descripcion"),
                            rs.getString("estado"),
                            rs.getDouble("calificacion")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener exámenes por clase: " + e.getMessage());
            e.printStackTrace();
        }
        return examenes;
    }
}
