package mx.edu.utez.sidex.model;

import java.time.LocalDate;

public class Clase {
    private int id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double minAU;
    private double maxAU;
    private double minDE;
    private double maxDE;
    private double minSA;
    private double maxSA;
    private double minNA;
    private double maxNA;
    private String codigo;
    private int creador;

    // Constructor completo
    public Clase(int id, String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin,
                 double minAU, double maxAU, double minDE, double maxDE, double minSA, double maxSA,
                 double minNA, double maxNA, String codigo, int creador) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.minAU = minAU;
        this.maxAU = maxAU;
        this.minDE = minDE;
        this.maxDE = maxDE;
        this.minSA = minSA;
        this.maxSA = maxSA;
        this.minNA = minNA;
        this.maxNA = maxNA;
        this.codigo = codigo;
        this.creador = creador;
    }

    // Constructor sin id (útil para crear nuevas clases)
    public Clase(String nombre, String descripcion, LocalDate fechaInicio, LocalDate fechaFin,
                 double minAU, double maxAU, double minDE, double maxDE, double minSA, double maxSA,
                 double minNA, double maxNA, String codigo, int creador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.minAU = minAU;
        this.maxAU = maxAU;
        this.minDE = minDE;
        this.maxDE = maxDE;
        this.minSA = minSA;
        this.maxSA = maxSA;
        this.minNA = minNA;
        this.maxNA = maxNA;
        this.codigo = codigo;
        this.creador = creador;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getMinAU() {
        return minAU;
    }

    public void setMinAU(double minAU) {
        this.minAU = minAU;
    }

    public double getMaxAU() {
        return maxAU;
    }

    public void setMaxAU(double maxAU) {
        this.maxAU = maxAU;
    }

    public double getMinDE() {
        return minDE;
    }

    public void setMinDE(double minDE) {
        this.minDE = minDE;
    }

    public double getMaxDE() {
        return maxDE;
    }

    public void setMaxDE(double maxDE) {
        this.maxDE = maxDE;
    }

    public double getMinSA() {
        return minSA;
    }

    public void setMinSA(double minSA) {
        this.minSA = minSA;
    }

    public double getMaxSA() {
        return maxSA;
    }

    public void setMaxSA(double maxSA) {
        this.maxSA = maxSA;
    }

    public double getMinNA() {
        return minNA;
    }

    public void setMinNA(double minNA) {
        this.minNA = minNA;
    }

    public double getMaxNA() {
        return maxNA;
    }

    public void setMaxNA(double maxNA) {
        this.maxNA = maxNA;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCreador() {
        return creador;
    }

    public void setCreador(int creador) {
        this.creador = creador;
    }
}
