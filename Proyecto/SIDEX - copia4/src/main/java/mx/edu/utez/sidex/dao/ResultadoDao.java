package mx.edu.utez.sidex.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import mx.edu.utez.sidex.model.Examen;
import mx.edu.utez.sidex.model.Resultado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public List<Examen> obtenerExamenesCompletadosPorEstudianteYClase(int estudianteId, int claseId) {
        List<Examen> examenesCompletados = new ArrayList<>();
        String sql = "SELECT e.* FROM examenes e " +
                "JOIN resultados r ON e.id = r.examen_id " +
                "WHERE r.estudiante_id = ? AND e.clase_id = ? AND r.calificacion IS NOT NULL";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estudianteId);
            ps.setInt(2, claseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Examen examen = new Examen(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getDate("fecha_apertura"),
                            rs.getDate("fecha_cierre"),
                            rs.getTimestamp("fecha_hora_apertura"),
                            rs.getTimestamp("fecha_hora_cierre"),
                            rs.getInt("clase_id"),
                            rs.getString("descripcion"),
                            rs.getString("estado"),
                            rs.getDouble("calificacion"),
                            rs.getDouble("mejor_calificacion"),
                            rs.getString("materia"),
                            rs.getObject("intentos", Integer.class),
                            rs.getBoolean("aprobado_por_docente")
                    );
                    examenesCompletados.add(examen);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener exámenes completados por estudiante y clase: " + e.getMessage());
            e.printStackTrace();
        }

        return examenesCompletados;
    }

    public Resultado procesarRespuestas(int estudianteId, int examenId, Map<Integer, Integer> respuestas) {
        Resultado resultado = new Resultado();
        int totalPreguntas = respuestas.size();
        int aciertos = 0;

        String sqlCorrectAnswers = "SELECT id, respuesta_correcta FROM preguntas WHERE examen_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlCorrectAnswers)) {

            ps.setInt(1, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int preguntaId = rs.getInt("id");
                    int respuestaCorrecta = rs.getInt("respuesta_correcta");

                    if (respuestas.containsKey(preguntaId) && respuestas.get(preguntaId) == respuestaCorrecta) {
                        aciertos++;
                    }
                }
            }

            double calificacion = ((double) aciertos / totalPreguntas) * 100;

            String insertResultado = "INSERT INTO resultados (estudiante_id, examen_id, calificacion, aciertos, total_preguntas, respuestas_incorrectas, aprobado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psInsert = con.prepareStatement(insertResultado, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psInsert.setInt(1, estudianteId);
                psInsert.setInt(2, examenId);
                psInsert.setDouble(3, calificacion);
                psInsert.setInt(4, aciertos);
                psInsert.setInt(5, totalPreguntas);
                psInsert.setInt(6, totalPreguntas - aciertos);
                psInsert.setBoolean(7, calificacion >= 60); // Suponiendo que 60 es la calificación mínima para aprobar

                psInsert.executeUpdate();
                try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        resultado.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public int obtenerIntentosPermitidos(int examenId) {
        int intentosPermitidos = 0;
        String sql = "SELECT intentos FROM examenes_editados_por_docentes WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    intentosPermitidos = rs.getInt("intentos");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return intentosPermitidos;
    }

    public int contarIntentos(int estudianteId, int examenId) {
        int intentosRealizados = 0;
        String sql = "SELECT COUNT(*) FROM resultados WHERE estudiante_id = ? AND examen_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estudianteId);
            ps.setInt(2, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    intentosRealizados = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return intentosRealizados;
    }

    public Resultado obtenerUltimoResultado(int usuarioId, int examenId) {
        Resultado resultado = null;
        String sql = "SELECT * FROM resultados WHERE estudiante_id = ? AND examen_id = ? ORDER BY id DESC LIMIT 1";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, examenId);
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
                    resultado.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

}

