package mx.edu.utez.sidex.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import mx.edu.utez.sidex.model.Clase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClaseDao {
    private static final Logger LOGGER = Logger.getLogger(ClaseDao.class.getName());
    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/activos"); // Verifica que esta URL sea correcta
            config.setUsername("root"); // Verifica el nombre de usuario
            config.setPassword("12345678"); // Verifica la contraseña
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al inicializar la fuente de datos", e);
            throw new RuntimeException("Error al inicializar la fuente de datos", e);
        }
    }

    public boolean create(Clase clase) {
        String query = "INSERT INTO clases (nombre, descripcion, fecha_inicio, fecha_fin, minAU, maxAU, minDE, maxDE, minSA, maxSA, minNA, maxNA, codigo, creador) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, clase.getNombre());
            ps.setString(2, clase.getDescripcion());
            ps.setDate(3, Date.valueOf(clase.getFechaInicio()));
            ps.setDate(4, Date.valueOf(clase.getFechaFin()));
            ps.setDouble(5, clase.getMinAU());
            ps.setDouble(6, clase.getMaxAU());
            ps.setDouble(7, clase.getMinDE());
            ps.setDouble(8, clase.getMaxDE());
            ps.setDouble(9, clase.getMinSA());
            ps.setDouble(10, clase.getMaxSA());
            ps.setDouble(11, clase.getMinNA());
            ps.setDouble(12, clase.getMaxNA());
            ps.setString(13, clase.getCodigo());
            ps.setInt(14, clase.getCreador());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al crear clase", e);
            return false;
        }
    }

    public List<Clase> obtenerClasesPorCreador(String correoCreador) {
        List<Clase> clases = new ArrayList<>();
        String sql = "SELECT c.* FROM clases c JOIN users u ON c.creador = u.id WHERE u.correo = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, correoCreador);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Clase clase = extractClaseFromResultSet(resultSet);
                    clases.add(clase);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener clases por creador", ex);
        }
        return clases;
    }

    public boolean unirseClase(int userId, String codigoClase) {
        if (isAlreadyEnrolled(userId, codigoClase)) {
            LOGGER.info("El estudiante ya está inscrito en esta clase.");
            return false;
        }

        String query = "INSERT INTO inscripciones (user_id, clase_id) SELECT ?, c.id FROM clases c WHERE c.codigo = ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, codigoClase);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al unirse a la clase", e);
            return false;
        }
    }

    private boolean isAlreadyEnrolled(int userId, String codigoClase) {
        String sql = "SELECT COUNT(*) FROM inscripciones i JOIN clases c ON i.clase_id = c.id WHERE i.user_id = ? AND c.codigo = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, codigoClase);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al verificar inscripción del estudiante", e);
        }
        return false;
    }

    public List<Clase> obtenerClasesPorEstudiante(int userId) {
        List<Clase> clases = new ArrayList<>();
        String sql = "SELECT c.* FROM clases c JOIN inscripciones i ON c.id = i.clase_id WHERE i.user_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Clase clase = extractClaseFromResultSet(resultSet);
                    clases.add(clase);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener clases por estudiante", ex);
        }
        return clases;
    }

    public Clase obtenerClasePorId(int claseId) {
        String sql = "SELECT * FROM clases WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, claseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return extractClaseFromResultSet(resultSet);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener clase por ID", ex);
        }
        return null;
    }

    private Clase extractClaseFromResultSet(ResultSet resultSet) throws SQLException {
        return new Clase(
                resultSet.getInt("id"),
                resultSet.getString("nombre"),
                resultSet.getString("descripcion"),
                resultSet.getDate("fecha_inicio").toLocalDate(),
                resultSet.getDate("fecha_fin").toLocalDate(),
                resultSet.getDouble("minAU"),
                resultSet.getDouble("maxAU"),
                resultSet.getDouble("minDE"),
                resultSet.getDouble("maxDE"),
                resultSet.getDouble("minSA"),
                resultSet.getDouble("maxSA"),
                resultSet.getDouble("minNA"),
                resultSet.getDouble("maxNA"),
                resultSet.getString("codigo"),
                resultSet.getInt("creador")
        );
    }

    public Clase obtenerClasePorCodigo(String codigo) {
        Clase clase = null;
        String sql = "SELECT * FROM clases WHERE codigo = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    clase = extractClaseFromResultSet(resultSet);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener clase por código", ex);
        }
        return clase;
    }

    public List<Clase> obtenerTodasClases() {
        List<Clase> clases = new ArrayList<>();
        String sql = "SELECT * FROM clases";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Clase clase = extractClaseFromResultSet(resultSet);
                clases.add(clase);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener todas las clases", ex);
        }
        return clases;
    }
}
