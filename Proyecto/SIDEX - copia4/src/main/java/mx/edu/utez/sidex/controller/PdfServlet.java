package mx.edu.utez.sidex.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.sidex.utils.DatabaseConnectionManager;
import net.sf.jasperreports.engine.JasperRunManager;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "PdfServlet", value = "/pdf")
public class PdfServlet extends HttpServlet {

    private Map<String, String> obtenerDatosDocente(String docenteId) {
        Map<String, String> datosDocente = new HashMap<>();
        try (Connection con = DatabaseConnectionManager.getConnection()) {
            String query = "SELECT nombres, apellido, apellidoMaterno FROM users WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, docenteId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String nombreCompleto = rs.getString("nombres") + " " + rs.getString("apellido") + " " + rs.getString("apellidoMaterno");
                        datosDocente.put("docente", nombreCompleto);
                    } else {
                        System.out.println("No se encontró ningún docente con el ID: " + docenteId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datosDocente;
    }

    private Map<String, String> obtenerDatosClase(String claseId) {
        Map<String, String> datosClase = new HashMap<>();
        try (Connection con = DatabaseConnectionManager.getConnection()) {
            String query = "SELECT nombre, codigo FROM clases WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, claseId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        datosClase.put("nombre", rs.getString("nombre"));
                        datosClase.put("codigo", rs.getString("codigo"));
                        System.out.println("Datos de la clase obtenidos: " + datosClase);
                    } else {
                        System.out.println("No se encontró ninguna clase con el ID: " + claseId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datosClase;
    }

    private List<Map<String, Object>> obtenerUsuariosPorClase(String claseId) {
        List<Map<String, Object>> usuarios = new ArrayList<>();
        try (Connection con = DatabaseConnectionManager.getConnection()) {
            String query = "SELECT id, nombre, email FROM users WHERE clase_id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, claseId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> usuario = new HashMap<>();
                        usuario.put("id", rs.getString("id"));
                        usuario.put("nombre", rs.getString("nombre"));
                        usuario.put("email", rs.getString("email"));
                        usuarios.add(usuario);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String claseId = req.getParameter("claseId");
        String docenteId = req.getParameter("id");

        if (claseId == null || claseId.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "claseId es requerido");
            return;
        }

        if (docenteId == null || docenteId.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "id es requerido");
            return;
        }

        Map<String, String> datosClase = obtenerDatosClase(claseId);
        List<Map<String, Object>> usuarios = obtenerUsuariosPorClase(claseId);
        Map<String, String> datosDocente = obtenerDatosDocente(docenteId);

        String nombreClase = datosClase.getOrDefault("nombre", "Nombre de Clase por Defecto");
        String codigo = datosClase.getOrDefault("codigo", "Codigo por defecto");
        String nombreDocente = datosDocente.getOrDefault("docente", "Nombre del Docente por Defecto");

        String rutaLogo = "/IMG/logo.png";
        String rutaSidex = "/IMG/sidex.jpg";
        File imagenLogo = new File(getServletContext().getRealPath(rutaLogo));
        File imagenSidex = new File(getServletContext().getRealPath(rutaSidex));

        InputStream archivoLogo = new FileInputStream(imagenLogo);
        InputStream archivoSidex = new FileInputStream(imagenSidex);

        String rutaReporte = "/WEB-INF/Reporte.jasper";
        File archivoReporte = new File(getServletContext().getRealPath(rutaReporte));
        InputStream inputReporte = new FileInputStream(archivoReporte);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("Logo", archivoLogo);
        parametros.put("sidex", archivoSidex); // Nuevo parámetro para la imagen SIDEX
        parametros.put("nombre", nombreClase);
        parametros.put("codigo", codigo);
        parametros.put("docente", nombreDocente);
        parametros.put("usuarios", usuarios);

        try (Connection con = DatabaseConnectionManager.getConnection()) {
            byte[] bytes = JasperRunManager.runReportToPdf(inputReporte, parametros, con);

            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "attachment; filename=reporte.pdf");
            resp.setContentLength(bytes.length);

            try (OutputStream os = resp.getOutputStream()) {
                os.write(bytes);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating PDF: " + e.getMessage());
        } finally {
            archivoLogo.close();
            archivoSidex.close();
            inputReporte.close();
        }
    }
}