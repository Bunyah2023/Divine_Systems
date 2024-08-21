package mx.edu.utez.sidex.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import mx.edu.utez.sidex.model.Examen;
import mx.edu.utez.sidex.model.Pregunta;
import mx.edu.utez.sidex.model.Resultado;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.Timestamp;

import static mx.edu.utez.sidex.utils.DatabaseConnectionManager.getConnection;

public class ExamenDao {
    private static HikariDataSource dataSource;

    // Lista para almacenar exámenes temporales
    private static List<Examen> examenesTemporales = new ArrayList<>();

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
            e.printStackTrace();
            throw new RuntimeException("Error al inicializar la fuente de datos", e);
        }
    }

    // Método para obtener un examen por su ID
    public Examen obtenerExamenPorId(int examenId) {
        Examen examen = null;
        String sql = "SELECT * FROM examenes WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    examen = new Examen(
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
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el examen por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return examen;
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
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el examen por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean crearExamen(Examen examen, List<Pregunta> preguntas) {
        String examenQuery = "INSERT INTO examenes (titulo, fecha_hora_apertura, fecha_hora_cierre, descripcion, estado, calificacion, mejor_calificacion, materia, intentos, aprobado_por_docente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String preguntaQuery = "INSERT INTO preguntas (examen_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        int numPreguntasInsertadas = 0;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement psExamen = con.prepareStatement(examenQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psExamen.setString(1, examen.getTitulo() != null ? examen.getTitulo() : "Examen sin título");
                psExamen.setTimestamp(2, examen.getFechaHoraApertura());
                psExamen.setTimestamp(3, examen.getFechaHoraCierre());
                psExamen.setString(4, examen.getDescripcion() != null ? examen.getDescripcion() : "");
                psExamen.setString(5, examen.getEstado() != null ? examen.getEstado() : "pendiente");
                psExamen.setDouble(6, examen.getCalificacion());
                psExamen.setDouble(7, examen.getMejorCalificacion());
                psExamen.setString(8, examen.getMateria() != null ? examen.getMateria() : "");
                if (examen.getIntentos() != null) {
                    psExamen.setInt(9, examen.getIntentos());
                } else {
                    psExamen.setNull(9, java.sql.Types.INTEGER);
                }
                psExamen.setBoolean(10, examen.isAprobadoPorDocente());

                psExamen.executeUpdate();

                try (ResultSet rs = psExamen.getGeneratedKeys()) {
                    if (rs.next()) {
                        int examenId = rs.getInt(1);
                        System.out.println("Examen ID generado: " + examenId);

                        // Insertar las preguntas asociadas
                        try (PreparedStatement psPregunta = con.prepareStatement(preguntaQuery)) {
                            for (Pregunta pregunta : preguntas) {
                                psPregunta.setInt(1, examenId);
                                psPregunta.setString(2, pregunta.getTexto() != null ? pregunta.getTexto() : "");
                                psPregunta.setString(3, pregunta.getOpcion1() != null ? pregunta.getOpcion1() : "");
                                psPregunta.setString(4, pregunta.getOpcion2() != null ? pregunta.getOpcion2() : "");
                                psPregunta.setString(5, pregunta.getOpcion3() != null ? pregunta.getOpcion3() : "");
                                psPregunta.setString(6, pregunta.getOpcion4() != null ? pregunta.getOpcion4() : "");
                                psPregunta.setInt(7, pregunta.getRespuestaCorrecta());
                                psPregunta.addBatch();
                            }
                            int[] results = psPregunta.executeBatch();
                            numPreguntasInsertadas = results.length;
                            System.out.println("Número de preguntas insertadas: " + numPreguntasInsertadas);
                        }
                    }
                }

                con.commit();
                return numPreguntasInsertadas == preguntas.size();
            } catch (SQLException e) {
                if (con != null) {
                    con.rollback();
                }
                System.err.println("Error al crear el examen: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la conexión: " + e.getMessage());
            e.printStackTrace();
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


    // Método para obtener exámenes editados por docentes por clase y dentro del rango de fechas de apertura y cierre
    public List<Examen> obtenerExamenesPorClaseYFechas(int claseId) {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM ExamenesEditadosPorDocentes WHERE clase_id = ? AND fecha_hora_apertura <= NOW() AND fecha_hora_cierre >= NOW()";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, claseId);
            System.out.println("Clase ID: " + claseId); // Debugging line
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(new Examen(
                            rs.getInt("id"),
                            rs.getString("titulo"),
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
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener exámenes por clase y fechas: " + e.getMessage());
            e.printStackTrace();
        }
        return examenes;
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

    public Map<Integer, Resultado> obtenerResultados(int examenId) {
        Map<Integer, Resultado> resultados = new HashMap<>();
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
                    resultados.put(resultado.getEstudianteId(), resultado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los resultados del examen: " + e.getMessage());
            e.printStackTrace();
        }
        return resultados;
    }

    // Método para obtener el ID de clase a partir del ID del examen
    public int obtenerClaseIdPorExamen(int examenId) {
        int claseId = -1; // Valor por defecto si no se encuentra
        String sql = "SELECT clase_id FROM examenes WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    claseId = rs.getInt("clase_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el clase_id por examenId: " + e.getMessage());
            e.printStackTrace();
        }

        return claseId;
    }

    // Método para obtener el total de preguntas por examen
    public int obtenerTotalPreguntasPorExamen(int examenId) {
        int totalPreguntas = 0;
        String sql = "SELECT COUNT(*) AS total FROM preguntas WHERE examen_id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalPreguntas = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el total de preguntas por examenId: " + e.getMessage());
            e.printStackTrace();
        }

        return totalPreguntas;
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
            System.err.println("Error al obtener las respuestas correctas por examenId: " + e.getMessage());
            e.printStackTrace();
        }

        return respuestasCorrectas;
    }

    public Map<String, Double> obtenerRangosCalificacionPorClase(int claseId) {
        Map<String, Double> rangosCalificacion = new HashMap<>();
        String sql = "SELECT min_au, max_au, min_de, max_de, min_sa, max_sa FROM clases WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, claseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    rangosCalificacion.put("minAU", rs.getDouble("min_au"));
                    rangosCalificacion.put("maxAU", rs.getDouble("max_au"));
                    rangosCalificacion.put("minDE", rs.getDouble("min_de"));
                    rangosCalificacion.put("maxDE", rs.getDouble("max_de"));
                    rangosCalificacion.put("minSA", rs.getDouble("min_sa"));
                    rangosCalificacion.put("maxSA", rs.getDouble("max_sa"));
                } else {
                    System.out.println("No se encontraron rangos de calificación para claseId: " + claseId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Imprimir los rangos para depuración
        System.out.println("Rangos de calificación obtenidos: " + rangosCalificacion);

        return rangosCalificacion;
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
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener exámenes por clase: " + e.getMessage());
            e.printStackTrace();
        }
        return examenes;
    }

    // Método para buscar exámenes por nombre
    public List<Examen> buscarExamenesPorNombre(String busqueda, String orden) {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM examenes WHERE titulo LIKE ? ORDER BY id " + orden;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + busqueda + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(new Examen(
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
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar exámenes por nombre: " + e.getMessage());
            e.printStackTrace();
        }
        return examenes;
    }


    public List<Examen> obtenerExamenes(String nombreExamen, String materia, String fechaDesde, String fechaHasta, String estado, String ordenarPor) {
        List<Examen> examenes = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM examenes WHERE 1=1");

        // Agregar filtros dinámicamente
        if (nombreExamen != null && !nombreExamen.isEmpty()) {
            sql.append(" AND titulo LIKE ?");
        }
        if (materia != null && !materia.isEmpty()) {
            sql.append(" AND materia = ?");
        }
        if (fechaDesde != null && !fechaDesde.isEmpty()) {
            sql.append(" AND fecha_hora_apertura >= ?");
        }
        if (fechaHasta != null && !fechaHasta.isEmpty()) {
            sql.append(" AND fecha_hora_cierre <= ?");
        }
        if (estado != null && !estado.isEmpty()) {
            sql.append(" AND estado = ?");
        }

        // Ordenar
        if (ordenarPor != null && !ordenarPor.isEmpty()) {
            sql.append(" ORDER BY ").append(ordenarPor);
        } else {
            sql.append(" ORDER BY id DESC"); // Ordenar por defecto
        }

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (nombreExamen != null && !nombreExamen.isEmpty()) {
                ps.setString(paramIndex++, "%" + nombreExamen + "%");
            }
            if (materia != null && !materia.isEmpty()) {
                ps.setString(paramIndex++, materia);
            }
            if (fechaDesde != null && !fechaDesde.isEmpty()) {
                ps.setString(paramIndex++, fechaDesde);
            }
            if (fechaHasta != null && !fechaHasta.isEmpty()) {
                ps.setString(paramIndex++, fechaHasta);
            }
            if (estado != null && !estado.isEmpty()) {
                ps.setString(paramIndex++, estado);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(new Examen(
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
                            rs.getBoolean("aprobado_por_docente"),
                            rs.getInt("creador_id") // Agrega creador_id si está en la tabla
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener exámenes: " + e.getMessage());
            e.printStackTrace();
        }
        return examenes;
    }



    public List<Examen> buscarExamenes(String busqueda, String materia, String orden) {
        List<Examen> examenes = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM examenes WHERE 1=1");

        // Añadir cláusula para el título si se proporciona
        if (busqueda != null && !busqueda.isEmpty()) {
            sql.append(" AND titulo LIKE ?");
        }

        // Añadir cláusula para la materia si se proporciona
        if (materia != null && !materia.isEmpty()) {
            sql.append(" AND materia = ?");
        }

        // Añadir orden
        sql.append(" ORDER BY id ").append(orden.equals("desc") ? "DESC" : "ASC");

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (busqueda != null && !busqueda.isEmpty()) {
                ps.setString(paramIndex++, "%" + busqueda + "%");
            }
            if (materia != null && !materia.isEmpty()) {
                ps.setString(paramIndex++, materia);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(new Examen(
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
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar exámenes: " + e.getMessage());
            e.printStackTrace();
        }
        return examenes;
    }


    // Método para actualizar un examen existente
    public void actualizarExamen(int examenId, String titulo, String descripcion, java.util.Date fechaApertura, java.util.Date fechaCierre, Time horaActivacion, int creadorId) {
        String sql = "UPDATE examenes SET titulo = ?, descripcion = ?, fecha_apertura = ?, fecha_cierre = ?, hora_activacion = ?, creador_id = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, descripcion);
            ps.setDate(3, new java.sql.Date(fechaApertura.getTime()));
            ps.setDate(4, new java.sql.Date(fechaCierre.getTime()));
            ps.setTime(5, horaActivacion);
            ps.setInt(6, creadorId); // Actualizar creador_id
            ps.setInt(7, examenId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar el examen: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Actualizar un examen editado por un docente
    public boolean actualizarExamenEditado(Examen examen, List<Pregunta> preguntas) {
        String examenQuery = "INSERT INTO ExamenesEditadosPorDocentes (examen_id, titulo, fecha_hora_apertura, fecha_hora_cierre, descripcion, intentos, materia, clase_id, fecha_modificacion, creador_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?) " +
                "ON DUPLICATE KEY UPDATE titulo = VALUES(titulo), fecha_hora_apertura = VALUES(fecha_hora_apertura), fecha_hora_cierre = VALUES(fecha_hora_cierre), descripcion = VALUES(descripcion), " +
                "intentos = VALUES(intentos), materia = VALUES(materia), clase_id = VALUES(clase_id), fecha_modificacion = CURRENT_TIMESTAMP";

        String preguntaQuery = "INSERT INTO PreguntasEditadasPorDocentes (examen_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta, pregunta_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE texto = VALUES(texto), opcion1 = VALUES(opcion1), opcion2 = VALUES(opcion2), opcion3 = VALUES(opcion3), opcion4 = VALUES(opcion4), respuesta_correcta = VALUES(respuesta_correcta)";

        Connection con = null;
        int numPreguntasInsertadas = 0;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            // Insertar o actualizar el examen
            try (PreparedStatement psExamen = con.prepareStatement(examenQuery)) {
                psExamen.setInt(1, examen.getId());
                psExamen.setString(2, examen.getTitulo() != null ? examen.getTitulo() : "Examen sin título");
                psExamen.setTimestamp(3, examen.getFechaHoraApertura());
                psExamen.setTimestamp(4, examen.getFechaHoraCierre());
                psExamen.setString(5, examen.getDescripcion() != null ? examen.getDescripcion() : "");
                psExamen.setInt(6, examen.getIntentos() != null ? examen.getIntentos() : java.sql.Types.INTEGER);
                psExamen.setString(7, examen.getMateria() != null ? examen.getMateria() : "");
                psExamen.setInt(8, examen.getClaseId());
                psExamen.setInt(9, examen.getCreadorId());

                psExamen.executeUpdate();
            }

            // Insertar o actualizar las preguntas asociadas
            try (PreparedStatement psPregunta = con.prepareStatement(preguntaQuery)) {
                for (Pregunta pregunta : preguntas) {
                    psPregunta.setInt(1, examen.getId());
                    psPregunta.setString(2, pregunta.getTexto() != null ? pregunta.getTexto() : "");
                    psPregunta.setString(3, pregunta.getOpcion1() != null ? pregunta.getOpcion1() : "");
                    psPregunta.setString(4, pregunta.getOpcion2() != null ? pregunta.getOpcion2() : "");
                    psPregunta.setString(5, pregunta.getOpcion3() != null ? pregunta.getOpcion3() : "");
                    psPregunta.setString(6, pregunta.getOpcion4() != null ? pregunta.getOpcion4() : "");
                    psPregunta.setInt(7, pregunta.getRespuestaCorrecta());
                    psPregunta.setInt(8, pregunta.getId());

                    psPregunta.addBatch();
                }

                int[] results = psPregunta.executeBatch();
                numPreguntasInsertadas = results.length;
                System.out.println("Número de preguntas insertadas o actualizadas: " + numPreguntasInsertadas);
            }

            // Confirmar la transacción
            con.commit();
            return numPreguntasInsertadas == preguntas.size();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            System.err.println("Error al actualizar el examen editado o las preguntas: " + e.getMessage());
            e.printStackTrace();
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


    public void guardarExamenEditadoPorDocente(int examenId, String titulo, String descripcion, Date fechaApertura, Date fechaCierre) {
        String sql = "INSERT INTO ExamenesEditadosPorDocentes (examen_id, titulo, descripcion, fecha_apertura, fecha_cierre) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE titulo = ?, descripcion = ?, fecha_apertura = ?, fecha_cierre = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            ps.setString(2, titulo);
            ps.setString(3, descripcion);
            ps.setDate(4, new java.sql.Date(fechaApertura.getTime()));
            ps.setDate(5, new java.sql.Date(fechaCierre.getTime()));
            ps.setString(6, titulo);
            ps.setString(7, descripcion);
            ps.setDate(8, new java.sql.Date(fechaApertura.getTime()));
            ps.setDate(9, new java.sql.Date(fechaCierre.getTime()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar el examen editado por el docente: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public boolean guardarExamenEditadoPorDocente(int examenId, String titulo, String descripcion, java.sql.Date fechaApertura, java.sql.Date fechaCierre, int creadorId) {
        String sql = "INSERT INTO ExamenesEditadosPorDocentes (examen_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta, modificador_rol) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE texto = ?, opcion1 = ?, opcion2 = ?, opcion3 = ?, opcion4 = ?, respuesta_correcta = ?, modificador_rol = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            ps.setString(2, titulo); // Esto puede ser modificado a otro valor si es necesario
            ps.setString(3, "Opción 1"); // Cambia esto a la opción correcta
            ps.setString(4, "Opción 2"); // Cambia esto a la opción correcta
            ps.setString(5, "Opción 3"); // Cambia esto a la opción correcta
            ps.setString(6, "Opción 4"); // Cambia esto a la opción correcta
            ps.setInt(7, 1); // Cambia esto al número de respuesta correcta
            ps.setString(8, "Docente");

            ps.setString(9, titulo); // Para el UPDATE
            ps.setString(10, "Opción 1"); // Para el UPDATE
            ps.setString(11, "Opción 2"); // Para el UPDATE
            ps.setString(12, "Opción 3"); // Para el UPDATE
            ps.setString(13, "Opción 4"); // Para el UPDATE
            ps.setInt(14, 1); // Para el UPDATE
            ps.setString(15, "Docente");

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar el examen editado: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Examen obtenerUltimoExamenEditadoParaLaClase(int examenId) {
        Examen examenEditado = null;
        String sql = "SELECT * FROM ExamenesEditadosPorDocentes WHERE examen_id = ? ORDER BY fecha_modificacion DESC LIMIT 1";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("examen_id");
                String titulo = rs.getString("texto"); // Ajusta esto según tu estructura de base de datos
                String descripcion = rs.getString("descripcion");
                Date fechaApertura = rs.getDate("fecha_apertura");
                Date fechaCierre = rs.getDate("fecha_cierre");

                // Obtener el `claseId` a partir del examen original, si es necesario
                // Aquí se asume que el `claseId` está almacenado en la tabla `examenes`
                int claseId = obtenerClaseIdPorExamenId(id); // Implementa este método si es necesario

                // Crear el objeto Examen utilizando el constructor correcto y llenando todos los campos necesarios
                examenEditado = new Examen(
                        id,
                        titulo,
                        fechaApertura,
                        fechaCierre,
                        fechaApertura, // Asignando `fechaHoraApertura` como `fechaApertura` ya que no está claro en el diseño
                        fechaCierre, // Asignando `fechaHoraCierre` como `fechaCierre` ya que no está claro en el diseño
                        claseId,
                        descripcion,
                        "Activo", // Estado, ajusta según tu necesidad
                        0.0, // Calificación, ajusta según tu necesidad
                        0.0, // Mejor Calificación, ajusta según tu necesidad
                        "Desconocida", // Materia, ajusta según tu necesidad
                        null, // Intentos, ajusta según tu necesidad
                        true // AprobadoPorDocente, ajusta según tu necesidad
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examenEditado;
    }

    private int obtenerClaseIdPorExamenId(int examenId) {
        int claseId = -1; // Valor predeterminado si no se encuentra el claseId
        String sql = "SELECT clase_id FROM examenes WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                claseId = rs.getInt("clase_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return claseId;
    }


    public List<Examen> obtenerUltimosExamenesEditadosPorDocente(int creadorId) {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM ExamenesEditadosPorDocentes ed " +
                "JOIN examenes e ON ed.examen_id = e.id " +
                "WHERE ed.creador_id = ? " +
                "ORDER BY ed.fecha_modificacion DESC"; // Asegúrate de tener un campo 'fecha_modificacion' o similar

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, creadorId);

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
                    examenes.add(examen);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los últimos exámenes editados por el docente: " + e.getMessage());
            e.printStackTrace();
        }

        return examenes;
    }

    public List<Examen> obtenerExamenesCerradosPorClase(int claseId) {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT id, titulo, fecha_apertura, fecha_cierre, clase_id, descripcion, estado, calificacion, mejor_calificacion, materia, intentos, aprobado_por_docente, creador_id FROM examenes WHERE clase_id = ? AND fecha_cierre < CURRENT_DATE";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, claseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Usamos el constructor con valores predeterminados para campos opcionales
                    Examen examen = new Examen(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getDate("fecha_apertura"),
                            rs.getDate("fecha_cierre"),
                            null, // Se omite fechaHoraApertura
                            null, // Se omite fechaHoraCierre
                            rs.getInt("clase_id"),
                            rs.getString("descripcion"),
                            rs.getString("estado"),
                            rs.getDouble("calificacion"),
                            rs.getDouble("mejor_calificacion"),
                            rs.getString("materia"),
                            (Integer) rs.getObject("intentos"),
                            rs.getBoolean("aprobado_por_docente"),
                            rs.getInt("creador_id")
                    );

                    examenes.add(examen);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los exámenes cerrados por clase: " + e.getMessage());
            e.printStackTrace();
        }

        return examenes;
    }


    public void moverExamenAHistorial(int examenId, int docenteId) {
        String sql = "UPDATE ExamenesEditadosPorDocentes SET estado = 'cerrado' WHERE examen_id = ? AND modificador_rol = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            ps.setInt(2, docenteId);  // Usamos el ID del docente para identificar quién editó el examen
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al mover el examen al historial: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public List<Examen> obtenerExamenesDelCoordinador() {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT id, titulo, fecha_apertura, fecha_cierre, fecha_hora_apertura, fecha_hora_cierre, clase_id, descripcion, estado, calificacion, mejor_calificacion, materia, intentos, aprobado_por_docente, creador_id FROM examenes";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Convertir Timestamp a Date y manejar valores nulos
                Date fechaHoraApertura = rs.getTimestamp("fecha_hora_apertura") != null ? new Date(rs.getTimestamp("fecha_hora_apertura").getTime()) : null;
                Date fechaHoraCierre = rs.getTimestamp("fecha_hora_cierre") != null ? new Date(rs.getTimestamp("fecha_hora_cierre").getTime()) : null;

                // Añadir examen a la lista
                examenes.add(new Examen(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getDate("fecha_apertura"),
                        rs.getDate("fecha_cierre"),
                        fechaHoraApertura,  // Corregido aquí
                        fechaHoraCierre,    // Corregido aquí
                        rs.getInt("clase_id"),
                        rs.getString("descripcion"),
                        rs.getString("estado"),
                        rs.getDouble("calificacion"),
                        rs.getDouble("mejor_calificacion"),
                        rs.getString("materia"),
                        (Integer) rs.getObject("intentos"),  // Puede ser null, manejar si es necesario
                        rs.getBoolean("aprobado_por_docente"),
                        rs.getInt("creador_id")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener exámenes: " + e.getMessage());
            e.printStackTrace();
        }

        return examenes;
    }





    public boolean guardarPreguntaEditadaPorDocente(int examenId, int preguntaId, String texto, String opcion1, String opcion2, String opcion3, String opcion4, int respuestaCorrecta, int rolId) {
        String sql = "INSERT INTO ExamenesEditadosPorDocentes (examen_id, pregunta_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta, modificador_rol, fecha_modificacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) " +
                "ON DUPLICATE KEY UPDATE texto = ?, opcion1 = ?, opcion2 = ?, opcion3 = ?, opcion4 = ?, respuesta_correcta = ?, modificador_rol = ?, fecha_modificacion = CURRENT_TIMESTAMP";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Insertar o actualizar los detalles de la pregunta
            ps.setInt(1, examenId);
            ps.setInt(2, preguntaId);
            ps.setString(3, texto);
            ps.setString(4, opcion1);
            ps.setString(5, opcion2);
            ps.setString(6, opcion3);
            ps.setString(7, opcion4);
            ps.setInt(8, respuestaCorrecta);
            ps.setInt(9, rolId);

            // Parámetros para la parte de actualización (UPDATE)
            ps.setString(10, texto);
            ps.setString(11, opcion1);
            ps.setString(12, opcion2);
            ps.setString(13, opcion3);
            ps.setString(14, opcion4);
            ps.setInt(15, respuestaCorrecta);
            ps.setInt(16, rolId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar la pregunta editada por el docente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    // Método auxiliar para mapear el resultado de ResultSet a un objeto Examen
// Método auxiliar para mapear el resultado de ResultSet a un objeto Examen
    private Examen mapExamen(ResultSet rs) throws SQLException {
        return new Examen(
                rs.getInt("id"),
                rs.getString("titulo"),
                null,  // Si ya no usas fecha_apertura
                null,  // Si ya no usas fecha_cierre
                rs.getTimestamp("fecha_hora_apertura"),  // Usar la columna correcta
                rs.getTimestamp("fecha_hora_cierre"),    // Usar la columna correcta
                rs.getInt("clase_id"),
                rs.getString("descripcion"),
                null, // Si ya no usas estado
                0.0,  // Si decides omitir calificación, usa un valor por defecto
                rs.getDouble("mejor_calificacion"),
                rs.getString("materia"),
                rs.getObject("intentos", Integer.class),
                rs.getBoolean("aprobado_por_docente"),
                rs.getInt("creador_id")
        );
    }



    // Método para filtrar exámenes por materia
    public List<Examen> filtrarPorMateria(String materia) {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM examenes WHERE materia = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, materia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(mapExamen(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return examenes;
    }

    // Método para filtrar exámenes desde una fecha específica
    public List<Examen> filtrarPorFechaDesde(String fechaDesde) {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM examenes WHERE fecha_hora_apertura >= ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaDesde);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(mapExamen(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return examenes;
    }

    // Método para filtrar exámenes hasta una fecha específica
    public List<Examen> filtrarPorFechaHasta(String fechaHasta) {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM examenes WHERE fecha_hora_cierre <= ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaHasta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(mapExamen(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return examenes;
    }

    // Método para filtrar exámenes por estado
    public List<Examen> filtrarPorEstado(String estado) {
        List<Examen> examenes = new ArrayList<>();
        String sql = "SELECT * FROM examenes WHERE estado = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examenes.add(mapExamen(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return examenes;
    }



    public Examen obtenerUltimoExamenEditadoPorDocente(int docenteId) {
        Examen examen = null;
        String sql = "SELECT * FROM ExamenesEditadosPorDocentes WHERE creador_id = ? ORDER BY fecha_hora_cierre DESC LIMIT 1";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, docenteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    examen = mapExamen(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el último examen editado por el docente: " + e.getMessage());
            e.printStackTrace();
        }

        return examen;
    }

    public void insertarExamenEditado(int examenId, String titulo, String descripcion, Timestamp fechaHoraApertura, Timestamp fechaHoraCierre, int claseId, int creadorId, String opcion1, String opcion2, String opcion3, String opcion4) {
        String sql = "INSERT INTO ExamenesEditadosPorDocentes " +
                "(examen_id, clase_id, pregunta_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta, fecha_modificacion, modificador_rol, creador_id, titulo, fecha_hora_apertura, fecha_hora_cierre) " +
                "SELECT ?, ?, NULL, ?, ?, ?, ?, ?, ?, NOW(), 'docente', ?, ?, ?, ? FROM examenes e WHERE e.id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, examenId);
            ps.setInt(2, claseId);
            ps.setString(3, "Pregunta editada ejemplo");
            ps.setString(4, opcion1);
            ps.setString(5, opcion2);
            ps.setString(6, opcion3);
            ps.setString(7, opcion4);
            ps.setInt(8, 1); // Asume que la respuesta correcta es 1, esto puede ser modificado según sea necesario
            ps.setInt(9, creadorId);
            ps.setString(10, titulo);
            ps.setTimestamp(11, fechaHoraApertura);
            ps.setTimestamp(12, fechaHoraCierre);
            ps.setInt(13, examenId); // Para el WHERE e.id = ?

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Examen insertado correctamente en ExamenesEditadosPorDocentes: " + examenId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void actualizarExamenEditado(int examenId, String titulo, String descripcion, Timestamp fechaHoraApertura, Timestamp fechaHoraCierre, int claseId, int creadorId, int id, List<Pregunta> preguntas) {
        String sql = "UPDATE ExamenesEditadosPorDocentes SET titulo = ?, descripcion = ?, fecha_hora_apertura = ?, fecha_hora_cierre = ?, clase_id = ? WHERE examen_id = ? AND creador_id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, descripcion);
            ps.setTimestamp(3, fechaHoraApertura);
            ps.setTimestamp(4, fechaHoraCierre);
            ps.setInt(5, claseId);
            ps.setInt(6, examenId);
            ps.setInt(7, creadorId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Examen actualizado correctamente en ExamenesEditadosPorDocentes: " + examenId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarExamen(int examenId, String titulo, String descripcion, Timestamp fechaHoraApertura, Timestamp fechaHoraCierre, int claseId, int creadorId) {
        String sql = "UPDATE examenes SET titulo = ?, descripcion = ?, fecha_hora_apertura = ?, fecha_hora_cierre = ?, clase_id = ?, creador_id = ? WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, descripcion);
            ps.setTimestamp(3, fechaHoraApertura);
            ps.setTimestamp(4, fechaHoraCierre);
            ps.setInt(5, claseId);
            ps.setInt(6, creadorId);
            ps.setInt(7, examenId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean examenEditadoExiste(int examenId, int creadorId) {
        String sql = "SELECT COUNT(*) AS count FROM ExamenesEditadosPorDocentes WHERE examen_id = ? AND creador_id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            ps.setInt(2, creadorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Examen> obtenerExamenesEnCursoPorDocente(int docenteId) {
        List<Examen> examenesEnCurso = new ArrayList<>();
        String sql = "SELECT * FROM ExamenesEditadosPorDocentes WHERE creador_id = ? AND fecha_hora_apertura <= NOW() AND fecha_hora_cierre >= NOW()";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, docenteId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Examen examen = mapExamen(rs);
                    examenesEnCurso.add(examen);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los exámenes en curso por docente: " + e.getMessage());
            e.printStackTrace();
        }

        return examenesEnCurso;
    }

    public boolean crearExamenEditadoPorDocente(Examen examen, List<Pregunta> preguntas, int docenteId) {
        String examenQuery = "INSERT INTO ExamenesEditadosPorDocentes (examen_id, clase_id, titulo, descripcion, fecha_hora_apertura, fecha_hora_cierre, intentos, materia, estado, calificacion, mejor_calificacion, aprobado_por_docente, creador_id, fecha_modificacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        String preguntaQuery = "INSERT INTO PreguntasEditadasPorDocentes (examen_id, clase_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta, creador_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection con = null;
        int numPreguntasInsertadas = 0;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement psExamen = con.prepareStatement(examenQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psExamen.setInt(1, examen.getId());
                psExamen.setInt(2, examen.getClaseId());
                psExamen.setString(3, examen.getTitulo() != null ? examen.getTitulo() : "Examen sin título");
                psExamen.setString(4, examen.getDescripcion() != null ? examen.getDescripcion() : "");
                psExamen.setTimestamp(5, examen.getFechaHoraApertura());
                psExamen.setTimestamp(6, examen.getFechaHoraCierre());
                if (examen.getIntentos() != null) {
                    psExamen.setInt(7, examen.getIntentos());
                } else {
                    psExamen.setNull(7, java.sql.Types.INTEGER);
                }
                psExamen.setString(8, examen.getMateria() != null ? examen.getMateria() : "");
                psExamen.setString(9, examen.getEstado() != null ? examen.getEstado() : "pendiente");
                psExamen.setDouble(10, examen.getCalificacion());
                psExamen.setDouble(11, examen.getMejorCalificacion());
                psExamen.setBoolean(12, examen.isAprobadoPorDocente());
                psExamen.setInt(13, docenteId);

                psExamen.executeUpdate();

                try (ResultSet rs = psExamen.getGeneratedKeys()) {
                    if (rs.next()) {
                        int examenId = rs.getInt(1);
                        System.out.println("Examen Editado ID generado: " + examenId);

                        // Insertar las preguntas asociadas
                        try (PreparedStatement psPregunta = con.prepareStatement(preguntaQuery)) {
                            for (Pregunta pregunta : preguntas) {
                                psPregunta.setInt(1, examenId);
                                psPregunta.setInt(2, examen.getClaseId());
                                psPregunta.setString(3, pregunta.getTexto() != null ? pregunta.getTexto() : "");
                                psPregunta.setString(4, pregunta.getOpcion1() != null ? pregunta.getOpcion1() : "");
                                psPregunta.setString(5, pregunta.getOpcion2() != null ? pregunta.getOpcion2() : "");
                                psPregunta.setString(6, pregunta.getOpcion3() != null ? pregunta.getOpcion3() : "");
                                psPregunta.setString(7, pregunta.getOpcion4() != null ? pregunta.getOpcion4() : "");
                                psPregunta.setInt(8, pregunta.getRespuestaCorrecta());
                                psPregunta.setInt(9, docenteId);
                                psPregunta.addBatch();
                            }
                            int[] results = psPregunta.executeBatch();
                            numPreguntasInsertadas = results.length;
                            System.out.println("Número de preguntas insertadas: " + numPreguntasInsertadas);
                        }
                    }
                }

                con.commit();
                return numPreguntasInsertadas == preguntas.size();
            } catch (SQLException e) {
                if (con != null) {
                    con.rollback();
                }
                System.err.println("Error al crear el examen editado por el docente: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la conexión: " + e.getMessage());
            e.printStackTrace();
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





    public List<Examen> mostrarExamenesEnCurso(int usuarioId, int rolId) {
        List<Examen> examenesEnCurso = new ArrayList<>();
        String sql;

        if (rolId == 2) { // Docente
            sql = "SELECT * FROM ExamenesEditadosPorDocentes WHERE creador_id = ? AND fecha_hora_apertura <= NOW() AND fecha_hora_cierre >= NOW()";
        } else if (rolId == 1) { // Estudiante
            sql = "SELECT * FROM ExamenesEditadosPorDocentes ed JOIN Inscripciones i ON ed.clase_id = i.clase_id " +
                    "WHERE i.user_id = ? AND fecha_hora_apertura <= NOW() AND fecha_hora_cierre >= NOW()";
        } else {
            // Manejar otros roles o lanzar una excepción
            return examenesEnCurso;
        }

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Examen examen = mapExamen(rs); // Asumiendo que tienes un método mapExamen que convierte un ResultSet en un objeto Examen
                    examenesEnCurso.add(examen);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los exámenes en curso para el usuario: " + e.getMessage());
            e.printStackTrace();
        }

        return examenesEnCurso;
    }




    public void activarExamenParaDocente(int examenId, int creadorId) {
        String query = "UPDATE ExamenesEditadosPorDocentes SET estado = 'activo' WHERE examen_id = ? AND creador_id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, examenId);
            ps.setInt(2, creadorId);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Examen activado correctamente para el docente con ID: " + creadorId);
            } else {
                System.out.println("No se encontró un examen editado por el docente con ID: " + creadorId + " para activar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al activar el examen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean actualizarExamenEditado(int examenId, Examen examen, List<Pregunta> preguntas, int creadorId) {
        String examenQuery = "INSERT INTO ExamenesEditadosPorDocentes (examen_id, titulo, fecha_hora_apertura, fecha_hora_cierre, descripcion, intentos, materia, clase_id, fecha_modificacion, creador_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?) " +
                "ON DUPLICATE KEY UPDATE titulo = VALUES(titulo), fecha_hora_apertura = VALUES(fecha_hora_apertura), fecha_hora_cierre = VALUES(fecha_hora_cierre), descripcion = VALUES(descripcion), " +
                "intentos = VALUES(intentos), materia = VALUES(materia), clase_id = VALUES(clase_id), fecha_modificacion = CURRENT_TIMESTAMP";

        String preguntaQuery = "INSERT INTO PreguntasEditadasPorDocentes (examen_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta, pregunta_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE texto = VALUES(texto), opcion1 = VALUES(opcion1), opcion2 = VALUES(opcion2), opcion3 = VALUES(opcion3), opcion4 = VALUES(opcion4), respuesta_correcta = VALUES(respuesta_correcta)";

        Connection con = null;
        int numPreguntasInsertadas = 0;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            // Insertar o actualizar el examen
            try (PreparedStatement psExamen = con.prepareStatement(examenQuery)) {
                psExamen.setInt(1, examenId);
                psExamen.setString(2, examen.getTitulo() != null ? examen.getTitulo() : "Examen sin título");
                psExamen.setTimestamp(3, examen.getFechaHoraApertura());
                psExamen.setTimestamp(4, examen.getFechaHoraCierre());
                psExamen.setString(5, examen.getDescripcion() != null ? examen.getDescripcion() : "");
                psExamen.setInt(6, examen.getIntentos() != null ? examen.getIntentos() : java.sql.Types.INTEGER);
                psExamen.setString(7, examen.getMateria() != null ? examen.getMateria() : "");
                psExamen.setInt(8, examen.getClaseId());
                psExamen.setInt(9, creadorId);

                psExamen.executeUpdate();
            }

            // Insertar o actualizar las preguntas asociadas
            try (PreparedStatement psPregunta = con.prepareStatement(preguntaQuery)) {
                for (Pregunta pregunta : preguntas) {
                    psPregunta.setInt(1, examenId);
                    psPregunta.setString(2, pregunta.getTexto() != null ? pregunta.getTexto() : "");
                    psPregunta.setString(3, pregunta.getOpcion1() != null ? pregunta.getOpcion1() : "");
                    psPregunta.setString(4, pregunta.getOpcion2() != null ? pregunta.getOpcion2() : "");
                    psPregunta.setString(5, pregunta.getOpcion3() != null ? pregunta.getOpcion3() : "");
                    psPregunta.setString(6, pregunta.getOpcion4() != null ? pregunta.getOpcion4() : "");
                    psPregunta.setInt(7, pregunta.getRespuestaCorrecta());
                    psPregunta.setInt(8, pregunta.getId());

                    psPregunta.addBatch();
                }

                int[] results = psPregunta.executeBatch();
                numPreguntasInsertadas = results.length;
                System.out.println("Número de preguntas insertadas o actualizadas: " + numPreguntasInsertadas);
            }

            // Confirmar la transacción
            con.commit();
            return numPreguntasInsertadas == preguntas.size();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            System.err.println("Error al actualizar el examen editado o las preguntas: " + e.getMessage());
            e.printStackTrace();
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


    public boolean crearOActualizarExamenEditadoPorDocente(Examen examen, List<Pregunta> preguntas, int creadorId) {
        String examenQuery = "INSERT INTO ExamenesEditadosPorDocentes (examen_id, titulo, fecha_hora_apertura, fecha_hora_cierre, descripcion, intentos, materia, clase_id, fecha_modificacion, creador_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?) " +
                "ON DUPLICATE KEY UPDATE titulo = VALUES(titulo), fecha_hora_apertura = VALUES(fecha_hora_apertura), fecha_hora_cierre = VALUES(fecha_hora_cierre), descripcion = VALUES(descripcion), intentos = VALUES(intentos), materia = VALUES(materia), clase_id = VALUES(clase_id), fecha_modificacion = CURRENT_TIMESTAMP";

        String preguntaQuery = "INSERT INTO PreguntasEditadasPorDocentes (examen_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta, pregunta_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE texto = VALUES(texto), opcion1 = VALUES(opcion1), opcion2 = VALUES(opcion2), opcion3 = VALUES(opcion3), opcion4 = VALUES(opcion4), respuesta_correcta = VALUES(respuesta_correcta)";

        Connection con = null;

        try {
            con = dataSource.getConnection();
            con.setAutoCommit(false);

            // Insertar o actualizar el examen
            try (PreparedStatement psExamen = con.prepareStatement(examenQuery)) {
                psExamen.setInt(1, examen.getId());
                psExamen.setString(2, examen.getTitulo() != null ? examen.getTitulo() : null);
                psExamen.setTimestamp(3, examen.getFechaHoraApertura() != null ? examen.getFechaHoraApertura() : null);
                psExamen.setTimestamp(4, examen.getFechaHoraCierre() != null ? examen.getFechaHoraCierre() : null);
                psExamen.setString(5, examen.getDescripcion() != null ? examen.getDescripcion() : null);
                if (examen.getIntentos() != null) {
                    psExamen.setInt(6, examen.getIntentos());
                } else {
                    psExamen.setNull(6, java.sql.Types.INTEGER);
                }
                psExamen.setString(7, examen.getMateria() != null ? examen.getMateria() : null);
                psExamen.setInt(8, examen.getClaseId());
                psExamen.setInt(9, creadorId);

                psExamen.executeUpdate();
            }

            // Insertar o actualizar las preguntas asociadas
            try (PreparedStatement psPregunta = con.prepareStatement(preguntaQuery)) {
                for (Pregunta pregunta : preguntas) {
                    psPregunta.setInt(1, examen.getId());
                    psPregunta.setString(2, pregunta.getTexto() != null ? pregunta.getTexto() : null);
                    psPregunta.setString(3, pregunta.getOpcion1() != null ? pregunta.getOpcion1() : null);
                    psPregunta.setString(4, pregunta.getOpcion2() != null ? pregunta.getOpcion2() : null);
                    psPregunta.setString(5, pregunta.getOpcion3() != null ? pregunta.getOpcion3() : null);
                    psPregunta.setString(6, pregunta.getOpcion4() != null ? pregunta.getOpcion4() : null);
                    psPregunta.setInt(7, pregunta.getRespuestaCorrecta());
                    psPregunta.setInt(8, pregunta.getId());

                    psPregunta.addBatch();
                }

                psPregunta.executeBatch();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            System.err.println("Error al crear o actualizar el examen editado por el docente: " + e.getMessage());
            e.printStackTrace();
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


    public boolean actualizarExamenDocente(Examen examen, List<Pregunta> preguntas, int creadorId, int claseId) {
        Connection connection = null;
        PreparedStatement insertExamenStmt = null;
        PreparedStatement updateExamenStmt = null;
        PreparedStatement deletePreguntasStmt = null;
        PreparedStatement insertPreguntaStmt = null;
        PreparedStatement checkClaseStmt = null;

        String insertExamenSql = "INSERT INTO ExamenesEditadosPorDocentes (examen_id, titulo, descripcion, fecha_hora_apertura, fecha_hora_cierre, materia, intentos, clase_id, creador_id) " +
                "SELECT id, ?, ?, ?, ?, ?, ?, ?, ? FROM examenes WHERE id = ?";
        String updateExamenSql = "UPDATE ExamenesEditadosPorDocentes SET titulo = ?, descripcion = ?, fecha_hora_apertura = ?, fecha_hora_cierre = ?, materia = ?, intentos = ? WHERE examen_id = ?";
        String deletePreguntasSql = "DELETE FROM PreguntasEditadasPorDocentes WHERE examen_id = ?";
        String insertPreguntaSql = "INSERT INTO PreguntasEditadasPorDocentes (examen_id, clase_id, texto, opcion1, opcion2, opcion3, opcion4, respuesta_correcta, creador_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String checkClaseSql = "SELECT COUNT(*) FROM clases WHERE id = ?";

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Iniciar transacción

            // Verificar que el clase_id existe en la tabla 'clases'
            checkClaseStmt = connection.prepareStatement(checkClaseSql);
            checkClaseStmt.setInt(1, examen.getClaseId());
            try (ResultSet rs = checkClaseStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("Error: 'clase_id' no existe en la tabla 'clases'.");
                    connection.rollback();
                    return false;
                }
            }

            // Verifica si el examen ya existe en 'ExamenesEditadosPorDocentes'
            boolean examenExiste = false;
            PreparedStatement checkExamenStmt = connection.prepareStatement("SELECT COUNT(*) FROM ExamenesEditadosPorDocentes WHERE examen_id = ?");
            checkExamenStmt.setInt(1, examen.getId());
            try (ResultSet rs = checkExamenStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    examenExiste = true;
                }
            }

            if (!examenExiste) {
                System.out.println("Insertando nuevo examen en ExamenesEditadosPorDocentes...");
                insertExamenStmt = connection.prepareStatement(insertExamenSql, Statement.RETURN_GENERATED_KEYS);
                insertExamenStmt.setString(1, examen.getTitulo());
                insertExamenStmt.setString(2, examen.getDescripcion());
                insertExamenStmt.setTimestamp(3, examen.getFechaHoraApertura());
                insertExamenStmt.setTimestamp(4, examen.getFechaHoraCierre());
                insertExamenStmt.setString(5, examen.getMateria());
                insertExamenStmt.setInt(6, examen.getIntentos());
                insertExamenStmt.setInt(7, examen.getClaseId());
                insertExamenStmt.setInt(8, creadorId);
                insertExamenStmt.setInt(9, examen.getId());

                int affectedRows = insertExamenStmt.executeUpdate();
                System.out.println("Filas afectadas al insertar examen: " + affectedRows);

                if (affectedRows == 0) {
                    System.out.println("Rollback: No se insertó el examen.");
                    connection.rollback();
                    return false;
                }

                try (ResultSet generatedKeys = insertExamenStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        examen.setId(generatedKeys.getInt(1));
                        System.out.println("ID del examen generado: " + examen.getId());
                    } else {
                        System.out.println("Rollback: No se pudo obtener el ID generado.");
                        connection.rollback();
                        return false;
                    }
                }
            } else {
                System.out.println("Actualizando examen existente...");
                updateExamenStmt = connection.prepareStatement(updateExamenSql);
                updateExamenStmt.setString(1, examen.getTitulo());
                updateExamenStmt.setString(2, examen.getDescripcion());
                updateExamenStmt.setTimestamp(3, examen.getFechaHoraApertura());
                updateExamenStmt.setTimestamp(4, examen.getFechaHoraCierre());
                updateExamenStmt.setString(5, examen.getMateria());
                updateExamenStmt.setInt(6, examen.getIntentos());
                updateExamenStmt.setInt(7, examen.getId());

                int examenRowsUpdated = updateExamenStmt.executeUpdate();
                System.out.println("Filas afectadas al actualizar examen: " + examenRowsUpdated);

                if (examenRowsUpdated == 0) {
                    System.out.println("Rollback: No se actualizó el examen.");
                    connection.rollback();
                    return false;
                }
            }

            System.out.println("Eliminando preguntas antiguas del examen editado...");
            deletePreguntasStmt = connection.prepareStatement(deletePreguntasSql);
            deletePreguntasStmt.setInt(1, examen.getId());
            deletePreguntasStmt.executeUpdate();

            System.out.println("Insertando nuevas preguntas...");
            insertPreguntaStmt = connection.prepareStatement(insertPreguntaSql);
            for (Pregunta pregunta : preguntas) {
                insertPreguntaStmt.setInt(1, examen.getId());
                insertPreguntaStmt.setInt(2, examen.getClaseId());
                insertPreguntaStmt.setString(3, pregunta.getTexto());
                insertPreguntaStmt.setString(4, pregunta.getOpcion1());
                insertPreguntaStmt.setString(5, pregunta.getOpcion2());
                insertPreguntaStmt.setString(6, pregunta.getOpcion3());
                insertPreguntaStmt.setString(7, pregunta.getOpcion4());
                insertPreguntaStmt.setInt(8, pregunta.getRespuestaCorrecta());
                insertPreguntaStmt.setInt(9, creadorId);

                insertPreguntaStmt.addBatch();
            }
            int[] batchResults = insertPreguntaStmt.executeBatch();
            System.out.println("Resultados del batch de preguntas: " + batchResults.length);

            connection.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            System.out.println("Error en la transacción, realizando rollback.");
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;

        } finally {
            try {
                if (insertExamenStmt != null) {
                    insertExamenStmt.close();
                }
                if (updateExamenStmt != null) {
                    updateExamenStmt.close();
                }
                if (deletePreguntasStmt != null) {
                    deletePreguntasStmt.close();
                }
                if (insertPreguntaStmt != null) {
                    insertPreguntaStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public Examen obtenerExamenEditadoPorId(int examenId) {
        Examen examen = null;
        String sql = "SELECT * FROM ExamenesEditadosPorDocente WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, examenId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    examen = new Examen();
                    examen.setId(rs.getInt("id"));
                    examen.setTitulo(rs.getString("titulo"));
                    examen.setDescripcion(rs.getString("descripcion"));
                    // y así sucesivamente para los demás campos...
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return examen;
    }

}





