package mx.edu.utez.sidex.model;

import java.util.Date;

public class Examen {
    private int id;
    private String titulo;
    private Date fechaApertura;
    private Date fechaCierre;
    private int claseId;
    private String descripcion;
    private String estado;
    private double calificacion;

    // Constructor
    public Examen(int id, String titulo, Date fechaApertura, Date fechaCierre, int claseId, String descripcion, String estado, double calificacion) {
        this.id = id;
        this.titulo = titulo;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.claseId = claseId;
        this.descripcion = descripcion;
        this.estado = estado;
        this.calificacion = calificacion;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public int getClaseId() {
        return claseId;
    }

    public void setClaseId(int claseId) {
        this.claseId = claseId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    // Método toString para mostrar información del examen
    @Override
    public String toString() {
        return "Examen{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fechaApertura=" + fechaApertura +
                ", fechaCierre=" + fechaCierre +
                ", claseId=" + claseId +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", calificacion=" + calificacion +
                '}';
    }
}
