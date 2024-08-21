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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "PdfAdminServlet", value = "/pdf_admin")
public class PdfAdminServlet extends HttpServlet {

    private Map<String, String> obtenerNombreUsuario(String id) {
        Map<String, String> nombreUsuario = new HashMap<>();
        try (Connection con = DatabaseConnectionManager.getConnection()) {
            String query = "SELECT nombres, apellido, apellidoMaterno FROM users WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nombreCompleto = rs.getString("nombres") + " " + rs.getString("apellido") + " " + rs.getString("apellidoMaterno");
                nombreUsuario.put("usuario", nombreCompleto);
            } else {
                System.out.println("No se encontró ningún usuario con el ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreUsuario;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        if (id == null || id.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "id es requerido");
            return;
        }

        Map<String, String> datosUsuario = obtenerNombreUsuario(id);
        String nombreUsuario = datosUsuario.getOrDefault("usuario", "Nombre de Usuario por Defecto");

        // Seleccionar las imágenes de los assets (logo y SIDEX)
        String rutaLogo = "/IMG/logo.png";
        String rutaSidex = "/IMG/sidex.jpg";
        File imagenLogo = new File(getServletContext().getRealPath(rutaLogo));
        File imagenSidex = new File(getServletContext().getRealPath(rutaSidex));

        InputStream archivoLogo = new FileInputStream(imagenLogo);
        InputStream archivoSidex = new FileInputStream(imagenSidex);

        // Obtener ubicación y bytes del reporte
        String rutaReporte = "/WEB-INF/ListaUsuarios.jasper";
        File archivoReporte = new File(getServletContext().getRealPath(rutaReporte));
        InputStream inputReporte = new FileInputStream(archivoReporte);

        // Colocar los parámetros del reporte
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("Logo", archivoLogo);
        parametros.put("sidex", archivoSidex); // Nuevo parámetro para la imagen SIDEX
        parametros.put("usuario", nombreUsuario);

        // Generar el PDF
        try (Connection con = DatabaseConnectionManager.getConnection()) {
            byte[] bytes = JasperRunManager.runReportToPdf(inputReporte, parametros, con);

            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "attachment; filename=Lista_de_usuarios.pdf");
            resp.setContentLength(bytes.length);

            OutputStream os = resp.getOutputStream();
            os.write(bytes);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generando el PDF: " + e.getMessage());
        } finally {
            archivoLogo.close();
            archivoSidex.close();
            inputReporte.close();
        }
    }
}