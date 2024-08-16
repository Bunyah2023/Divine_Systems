package mx.edu.utez.sidex.model;

import java.sql.Timestamp;
import java.util.Date;

public class Examen {
    private int id;
    private String titulo;
    private Date fechaApertura;
    private Date fechaCierre;
    private Date fechaHoraApertura; // Nuevo campo
    private Date fechaHoraCierre;   // Nuevo campo
    private int claseId;
    private String descripcion;
    private String estado;
    private double calificacion;
    private double mejorCalificacion; // Nuevo campo
    private String materia;
    private Integer intentos;
    private boolean aprobadoPorDocente; // Nuevo campo
    private int creadorId;

    // Constructor completo
    public Examen(int id, String titulo, Date fechaApertura, Date fechaCierre, Date fechaHoraApertura, Date fechaHoraCierre, int claseId, String descripcion, String estado, double calificacion, double mejorCalificacion, String materia, Integer intentos, boolean aprobadoPorDocente) {
        this.id = id;
        this.titulo = titulo;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.fechaHoraApertura = fechaHoraApertura;
        this.fechaHoraCierre = fechaHoraCierre;
        this.claseId = claseId;
        this.descripcion = descripcion;
        this.estado = estado;
        this.calificacion = calificacion;
        this.mejorCalificacion = mejorCalificacion;
        this.materia = materia;
        this.intentos = intentos;
        this.aprobadoPorDocente = aprobadoPorDocente;
        this.creadorId = creadorId;

    }

    public Examen(int id, String titulo, Date fechaApertura, Date fechaCierre, Date fechaHoraApertura, Date fechaHoraCierre, int claseId, String descripcion, String estado, double calificacion, double mejorCalificacion, String materia, Integer intentos, boolean aprobadoPorDocente, int creadorId) {
        this.id = id;
        this.titulo = titulo;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.fechaHoraApertura = fechaHoraApertura;
        this.fechaHoraCierre = fechaHoraCierre;
        this.claseId = claseId;
        this.descripcion = descripcion;
        this.estado = estado;
        this.calificacion = calificacion;
        this.mejorCalificacion = mejorCalificacion;
        this.materia = materia;
        this.intentos = intentos;
        this.aprobadoPorDocente = aprobadoPorDocente;
        this.creadorId = creadorId;
    }


    public Examen(int id, String titulo, Date fechaApertura, Date fechaCierre, Timestamp fechaHoraApertura, Timestamp fechaHoraCierre, int claseId, String descripcion, String estado, double calificacion, double mejorCalificacion, String materia, Integer intentos, boolean aprobadoPorDocente, int creadorId) {
        this.id = id;
        this.titulo = titulo;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.fechaHoraApertura = fechaHoraApertura;
        this.fechaHoraCierre = fechaHoraCierre;
        this.claseId = claseId;
        this.descripcion = descripcion;
        this.estado = estado;
        this.calificacion = calificacion;
        this.mejorCalificacion = mejorCalificacion;
        this.materia = materia;
        this.intentos = intentos;
        this.aprobadoPorDocente = aprobadoPorDocente;
        this.creadorId = creadorId;
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

    public Date getFechaHoraApertura() {
        return fechaHoraApertura;
    }

    public void setFechaHoraApertura(Date fechaHoraApertura) {
        this.fechaHoraApertura = fechaHoraApertura;
    }

    public Date getFechaHoraCierre() {
        return fechaHoraCierre;
    }

    public void setFechaHoraCierre(Date fechaHoraCierre) {
        this.fechaHoraCierre = fechaHoraCierre;
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

    public double getMejorCalificacion() {
        return mejorCalificacion;
    }

    public void setMejorCalificacion(double mejorCalificacion) {
        this.mejorCalificacion = mejorCalificacion;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public boolean isAprobadoPorDocente() {
        return aprobadoPorDocente;
    }

    public void setAprobadoPorDocente(boolean aprobadoPorDocente) {
        this.aprobadoPorDocente = aprobadoPorDocente;
    }
    public int getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(int creadorId) {
        this.creadorId = creadorId;
    }


    // Método toString para mostrar información del examen
    @Override
    public String toString() {
        return "Examen{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fechaApertura=" + fechaApertura +
                ", fechaCierre=" + fechaCierre +
                ", fechaHoraApertura=" + fechaHoraApertura +
                ", fechaHoraCierre=" + fechaHoraCierre +
                ", claseId=" + claseId +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", calificacion=" + calificacion +
                ", mejorCalificacion=" + mejorCalificacion +
                ", materia='" + materia + '\'' +
                ", intentos=" + intentos +
                ", aprobadoPorDocente=" + aprobadoPorDocente +
                ", creadorId=" + creadorId +
                '}';

    }
}